package com.aung.yuaiagent.rag;

import lombok.extern.slf4j.Slf4j;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class happyAppDocumentLoader {

    private final ResourcePatternResolver resourcePatternResolver;

    happyAppDocumentLoader(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }

    /**
     * Load all markdown documents
     * @return
     */
    public List<Document> loaderMarkdownDocument(){
        List<Document> allDocuments = new ArrayList<>();
        try{
            Resource[] resources = resourcePatternResolver.getResources("classpath:markdown/*.md");
            for(Resource resource : resources){
                log.info("resource:{}",resource.getFilename());
                MarkdownDocumentReaderConfig config =  MarkdownDocumentReaderConfig.builder()
                        .withHorizontalRuleCreateDocument(true)
                        .withIncludeCodeBlock(false)
                        .withIncludeBlockquote(false)
                        .withAdditionalMetadata("filename",resource.getFilename())
                        .build();
                MarkdownDocumentReader reader = new MarkdownDocumentReader(resource,config);
                allDocuments.addAll(reader.get());
            }
        } catch (Exception e) {
            log.error("Markdown loading failed." , e);
        }
        return allDocuments;
    }
}
