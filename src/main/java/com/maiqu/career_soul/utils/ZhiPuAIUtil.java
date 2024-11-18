package com.maiqu.career_soul.utils;

import com.maiqu.career_soul.javabean.pojo.Messages;
import com.maiqu.career_soul.prompt.ChatSystemPrompt;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ZhiPuAIUtil {
    private static final String API_KEY = "";
    private static final ClientV4 client = new ClientV4.Builder(API_KEY)
            .build();

    // 请自定义自己的业务id
    private static final String requestIdTemplate = "CareerSoulChatAI";

    public static String getGLMEmohaaResponse(List<ChatMessage> messages, String userName, String userInfo) {
        ChatMeta chatMeta = new ChatMeta();
        chatMeta.setBot_name("小智");
        chatMeta.setBot_info(ChatSystemPrompt.Chat_System_Prompt);
        chatMeta.setUser_name(userName);
        chatMeta.setUser_info(userInfo);

        String requestId = String.format(requestIdTemplate, System.currentTimeMillis());
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model("emohaa")
                .meta(chatMeta)
                .stream(Boolean.FALSE)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .requestId(requestId)
                .build();

        ModelApiResponse invokeModelApiResp = client.invokeModelApi(chatCompletionRequest);

        String glmResponse;
        try {
            glmResponse = invokeModelApiResp.getData().getChoices().get(0).getMessage().getContent().toString();
            return glmResponse;
        } catch (Exception e) {
            return null;
        }
    }

    public static List<ChatMessage> makeGLMMessages(List<Messages> messagesList, String systemPrompt) {
        List<ChatMessage> chatMessages = new ArrayList<>();

        if (systemPrompt != null && !systemPrompt.isEmpty()){
            chatMessages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), systemPrompt));
        }

        if (messagesList == null || messagesList.isEmpty()) {
            return chatMessages;
        }

        for (int i = 0; i < messagesList.size(); i++) {
            Messages messages = messagesList.get(i);
            chatMessages.add(new ChatMessage(ChatMessageRole.USER.value(), messages.getUserMessage()));
            chatMessages.add(new ChatMessage(ChatMessageRole.ASSISTANT.value(), messages.getAiMessage()));
        }

        return chatMessages;
    }

}
