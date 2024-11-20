package com.maiqu.career_soul.utils;

import com.maiqu.career_soul.javabean.pojo.Messages;
import com.maiqu.career_soul.prompt.ChatSystemPrompt;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ZhiPuAI工具类，用于与ZhiPuAI模型进行交互
 */
@Component
public class ZhiPuAIUtil {

    // API密钥，用于访问ZhiPuAI服务
    private static final String API_KEY = Objects.requireNonNullElse(System.getenv("GLM_API_KEY"), null);
    // 客户端实例，用于调用ZhiPuAI服务
    private static final ClientV4 client = new ClientV4.Builder(API_KEY)
            .build();

    // 请自定义自己的业务id
    private static final String requestIdTemplate = "CareerSoulChatAI";


    /**
     * 获取GLM模型的响应
     *
     * @param messages 消息列表，包含用户和AI的对话历史
     * @param userName 用户名
     * @param userInfo 用户信息
     * @return GLM模型的响应内容，如果发生异常则返回null
     */
    public static String getGLMEmohaaResponse(List<ChatMessage> messages, String userName, String userInfo) {
        // 构建聊天元数据，包括机器人名称、系统提示、用户名和用户信息
        ChatMeta chatMeta = new ChatMeta();
        chatMeta.setBot_name("小智");
        chatMeta.setBot_info(ChatSystemPrompt.Chat_System_Prompt);
//        chatMeta.setBot_info("拥有专业的心理咨询话术能力");
        chatMeta.setUser_name(userName);
        chatMeta.setUser_info(userInfo);

        // 构建请求ID，使用模板和当前时间戳
        String requestId = String.format(requestIdTemplate, System.currentTimeMillis());
        // 构建聊天完成请求对象
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model("emohaa")
                .meta(chatMeta)
                .stream(Boolean.FALSE)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .requestId(requestId)
                .build();

        // 调用模型API并获取响应
        ModelApiResponse invokeModelApiResp = client.invokeModelApi(chatCompletionRequest);

        // 从响应中提取并返回GLM模型的内容
        String glmResponse;
        try {
            glmResponse = invokeModelApiResp.getData().getChoices().get(0).getMessage().getContent().toString();
            return glmResponse;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 构建GLM模型的消息列表
     *
     * @param messagesList 用户和AI对话历史的消息列表
     * @param systemPrompt 系统提示，用于指导模型的行为
     * @return 构建的消息列表，用于调用GLM模型
     */
    public static List<ChatMessage> makeGLMMessages(List<Messages> messagesList, String systemPrompt) {
        List<ChatMessage> chatMessages = new ArrayList<>();

        // 如果系统提示不为空，则添加到消息列表中
        if (systemPrompt != null && !systemPrompt.isEmpty()) {
            chatMessages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), systemPrompt));
        }

        // 如果消息列表为空，则直接返回当前构建的消息列表
        if (messagesList == null || messagesList.isEmpty()) {
            return chatMessages;
        }

        // 遍历消息列表，构建用户和AI的消息对象，并添加到消息列表中
        for (int i = 0; i < messagesList.size(); i++) {
            Messages messages = messagesList.get(i);
            chatMessages.add(new ChatMessage(ChatMessageRole.USER.value(), messages.getUserMessage()));
            chatMessages.add(new ChatMessage(ChatMessageRole.ASSISTANT.value(), messages.getAiMessage()));
        }

        return chatMessages;
    }

}