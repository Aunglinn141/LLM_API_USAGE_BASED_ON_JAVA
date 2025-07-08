package com.aung.yuaiagent.tool;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import com.aung.yuaiagent.constant.FileConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

public class DownloadResource {

    /**
     * Download resource from URL.
     * @param url
     * @param fileName
     * @return
     */
    @Tool(description = "Download resource from URL.")
    public String downloadResource(@ToolParam(description = "Url of the resource to download.") String url,
                                   @ToolParam(description = "Name of the file to save the download resource.") String fileName) {
        // Create directory if not exis
        String fileDir = FileConstant.FILE_SAVE_DIR + "/download/";

        String filePath = fileDir + fileName;
        try {
            // Create directory if not exist
            FileUtil.mkdir(fileDir);
            // Downloaded resource save to file
            HttpUtil.downloadFile(url, filePath);
            return "Resource download success to : " + filePath;
        }catch (Exception e){
            return "Resource download failed: " + e.getMessage();
        }
    }
}
