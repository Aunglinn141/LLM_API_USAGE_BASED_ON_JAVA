package com.aung.yuaiagent.tool;

import cn.hutool.core.io.FileUtil;
import com.aung.yuaiagent.constant.FileConstant;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

public class PDFGenerationTool {

    @Tool(description = "Generate a PDF file with given content.")
    public String generatePDF(@ToolParam(description = "Name of the file to save the generated pdf.") String filename,
                              @ToolParam(description = "Content to be included in the pdf.") String content) {
        String fileDir = FileConstant.FILE_SAVE_DIR + "/pdf";
        String filepath = fileDir + "/" + filename ;
        try{
            FileUtil.mkdir(fileDir);

            // Create PDF file
            try (PdfWriter writer = new PdfWriter(filepath);
                 PdfDocument pdf = new PdfDocument(writer);
                 Document document = new Document(pdf)) {

                // setting chinese font
                PdfFont font = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H");
                document.setFont(font);

                Paragraph paragraph = new Paragraph(content);
                document.add(paragraph);
            }
            return "PDF generation success: " + filepath;

        }catch (Exception e){
            return "PDF generation failed: " + e.getMessage();

        }
    }
}
