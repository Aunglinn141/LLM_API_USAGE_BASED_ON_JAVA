package com.aung.yuaiagent.tool;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WebSearchTool {

    private static final String SEARCH_API_URL = "https://www.searchapi.io/api/v1/search";

    private final String apiKey;

    public WebSearchTool(String apiKey) {
        this.apiKey = apiKey;
    }

    @Tool(name = "webSearchTool", description = "Search for web form baidu Search Engine.")
    public String searchWeb(@ToolParam(description = "Search query keyword.") String query) {
        Map<String, Object> params = Map.of(
                "q", query,
                "api_key", apiKey,
                "engine", "google" );

        try{
            String result = HttpUtil.get(SEARCH_API_URL, params);

            // get the first 5 results
            JSONObject jsonObject = JSONUtil.parseObj(result);

            //get the organic results
            JSONArray organicResults = jsonObject.getJSONArray("organic_results");
            List<Object> object = organicResults.subList(0, 5);

            //assemble the result with String
            object.stream().map(obj->{{
                JSONObject tmpJSONObject = (JSONObject) obj;
                return tmpJSONObject.toString();
            }}).collect(Collectors.joining(","));
            return result;
        }catch (Exception e){
            return "Error: " + e.getMessage();
        }
    }
}
