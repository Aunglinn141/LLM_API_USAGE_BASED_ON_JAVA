package com.aung.yuaiagent.agent;

import com.aung.yuaiagent.agent.model.AgentState;
import io.micrometer.common.util.StringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.ArrayList;
import java.util.List;

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

    protected void cleanUp() {
    }

    /**
     * step logic
     * @return
     */

    protected abstract String step();
}
