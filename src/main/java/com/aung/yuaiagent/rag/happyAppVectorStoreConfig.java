package com.aung.yuaiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class happyAppVectorStoreConfig {

    @Resource
    HappyAppDocumentLoader happyAppDocumentLoader;

    @Autowired
    MyTokenTextSplitter myTokenTextSplitter;

    @Resource
    MyKeywordEnricher myKeywordEnricher;
//    @Autowired
//    private VectorStore vectorStore;

    @Bean
    VectorStore happyAppVectorStore(@Qualifier("dashscopeEmbeddingModel") EmbeddingModel embeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(embeddingModel)
                .build();

        // read local file
        List<Document> documents = happyAppDocumentLoader.loaderMarkdownDocument();

        // customized split, recommend to use cloud service and manual split
        List<Document> splitDocuments = myTokenTextSplitter.splitCustomized(documents);

        //customized enrichment, auto full the ordinary keyword info
        List<Document> enrichedDocuments = myKeywordEnricher.enrichDocuments(splitDocuments);

        simpleVectorStore.add(enrichedDocuments);
        return simpleVectorStore;
    }

//    private void etl(){

        // Drawing word from pdf file
//        PDFReader pdfReader = new PagepdfDocumentReader("test.pdf");
//        List<Document> documents = pdfReader.read();

        //converter: split word and add summary
//        MyTokenTextSplitter myTokenTextSplitter = new MyTokenTextSplitter();
//        List<Document> documentList = myTokenTextSplitter.splitCustomized(documents);

        //summary
//        List<Document> documentList = myTokenTextSplitter.enrichDocumentBySummary(documentList);

        // loading: writer into vector store
//        vectorStore.write(documentList);
//    }
}
