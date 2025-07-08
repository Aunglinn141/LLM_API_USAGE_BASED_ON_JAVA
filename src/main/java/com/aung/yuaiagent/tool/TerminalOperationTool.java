package com.aung.yuaiagent.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TerminalOperationTool {

    @Tool(description = "Execute a terminal command.")
    public String executeTerminalCommand(@ToolParam(description = "Command to execute in the terminal.") String command) {
        StringBuffer output =  new StringBuffer();
        try {
            ProcessBuilder builder =  new ProcessBuilder("cmd.exe","/c" , command);
            Process process = builder.start();
            try(BufferedReader stdoutreader = new BufferedReader(new InputStreamReader(process.getInputStream(),"GBK"))){
                String line = "";
                while ((line = stdoutreader.readLine())!= null){
                    output.append(line).append("\n");
                }
            }
            try (BufferedReader stderrreader = new BufferedReader(new InputStreamReader(process.getErrorStream(),"GBK"))){
                String line = "";
                while ((line = stderrreader.readLine())!= null){
                    output.append("Commend execution failed with exit code : ").append(line).append("\n");
                }
            }
//            int exitCode = process.waitFor();
//            if (exitCode != 0) {
//                output.append("Command execution failed with exit code ").append(exitCode);
//            }
        }catch (Exception e){
            output.append("Error executing terminal command.").append(e.getMessage());
        }
        return output.toString();
    }
}
