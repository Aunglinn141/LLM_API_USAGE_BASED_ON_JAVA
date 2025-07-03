package com.aung.yuaiagent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.rag.Query;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QueryRewriterTest {

    @Resource
    private QueryRewriter queryRewriter;

    @Test
    void doQueryRewriteTest(){
        String queries = queryRewriter.doQueryRewrite("你好，代码校园是谁呢？");
        System.out.println(queries);
        Assertions.assertNotNull(queries);
    }
}