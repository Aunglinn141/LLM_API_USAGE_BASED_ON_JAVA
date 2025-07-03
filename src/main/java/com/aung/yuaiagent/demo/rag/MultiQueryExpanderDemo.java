package com.aung.yuaiagent.demo.rag;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MultiQueryExpanderDemo {

    private final ChatClient.Builder chatClientBuilder;

    public MultiQueryExpanderDemo(ChatModel dashScopeChatModel) {
        this.chatClientBuilder = ChatClient.builder(dashScopeChatModel);
    }

    public List<Query> expand (String query){
        MultiQueryExpander queryExpander = MultiQueryExpander.builder()
                // setting LLM Customer side
                .chatClientBuilder(chatClientBuilder)

                // not include original search
                .includeOriginal(false)

                //the Number of search that the original searches generate
                .numberOfQueries(3)
                .build();
        List<Query> queries = queryExpander.expand(new Query(query));
        return queries;
    }
}
