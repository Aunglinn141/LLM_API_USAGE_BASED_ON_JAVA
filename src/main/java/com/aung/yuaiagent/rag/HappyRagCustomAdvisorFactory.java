package com.aung.yuaiagent.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HappyRagCustomAdvisorFactory {

    public static Advisor createHappyRagAdvisor(VectorStore vectorStore, String status){

        // 过滤特定状态条件的文档
        Filter.Expression expression= new FilterExpressionBuilder()
                .eq("status", status)
                .build();

        // Create Document index
        VectorStoreDocumentRetriever documentRetriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)
                .filterExpression(expression)
                .similarityThreshold(0.3)
                .topK(10)
                .build();

        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(documentRetriever)
//                .queryAugmenter(ContextualQueryAugmenter.builder().allowEmptyContext(true).build())
                .queryAugmenter(HappyAppContextualQueryAugumenterFactory.createInstance())
                .build();
    }
}
