package com.maiqu.career_soul.controller;

import com.maiqu.career_soul.javabean.Result;
import com.maiqu.career_soul.javabean.dto.ChatRequest;
import com.maiqu.career_soul.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")

@RestController
@RequestMapping("maiqu/career_soul/chat")
public class ChatController {

    private static final List<String> agents = new ArrayList<>();

    static {
        agents.add("Chat");
        agents.add("Holland");
        agents.add("IQ");
    }

    @Autowired
    private ChatService chatService;

    @PostMapping
    public Result AIChat(@RequestBody ChatRequest chatRequest) {
        Result result = new Result();

        Long id = chatRequest.getConversationsId();
        String userInput = chatRequest.getUserInput();
        String agentName = chatRequest.getAgentName();

        if (id == null || userInput == null || userInput.isEmpty()) {
            result.setCode(401);
            result.setMsg("参数不能为空");
            return result;
        }

        if (!agents.contains(agentName)) {
            result.setCode(402);
            result.setMsg("不存在该智能体");
            return result;
        }

        String response = chatService.getResponse(chatRequest);

        if (!response.equals("400")) {
            result.setCode(200);
            result.setMsg("success");
            result.setData(response);
        } else {
            result.setCode(402);
            result.setMsg("error");
            result.setData("模型异常");
        }

        return result;
    }

    @PostMapping("get_conversation_id")
    public Result addConversation(@RequestBody ChatRequest chatRequest) {
        Long conversationId = chatService.addConversation(chatRequest.getUserId(), chatRequest.getAgentName());
        return Result.success(conversationId);
    }

}
