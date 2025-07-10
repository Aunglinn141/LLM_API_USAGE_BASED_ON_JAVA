package com.aung.yuimageseachmcp;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ImageSearchToolTest {

    @Resource
    private ImageSearchTool imageSearchTool;

    //
    @Test
    void imageSearch(){

        String query = "A forest image";
        String response = imageSearchTool.searchImage(query);
        System.out.println(response);
        Assertions.assertNotNull(response);
    }

}