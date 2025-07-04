package com.aung.yuaiagent.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;

@Slf4j
public class HappyAppContextualQueryAugumenterFactory {

    public static ContextualQueryAugmenter createInstance(){

        PromptTemplate promptTemplate =  new PromptTemplate("""
                                                        你应该输出下面的内容：
                                                        抱歉，我能回答情感相关的问题，别的没办法帮你哦。
                                                        如有其他需要，可以联系 https://doubao.com 
                                                        """);
        return ContextualQueryAugmenter.builder()
                .allowEmptyContext(true)
                .emptyContextPromptTemplate(promptTemplate)
                .build();
    }

}
