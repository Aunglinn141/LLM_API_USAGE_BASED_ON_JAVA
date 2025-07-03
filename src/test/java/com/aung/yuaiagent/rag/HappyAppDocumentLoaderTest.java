package com.aung.yuaiagent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HappyAppDocumentLoaderTest {

    @Resource
    private HappyAppDocumentLoader happyAppDocumentLoader;

    @Test
    void loaderMarkdownDocument() {
        List<Document> documents = happyAppDocumentLoader.loaderMarkdownDocument();
        assertNotNull(documents);
    }
}