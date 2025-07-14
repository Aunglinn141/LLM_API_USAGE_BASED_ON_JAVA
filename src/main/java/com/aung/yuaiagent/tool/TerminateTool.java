package com.aung.yuaiagent.tool;

import org.springframework.ai.tool.annotation.Tool;

public class TerminateTool {

    @Tool(description = """
            Terminate the interaction when the request is met OR if the assistant cannot processed further with the task.
            When you hace finished all the task, call this tool to end the work.
            """)
    public String doTerminate(){
        return "Task finished";
    }
}
