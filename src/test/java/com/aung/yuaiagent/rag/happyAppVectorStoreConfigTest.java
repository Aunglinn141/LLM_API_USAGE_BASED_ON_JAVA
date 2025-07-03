package com.aung.yuaiagent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class happyAppVectorStoreConfigTest {

    @Resource
    VectorStore happyAppVectorStore;

    @Test
    void HappyAppVectorStore(){
        VectorStore a = happyAppVectorStore;
        Assertions.assertNotNull(a);
    }
}