package com.aung.yuaiagent.tool;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolRegister {

    // Search API Key
    @Value("${search-api.api-key}")
    private String searchApiKey;

    @Bean
    // Register all tools here
    public ToolCallback[] allTools() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        WebSearchTool webSearchTool = new WebSearchTool(searchApiKey);
        WebScrapingTool webScrapingTool = new WebScrapingTool();
        DownloadResource downloadResourceTool = new DownloadResource();
        TerminalOperationTool terminalOperationTool = new TerminalOperationTool();
        PDFGenerationTool pdfGenerationTool = new PDFGenerationTool();
        // Add more tools here
        return ToolCallbacks.from(
                fileOperationTool,
                webSearchTool,
                webScrapingTool,
                downloadResourceTool,
                terminalOperationTool,
                pdfGenerationTool
        );
    }
}
