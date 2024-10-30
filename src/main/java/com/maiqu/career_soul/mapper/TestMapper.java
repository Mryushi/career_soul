package com.maiqu.career_soul.mapper;

import com.maiqu.career_soul.pojo.Conversations;
import com.maiqu.career_soul.pojo.Messages;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TestMapper {

    @Select("select message_id, conversation_id, user_message, ai_message from messages where conversation_id = #{conversationId}")
    List<Messages> getMessagesByConversationId(Integer conversationId);


    @Options(useGeneratedKeys = true, keyProperty = "conversationId")
    @Insert("insert into conversations(user_id, agent_id, create_time, update_time)" +
            "values(#{userId}, #{agentId}, #{createTime}, #{updateTime})")
    Integer addConversation(Conversations conversations);

    @Insert("insert into messages(conversation_id, user_message, ai_message)" +
            "values(#{conversationId}, #{userMessage}, #{aiMessage})")
    Integer addMessage(Messages messages);
}
