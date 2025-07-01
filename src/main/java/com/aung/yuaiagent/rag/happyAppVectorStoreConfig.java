package com.aung.yuaiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class happyAppVectorStoreConfig {

    @Resource
    happyAppDocumentLoader happyAppDocumentLoader;

    @Bean
    VectorStore happyAppVectorStore(@Qualifier("dashscopeEmbeddingModel") EmbeddingModel embeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(embeddingModel)
                .build();

        // read local file
        List<Document> documents = happyAppDocumentLoader.loaderMarkdownDocument();

        simpleVectorStore.add(documents);
        return simpleVectorStore;
    }
}
