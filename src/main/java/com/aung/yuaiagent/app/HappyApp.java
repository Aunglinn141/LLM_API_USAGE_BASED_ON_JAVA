package com.aung.yuaiagent.app;


import com.aung.yuaiagent.advisor.MyLoggerAdvisor;
import com.aung.yuaiagent.advisor.ReReadingAdvisor;
import com.aung.yuaiagent.chat_memory.FileBasedChatMemory;
import com.aung.yuaiagent.rag.HappyRagCustomAdvisorFactory;
import com.aung.yuaiagent.rag.QueryRewriter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vectorstore.VectorStore;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;


@Component
@Slf4j

public class HappyApp {
    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = """
            我以全能助手的身份为你服务，能帮你解决生活、学习、工作等多领域的问题。
            请你详细的说说事情的经过，对方的反应以及你自身的想法，我会根据此给出合适的解决方案。
            """;

    @Resource
    private VectorStore happyAppVectorStore;

    @Resource
    QueryRewriter queryRewriter;

//    @Resource
//    private VectorStore pgVectorVectorStore;

    @Resource
    private Advisor happyAppRagCloudAdvisor;


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
        message = queryRewriter.doQueryRewrite(message);
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

    /**
     * chat with local data
     * @param message
     * @param chatID
     * @return
     */
    public String chatWithRag (String message, String chatID){
        ChatResponse response = chatClient.prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatID)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .advisors(new MyLoggerAdvisor())
                .advisors(new QuestionAnswerAdvisor(happyAppVectorStore))
                .advisors(HappyRagCustomAdvisorFactory.createHappyRagAdvisor(happyAppVectorStore, "已婚了"))
                .call()
                .chatResponse();
        String content = null;
        if(response != null){
            content = response.getResult().getOutput().getText();
        }
        log.info("context:{}", content);
        return content;
    }


    /**
     * chat with rag cloud
     * @param message
     * @param chatID
     * @return
     */
    public String chatWithCloudRag (String message, String chatID){
        ChatResponse response = chatClient.prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatID)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .advisors(new MyLoggerAdvisor())
                .advisors(happyAppRagCloudAdvisor)
                .call()
                .chatResponse();
        String content = null;
        if(response != null){
            content = response.getResult().getOutput().getText();
        }
        log.info("context:{}", content);
        return content;
    }



    /**
     * chat with pgVector
     * @param message
     * @param chatID
     * @return
     */
    public String chatWithpgVectorRag(String message, String chatID) {
        ChatResponse response = chatClient.prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatID)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .advisors(new MyLoggerAdvisor())
//                .advisors(new QuestionAnswerAdvisor(pgVectorVectorStore))
                .call()
                .chatResponse();
        String content = null;
        if (response != null) {
            content = response.getResult().getOutput().getText();
        }
        log.info("context:{}", content);
        return content;
    }
    @Resource
    private ToolCallback[] allTools;

    /**
     * chat with tools
     * @param message
     * @param chatID
     * @return
     */
    public String doChatWithTools(String message, String chatID) {
        ChatResponse response = chatClient.prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatID)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .advisors(new MyLoggerAdvisor())
                .tools(allTools)
                .call()
                .chatResponse();
        String content = null;
        if (response != null) {
            content = response.getResult().getOutput().getText();
        }
        log.info("context:{}", content);
        return content;
    }

    @Resource
    private ToolCallbackProvider toolCallbackProvider;

    public String doChatWithMcp(String message, String chatID) {
        ChatResponse response = chatClient.prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatID)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .advisors(new MyLoggerAdvisor())
                .tools(toolCallbackProvider)
                .call()
                .chatResponse();
        String content = null;
        if (response != null) {
            content = response.getResult().getOutput().getText();
        }
        log.info("context:{}", content);
        return content;
    }

    /**
     * chat by stream
     * @param message user prompts
     * @param chatID chatID
     * @return return result and result is in stream
     */
    public Flux<String> doChatByStream (String message, String chatID) {
        message = queryRewriter.doQueryRewrite(message);
        return chatClient.prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatID)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .stream()
                .content();
    }
}
