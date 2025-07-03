package com.aung.yuaiagent.demo.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.rag.Query;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.List;


@SpringBootTest
class MultiQueryExpanderDemoTest {

    @Resource
    private MultiQueryExpanderDemo multiQueryExpanderDemo;

    @Test
    void expand(){
        List<Query> queries = multiQueryExpanderDemo.expand("你好，我是代码校园！");
        Assertions.assertNotNull(queries);
    }
}