package com.aung.yuaiagent.rag;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.stereotype.Component;

@Component
public class QueryRewriter {

    private final QueryTransformer queryTransformer;

    public QueryRewriter(ChatModel dashscopeChatModel) {
        ChatClient.Builder builder = ChatClient.builder(dashscopeChatModel);

        // Create Rewrite converter
        queryTransformer = RewriteQueryTransformer.builder().chatClientBuilder(builder).build();
    }

    // Do rewrite
    public String doQueryRewrite(String query){
        return queryTransformer.transform(new Query(query)).text();
    }
}
