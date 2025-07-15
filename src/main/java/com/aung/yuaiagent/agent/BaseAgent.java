package com.aung.yuaiagent.agent;

import com.aung.yuaiagent.agent.model.AgentState;
import io.micrometer.common.util.StringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 抽象基础代理类，用来管理代理状态和执行流程
 *
 * 提供状态转换，内存管理和基于步骤的执行循环的基础功能
 *
 * 子类必须实现step（）方法，用来执行具体的代理logic
 *
 */

@Slf4j
@Data
public abstract class BaseAgent {

    // main attribute
    private String name;

    //prompts
    private String systemPrompt;
    private String nextStepPrompt;

    // state
    private AgentState state = AgentState.IDLE;

    // function control
    private int maxSteps = 10;
    private int currentStep = 0;

    //LLM
    private ChatClient chatClient;

    // message list (auto maintainance content)
    private List<Message> messageList = new ArrayList<>();

    // running agent

    /**
     * running agent logic
     * @param userPrompt user prompt
     * @return running result
     */
    public String run(String userPrompt){
        if(this.state != AgentState.IDLE){
            throw new RuntimeException("Cannot run agent from state: " + this.state);
        }
        if (StringUtils.isBlank(userPrompt)){
            throw new RuntimeException("User prompt cannot be empty");
        }

        //change state to running
        this.state = AgentState.RUNNING;

        //save content(include user input) to message list
        messageList.add(new UserMessage(userPrompt));

        //save the result list
        List<String> results = new ArrayList<>();

        // execute steps loop
        try {
            for (int i = 0; i < maxSteps && state != AgentState.FINISHED; i++) {

                int stepNumber = i + 1;
                currentStep = stepNumber;
                log.info("Executing Step {} of {}", stepNumber, maxSteps);
                //execute step
                String stepResult = step();
                //save result
                String result = "Current step : " + currentStep + ":" + stepResult;
                results.add(result);
                //save content(include bot output) to message list
                messageList.add(new UserMessage(stepResult));

            }
            if (currentStep >= maxSteps) {
                state = AgentState.FINISHED;
                results.add("Terminated : Max step reached(" + maxSteps + ")");
            }
            return String.join("\n", results);
        }catch (Exception e){
            state = AgentState.ERROR;
            log.error("Error in step", e);
            return "Error in step: " + e.getMessage();
        }finally {
            //change state to idle
            this.cleanUp();
        }
    }

    public SseEmitter runStream(String userPrompt){

        SseEmitter sseEmitter = new SseEmitter(5 * 60 * 1000L);

        CompletableFuture.runAsync(() -> {
            try {

                if (this.state != AgentState.IDLE) {
                    sseEmitter.send("Cannot run agent from state: " + this.state);
                    sseEmitter.complete();
                    return;
                }
                if (StringUtils.isBlank(userPrompt)) {
                    sseEmitter.send("User prompt cannot be empty");
                    sseEmitter.complete();
                    return;
                }
            }catch (Exception e) {
                sseEmitter.completeWithError(e);
            }
            this.state = AgentState.RUNNING;

            messageList.add(new UserMessage(userPrompt));
            ArrayList<String> results = new ArrayList<>();
            try {
                for (int i = 0; i < maxSteps && state != AgentState.FINISHED; i++) {
                    int stepNumber = i + 1;
                    currentStep = stepNumber;
                    log.info("Executing Step {} of {}", stepNumber, maxSteps);
                    String stepResult = step();
                    String result = "Current step : " + currentStep + ":" + stepResult;
                    results.add(result);
    //                messageList.add(new UserMessage(stepResult));
                    sseEmitter.send(result);
                }
                if (currentStep >= maxSteps) {
                    state = AgentState.FINISHED;
                    results.add("Terminated : Max step reached(" + maxSteps + ")");
                    sseEmitter.send("Terminated : Max step reached(" + maxSteps + ")");
                }
                sseEmitter.complete();
            }catch (Exception e) {
                state = AgentState.ERROR;
                log.error("Error in step", e);
                try {
                    sseEmitter.send("Error in step: " + e.getMessage());
                    sseEmitter.complete();
                } catch (Exception e1) {
                    sseEmitter.completeWithError(e1);
                }
            }finally {
                this.cleanUp();
            }
        });
        sseEmitter.onTimeout(() ->{
            this.state = AgentState.ERROR;
            this.cleanUp();
            log.warn("Timeout");
        });
        sseEmitter.onCompletion(() -> {
            if (this.state == AgentState.RUNNING) {
                this.state = AgentState.FINISHED;
            }
            this.cleanUp();
            log.info("Completed");
        });
        return sseEmitter;
    }

    protected void cleanUp() {
    }

    /**
     * step logic
     * @return step result
     */

    public abstract String step();
}
