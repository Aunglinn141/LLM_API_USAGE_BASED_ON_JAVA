package com.aung.yuaiagent.app;


import com.aung.yuaiagent.advisor.MyLoggerAdvisor;
import com.aung.yuaiagent.advisor.ReReadingAdvisor;
import com.aung.yuaiagent.chat_memory.FileBasedChatMemory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;


@Component
@Slf4j

public class HappyApp {
    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = "扮演情感心里领域的专家，开场向用户表明身份，告知能解决的用户的情况问题。引用客户详述事情经过，对方反应及自身想法，一边给出解决方案";

    public HappyApp(ChatModel dashScopeChatModel){
        String filedir  = System.getProperty("user.dir") + "/tmp/chat_memory";
        ChatMemory chatmemory = new FileBasedChatMemory(filedir);
//        ChatMemory chatmemory = new InMemoryChatMemory();
        chatClient = ChatClient.builder(dashScopeChatModel).defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(new MessageChatMemoryAdvisor(chatmemory),
                        new MyLoggerAdvisor(),
                        new ReReadingAdvisor())
                .build();
    }

    /**
     *
     * @param message
     * @param chatID
     * @return
     */
    public String chat (String message, String chatID){
        ChatResponse response = chatClient.prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatID)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .chatResponse();
        String content = null;
        if(response != null){
            content = response.getResult().getOutput().getText();
        }
        log.info("context:{}", content);
        return content;
    }
    record ActorsFilms(String actor, List<String> movies) {
    }


    /**
     **making structural output and make a recording function
     * @param message
     * @param chatID
     * @return
     */
    public ActorsFilms chatReport(String message, String chatID) {
        ActorsFilms actorsFilms = chatClient.prompt()
                .system(SYSTEM_PROMPT )
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatID)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .entity(ActorsFilms.class);
        log.info("ActorFilms:{}", actorsFilms);
        return actorsFilms;
    }
}
