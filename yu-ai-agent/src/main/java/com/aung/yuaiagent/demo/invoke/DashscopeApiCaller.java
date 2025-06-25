package com.aung.yuaiagent.demo.invoke;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

public class DashscopeApiCaller {
    public static void main(String[] args) {
        String apiKey = TestApiKey.API_KEY; // 替换为你的实际API密钥
        String url = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";
        
        String jsonBody = "{\n" +
                "    \"model\": \"qwen-plus\", \n" +
                "    \"messages\": [\n" +
                "        {\n" +
                "            \"role\": \"system\",\n" +
                "            \"content\": \"You are a helpful assistant.\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"role\": \"user\", \n" +
                "            \"content\": \"你是谁？\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        
        HttpResponse response = HttpRequest.post(url)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(jsonBody)
                .execute();
        
        System.out.println(response.body());
    }
}