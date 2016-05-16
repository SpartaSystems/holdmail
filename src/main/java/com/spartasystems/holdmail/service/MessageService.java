package com.spartasystems.holdmail.service;

import com.spartasystems.holdmail.domain.Message;
import com.spartasystems.holdmail.mapper.MessageListMapper;
import com.spartasystems.holdmail.mapper.MessageMapper;
import com.spartasystems.holdmail.model.MessageList;
import com.spartasystems.holdmail.persistence.MessageEntity;
import com.spartasystems.holdmail.persistence.MessageRepository;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Null;
import java.util.List;

@Component
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private MessageListMapper messageListMapper;

    public Message saveMessage(Message message) {

        MessageEntity entity = messageMapper.fromDomain(message);

        entity = messageRepository.save(entity);

        return messageMapper.toDomain(entity);
    }

    public Message getMessage(long messageId) {

        MessageEntity entity = messageRepository.findOne(messageId);

        return messageMapper.toDomain(entity);
    }

    public MessageList findMessages(@Null @Email String recipientEmail) {

        List<MessageEntity> entities;

        if(StringUtils.isBlank(recipientEmail)) {
            entities = messageRepository.findAllByOrderByReceivedDateDesc();
        }
        else {
            entities = messageRepository.findAllForRecipientOrderByReceivedDateDesc(recipientEmail);
        }

        return messageListMapper.toMessageList(entities);

    }

}
