package com.aung.yuimageseachmcp;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class YuImageSeachMcpApplication {

    public static void main(String[] args) {
        SpringApplication.run(YuImageSeachMcpApplication.class, args);
    }

    @Bean
    public ToolCallbackProvider ImageSearchTools(ImageSearchTool imageSearchTool){
        return MethodToolCallbackProvider.builder()
                .toolObjects(imageSearchTool)
                .build();
    }

}
