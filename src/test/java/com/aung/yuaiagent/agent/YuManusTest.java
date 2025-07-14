package com.aung.yuaiagent.agent;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest
class YuManusTest {

    @Resource
    private YuManus yuManus;

    @Test
    void test() {
        String userPrompt = """
                我现在在厦门华侨大学，请帮我找到十公里内最近的美食推荐，
                并结合一些网络图片，制定一份信息的暑期旅游攻略。
                并以pdf格式给我输出。
                """;
        String result = yuManus.run(userPrompt);
        Assertions.assertNotNull(result);
    }
}