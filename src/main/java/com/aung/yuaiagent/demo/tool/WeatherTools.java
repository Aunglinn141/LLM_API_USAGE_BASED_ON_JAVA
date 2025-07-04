package com.aung.yuaiagent.demo.tool;

import org.springframework.ai.tool.annotation.Tool;

public class WeatherTools {

    @Tool(description = "Get the current weather condition of a specified city")
    public String getWeather(String city){
        return " 厦门今天（2025 年 7 月 4 日）的天气为晴。最低温度 27℃，最高温度 34℃，空气质量优，pm2.5 指数 21，湿度 61，南风 3 级。";
    }
}
