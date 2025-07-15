package com.aung.yuaiagent.agent;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.aung.yuaiagent.agent.model.AgentState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.tool.ToolCallback;

import javax.tools.Tool;
import java.util.List;
import java.util.stream.Collectors;

/**
 * optimize tool interface concept using
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = false)
public class ToolCallAgent extends ReActAgent {

    // available tools
    private final ToolCallback[] availableTools;

    // tool call chat response
    private ChatResponse toolCallChatResponse;

    // tool calling manager
    private final ToolCallingManager toolCallingManager;

    // not allowed internal tool, us the tool that we build
    private final ChatOptions chatOptions;

    public ToolCallAgent(ToolCallback[] availableTools) {
        super();
        this.availableTools = availableTools;
        this.toolCallingManager = ToolCallingManager.builder().build();
        this.chatOptions = DashScopeChatOptions.builder()
                .withProxyToolCalls(true) // not allow spring ai internal tool, us the tool that we build
                .build();
    }

    @Override
    public boolean think() {
        if (getNextStepPrompt() != null && !getNextStepPrompt().isEmpty()) {
            UserMessage userMessage = new UserMessage(getNextStepPrompt());
            getMessageList().add(userMessage);
        }
        List<Message> messageList = getMessageList();
        Prompt prompt = new Prompt(messageList, chatOptions);
        try {
            ChatResponse chatResponse = getChatClient().prompt(prompt)
                    .system(getSystemPrompt())
                    .tools(availableTools)
                    .call()
                    .chatResponse();

            this.toolCallChatResponse = chatResponse;
            AssistantMessage assistantMessage = null;
            if (chatResponse != null) {
                assistantMessage = chatResponse.getResult().getOutput();
            }

            String result = null;
            if (assistantMessage != null) {
                result = assistantMessage.getText();
            }
            log.info("{} is thinking {}", getName(), result);

            List<AssistantMessage.ToolCall> toolCallList = null;
            if (assistantMessage != null) {
                toolCallList = assistantMessage.getToolCalls();
                log.info("{} choose {} tool to use.", getName(), toolCallList.size());
            }
            String toolCallInfo = toolCallList.stream()
                    .map(toolCall -> String.format("tool name : %s(%s)", toolCall.name(), toolCall.arguments()))
                    .collect(Collectors.joining(","));
            log.info("{} tool call info : {}", getName(), toolCallInfo);
            if (toolCallList.isEmpty()) {
                getMessageList().add(assistantMessage);
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            log.error("{} get some problem while thinking {}", getName(), e.getMessage());
            getMessageList().add(new AssistantMessage("I have some problem, please try again" + e.getMessage()));
            return false;
        }
    }

    // call tool to do action
    @Override
    public String act() {
        if (!toolCallChatResponse.hasToolCalls()) {
            return "no ToolCall response";
        }
        Prompt prompt = new Prompt(getMessageList(), chatOptions); // get message list
        ToolExecutionResult toolExecutionResult = toolCallingManager.executeToolCalls(prompt, toolCallChatResponse); // call tool

        // add tool response to message list
        setMessageList(toolExecutionResult.conversationHistory());

        // set state
        ToolResponseMessage toolResponseMessage = (ToolResponseMessage) CollUtil.getLast(toolExecutionResult.conversationHistory());
        String results = toolResponseMessage.getResponses().stream()
                .map(response -> "tool" + response.name() + "finish it work and result is: " + response.responseData())
                .collect(Collectors.joining("\n"));
        // determine if agent should terminate to use the tool
        if (toolResponseMessage.getResponses().stream().anyMatch(response -> response.responseData().equals("doTerminate"))) {
            setState(AgentState.FINISHED);
        }
        log.info("tool response result : " + results);
        return results;
    }

}

