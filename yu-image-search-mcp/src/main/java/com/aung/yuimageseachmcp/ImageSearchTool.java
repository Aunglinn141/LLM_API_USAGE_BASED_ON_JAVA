package com.aung.yuimageseachmcp;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.micrometer.common.util.StringUtils;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ImageSearchTool {

    // pexels API_KEY;
//    @Value("${pexels.api-key")
    private static final String API_KEY = "dUU9UYNEfukJAnEtEq1u5TEqLaCa8DyKvPIFpysWbHxbgDqHIO4P52ho";

    private static final String API_URL = "https://api.pexels.com/v1/search";

    @Tool(description = "search image from web")
    public String searchImage(@ToolParam(description = "search query keyword.") String query) {
        try{
            return String.join(",", searchMediumImage(query));
        }catch (Exception e){
            return "Error search image" + e.getMessage();
        }

    }

    //
    private List<String> searchMediumImage(String query) {

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", API_KEY);

        // add request parameters
        Map<String, Object> params = new HashMap<>();
        params.put("query", query);

        // send request
        String response = HttpUtil.createGet(API_URL)
                .addHeaders(headers)
                .form(params)
                .execute()
                .body();
        return JSONUtil.parseObj(response)
                .getJSONArray("photos")
                .stream()
                .map(photoObj -> (JSONObject) photoObj)
                .map(photoObj -> photoObj.getJSONObject("src"))
                .map(photo -> photo.getStr("medium"))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
    }
}
