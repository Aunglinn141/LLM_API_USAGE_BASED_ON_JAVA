package com.aung.yuaiagent.agent;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.tool.ToolCallback;

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

    public ToolCallAgent(ToolCallback[] availableTools){
        super();
        this.availableTools = availableTools;
        this.toolCallingManager = ToolCallingManager.builder().build();
        this.chatOptions = DashScopeChatOptions.builder()
                .withProxyToolCalls(true) // not allow spring ai internal tool, us the tool that we build
                .build();
    }
    @Override
    public boolean think() {
        return false;
    }

    @Override
    public String act() {
        return "ToolCall";
    }

    @Override
    public String step() {
        return super.step();
    }
}
