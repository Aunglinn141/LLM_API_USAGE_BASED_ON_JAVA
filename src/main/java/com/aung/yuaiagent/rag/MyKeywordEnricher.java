package com.aung.yuaiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentWriter;
import org.springframework.ai.transformer.KeywordMetadataEnricher;
import org.springframework.ai.transformer.SummaryMetadataEnricher;
import org.springframework.stereotype.Component;

import java.util.List;
/**
 * Customized ChatModel Keyword Enricher
 */
@Component
public class MyKeywordEnricher {


    @Resource
    private ChatModel dashscopeChatModel;


    /**
     * Keyword Enricher Function With Document
     * @param documents
     * @return
     */
    public List<Document> enrichDocuments(List<Document> documents) {
        KeywordMetadataEnricher keywordMetadataEnricher = new KeywordMetadataEnricher(dashscopeChatModel,5);
        return keywordMetadataEnricher.apply(documents);
    }

    /**
     * Summary Enricher Function With Document
     * @param documents
     * @return
     */
    public List<Document> enrichDocumentBySummary(List<Document> documents) {
        SummaryMetadataEnricher summaryMetadataEnricher = new SummaryMetadataEnricher(dashscopeChatModel,
                List.of(SummaryMetadataEnricher.SummaryType.PREVIOUS,
                        SummaryMetadataEnricher.SummaryType.CURRENT,
                        SummaryMetadataEnricher.SummaryType.NEXT));
        return summaryMetadataEnricher.apply(documents);
    }
}
