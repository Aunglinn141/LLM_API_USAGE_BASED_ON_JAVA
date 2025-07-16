package com.aung.yuaiagent.controller;

import com.aung.yuaiagent.agent.YuManus;
import com.aung.yuaiagent.app.HappyApp;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/ai")
public class AiController {

    @Resource
    private HappyApp happyApp;

    @Resource
    private ToolCallback[] allTools;

    @Resource
    private ChatModel dashScopeChatModel;

    @GetMapping("/happy_app/chat")
    public String doChatWithHappyAppSync (String message, String chatID) {
        return happyApp.chat(message, chatID);
    }


    @GetMapping(value = "/happy_app/chat/sse" , produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithHappyAppSSe (String message, String chatID) {
        return happyApp.doChatByStream(message, chatID);
    }

    @GetMapping(value = "/happy_app/chat/sse2")
    public Flux<ServerSentEvent<String>> doChatWithHappyAppSSE2 (
            @RequestParam("message") String message,
            @RequestParam("chatId" ) String chatID) {
        return happyApp.doChatByStream(message, chatID)
                .map(chunk-> ServerSentEvent.<String>builder()
                        .data(chunk).build());
    }

    @GetMapping(value = "/happy_app/chat/sse/emitter")
    public SseEmitter doChatWithHappyAppSseEmitter (String message, String chatID) {
        SseEmitter sseEmitter = new SseEmitter(180000L);
        happyApp.doChatByStream(message, chatID)
                .subscribe(chunk -> {
                    try {
                        sseEmitter.send(chunk);
                    } catch (Exception e) {
                        sseEmitter.completeWithError(e);
                    }
                }, sseEmitter::completeWithError, sseEmitter::complete);
        return sseEmitter;
    }

    @GetMapping("/manus/chat")
    public SseEmitter doChatWithManus (String message) {
        YuManus yuManus = new YuManus(allTools, dashScopeChatModel);
        return yuManus.runStream(message);
    }

}
