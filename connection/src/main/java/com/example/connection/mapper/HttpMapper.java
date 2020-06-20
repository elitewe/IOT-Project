package com.example.connection.mapper;

import com.example.connection.bean.DevMessage;
import com.example.connection.bean.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HttpMapper {
    int insertContent(Integer clientId, String payload, String protocol, String url, String messageId);

    List<DevMessage> getAllMessage();

    List<Message> getMessagesById(int id);
}
