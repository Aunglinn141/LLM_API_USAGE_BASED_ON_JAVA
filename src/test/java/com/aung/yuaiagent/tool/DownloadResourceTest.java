package com.aung.yuaiagent.tool;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DownloadResourceTest {

    @Test
    void downloadResource() {
        DownloadResource downloadResource = new DownloadResource();
        String url = "https://www.hqu.edu.cn/images/logo.png";
        String fileName = "logo.png";
        String result = downloadResource.downloadResource(url, fileName);
        System.out.println(result);
        Assertions.assertNotNull(result);
    }
}