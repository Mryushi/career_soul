package com.maiqu.career_soul.mapper;

import com.maiqu.career_soul.javabean.pojo.Conversations;
import com.maiqu.career_soul.javabean.pojo.Messages;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChatMapper {

    @Select("select message_id, conversation_id, user_message, ai_message, created_time from messages " +
            "where conversation_id = #{conversationId} " +
            "order by created_time")
    List<Messages> getMessagesByConversationId(Long conversationId);

    @Options(useGeneratedKeys = true, keyProperty = "conversationId")
    @Insert("insert into conversations(user_id, agent_name, created_time, updated_time)" +
            "values(#{userId}, #{agentName}, #{createdTime}, #{updatedTime})")
    Integer addConversation(Conversations conversations);

    @Insert("insert into messages(conversation_id, user_message, ai_message, created_time)" +
            "values(#{conversationId}, #{userMessage}, #{aiMessage}, #{createdTime})")
    Integer addMessage(Messages messages);
}
