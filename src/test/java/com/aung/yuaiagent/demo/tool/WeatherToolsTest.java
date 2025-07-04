package com.aung.yuaiagent.demo.tool;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WeatherToolsTest {

    @Resource
    ChatModel dashscopeChatModel;

    @Test
    void getWeather(){
        String content = ChatClient.create(dashscopeChatModel)
                .prompt("厦门天气如何？")
                .tools(new WeatherTools())
                .call()
                .content();
        System.out.println(content);
        Assertions.assertNotNull(content);

    }
}