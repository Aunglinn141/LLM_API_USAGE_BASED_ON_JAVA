package com.aung.yuaiagent.demo.invoke;


import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Component
public class ollamaAiInvoke implements CommandLineRunner {

    @Resource(name = "ollamaChatModel")
    private OllamaChatModel ollamaCharModel;
    @Override
    public void run(String... args) throws Exception {
        AssistantMessage assistantMessage = ollamaCharModel.call(new Prompt("华侨大学")).getResult().getOutput();
        System.out.println(assistantMessage.getText());
    }

}
