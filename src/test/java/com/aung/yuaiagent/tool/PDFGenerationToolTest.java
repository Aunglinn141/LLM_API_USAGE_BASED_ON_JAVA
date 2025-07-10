package com.aung.yuaiagent.tool;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PDFGenerationToolTest {
    @Test
    void generatePDF() {
        PDFGenerationTool pdfGenerationTool = new PDFGenerationTool();
        String content  = "华侨大学是中央统战部（国务院侨务办公室）直属高校。学校是国家华文教育基地，国家大学生文化素质教育基地，国家语言文字推广基地，国家语言服务领域特色服务出口基地，全国深化创新创业教育改革示范高校，全国高校教师考核评价改革示范校，全国高校中华优秀传统文化传承基地。";
        String result = pdfGenerationTool.generatePDF("Huaqiaouniversity.pdf",content);
        System.out.println(result);
        assertNotNull(result);
    }

}