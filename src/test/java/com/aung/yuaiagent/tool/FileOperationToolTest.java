package com.aung.yuaiagent.tool;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileOperationToolTest {

    @Test
    void writeFile() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        String fileName = "hello.txt";
        String content = "厦门今天（2025 年 7 月 4 日）的天气为晴。最低温度 27℃，最高温度 34℃，空气质量优，pm2.5 指数 21，湿度 61，南风 3 级。\n"+

                            "小时天气情况如下：\n" +

                            "14：00，晴，32℃，南风 2 级，湿度 56。\n"+
                            "15：00，晴，33℃，东南风 3 级，湿度 58。\n"+
                            "16：00，晴，32℃，东南风 4 级，湿度 62。\n"+
                            "17：00，晴，32℃，东南风 4 级，湿度 65。\n"+
                            "18：00，晴，31℃，东风 4 级，湿度 69。\n"+
"此外，厦门市气象台今日 10 时 21 分继续发布高温橙色预警信号，受副热带高压影响，预计今日白天湖里区、思明区最高气温可达 34~36℃，局部超过 36℃；集美区、海沧区、同安区、翔安区最高气温可达 35~37℃，局部超过 38℃，高温时段出现在 11~16 时。";
        String result = fileOperationTool.writeFile(fileName, content);
        Assertions.assertNotNull(result);
    }

    @Test
    void readFile() {

        FileOperationTool fileOperationTool = new FileOperationTool();
        String fileName = "hello.txt";
        String result = fileOperationTool.readFile(fileName);
        System.out.println(result);
        Assertions.assertNotNull(result);
    }
}