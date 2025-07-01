package com.aung.yuaiagent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;


@SpringBootTest
class HappyAppTest {

    @Resource
    private HappyApp happyApp;

    @Test
    void testChat(){
        String chatId = UUID.randomUUID().toString();

//        first chat
        String message = "你好，我是代码校园";
        String response = happyApp.chat(message, chatId);
        Assertions.assertNotNull(response);
//
////        Second chat
//        message = "我想让我的另一半“王恩在”更喜欢我";
//        response = happyApp.chat(message, chatId);
//        Assertions.assertNotNull(response);
//
////        Third chat
//        message = "我的另一半叫什么名字，你帮我回忆一下，我刚刚讲过";
//        response = happyApp.chat(message, chatId);
//        Assertions.assertNotNull(response);
    }

    @Test
    void chatReport() {

        String chatId = UUID.randomUUID().toString();

//        first chat
        String message = "你好，我是代码校园,我晚上睡不着怎么办，给我十条解决方案";
        HappyApp.ActorsFilms actorsFilms = happyApp.chatReport(message, chatId);
        System.out.println(actorsFilms);
        Assertions.assertNotNull(actorsFilms);
    }

    @Test
    void chatWithRag() {
        String chatId = UUID.randomUUID().toString();
        String message = "我的apple watch 连接不上iphone 了怎么办？给我一些建议";
        String response = happyApp.chatWithRag(message, chatId);
        System.out.println(response);
        Assertions.assertNotNull(response);
    }

    @Test
    void chatWithCloudRag() {
        String chatId = UUID.randomUUID().toString();
        String message = "我谈恋爱了！ 请给我一些建议";
        String response = happyApp.chatWithCloudRag(message, chatId);
        System.out.println(response);
        Assertions.assertNotNull(response);
    }
}