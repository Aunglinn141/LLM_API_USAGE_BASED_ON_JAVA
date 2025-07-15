package com.aung.yuaiagent.agent;


import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.aung.yuaiagent.advisor.MyLoggerAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Component;

@Component
public class YuManus extends ToolCallAgent {

    public YuManus(ToolCallback[] availableTools, ChatModel dashscopeChatModel) {
        super(availableTools);
        this.setName("YuManus");
        String SYSTEM_PROMPT = """
                You are YuManus, an all-capable AI assistant.aimed at solving any task presented by the user.
                You have various tools at your disposal. You can call upon to efficiently completer complex request.
                """;
        this.setSystemPrompt(SYSTEM_PROMPT);
        String NEXT_STEP_PROMPT = """
        Base on user needs, proactively select the most appropriate tool or combination of tool.
        For complex task, you can break down the problem and use different tools to solve by step to solve it.
        After using each tool, cleatly explain the execution result and suggest the next steps.
        If you want to stop the interaction at any point, use the `doTerminate` tool / function call.
        """;
        this.setNextStepPrompt(NEXT_STEP_PROMPT);
        this.setMaxSteps(20);

        // initialize the client
        ChatClient chatClient =  ChatClient.builder(dashscopeChatModel).defaultAdvisors(new MyLoggerAdvisor())
                .build();
        this.setChatClient(chatClient);
    }
}
