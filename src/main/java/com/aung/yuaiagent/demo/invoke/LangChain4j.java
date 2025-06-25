package com.aung.yuaiagent.demo.invoke;

import dev.langchain4j.community.model.dashscope.QwenChatModel;

public class LangcChain4j {
    public static void main(String[] args) {
        QwenChatModel qwenChatModel = QwenChatModel.builder()
                .apiKey(TestApiKey.API_KEY)
                .modelName("qwen-max")
                .build();
        String result = qwenChatModel.chat("你好！");
        System.out.println(result);
    }
}
