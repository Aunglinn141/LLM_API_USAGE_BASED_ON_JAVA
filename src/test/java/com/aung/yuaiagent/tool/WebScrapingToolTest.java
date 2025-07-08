package com.aung.yuaiagent.tool;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebScrapingToolTest {

    @Test
    void scrapeWebPage() {
        WebScrapingTool webScrapingTool = new WebScrapingTool();
        String url = "https://epaper.xmrb.com/xmrb/pc/col/202507/07/node_A01.html";
        String result = webScrapingTool.scrapeWebPage(url);
        System.out.println(result);
        Assertions.assertNotNull(result);
    }

}