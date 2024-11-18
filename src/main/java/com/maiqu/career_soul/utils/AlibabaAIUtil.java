package com.maiqu.career_soul.utils;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.maiqu.career_soul.javabean.pojo.Messages;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AlibabaAIUtil {

    //阿里云 API 密钥
    private static final String API_KEY = "";

    /**
     * 获取 AI 响应并更新消息列表
     *
     * @param messages 消息列表
     * @param model 模型名称
     * @return AI 的响应内容
     * @throws ApiException          API 调用异常
     * @throws NoApiKeyException     没有 API 密钥异常
     * @throws InputRequiredException 输入必需异常
     */
    public static GenerationResult getQwenResponse(List<Message> messages, String model, Boolean enableSearch) throws ApiException, NoApiKeyException, InputRequiredException {
        Generation gen = new Generation();
        GenerationParam param = GenerationParam.builder()
                .apiKey(API_KEY)
                .model(model)
                .messages(messages)
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .enableSearch(enableSearch)
                .build();

        return gen.call(param);
    }

    /**
     * 合并用户消息和AI消息，用户消息作为第一条，AI消息作为第二条，轮流插入
     *
     * @param messagesList 数据库消息列表
     * @return 模型需求的对话消息列表
     */
    public static List<Message> makeQwenMessages(List<Messages> messagesList, String systemPrompt) {
        List<Message> chatMessages = new ArrayList<>();
        chatMessages.add(Message.builder().role(Role.SYSTEM.getValue()).content(systemPrompt).build());

        if (messagesList.isEmpty()) {
            return chatMessages;
        }

        for (int i = 0; i < messagesList.size(); i++) {
            Messages messages = messagesList.get(i);
            chatMessages.add(Message.builder().role(Role.USER.getValue()).content(messages.getUserMessage()).build());
            chatMessages.add(Message.builder().role(Role.ASSISTANT.getValue()).content(messages.getAiMessage()).build());
        }

        return chatMessages;
    }
}
