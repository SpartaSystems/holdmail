package com.spartasystems.holdmail.service;

import com.spartasystems.holdmail.domain.Message;
import com.spartasystems.holdmail.mapper.MessageListMapper;
import com.spartasystems.holdmail.mapper.MessageMapper;
import com.spartasystems.holdmail.model.MessageList;
import com.spartasystems.holdmail.persistence.MessageEntity;
import com.spartasystems.holdmail.persistence.MessageRepository;
import com.spartasystems.holdmail.smtp.OutgoingMailSender;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Null;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private OutgoingMailSender outgoingMailSender;

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



    public MessageList findMessageListBySubject(@NotBlank String subject) {
        return messageListMapper.toMessageList(messageRepository.findBySubject(subject,new PageRequest(0,150)));
    }

    @Transactional
    public List<Message> findDomainMessages(@Null @Email String recipientEmail) {

        List<MessageEntity> entities;

        if(StringUtils.isBlank(recipientEmail)) {
            entities = messageRepository.findAllByOrderByReceivedDateDesc();
        }
        else {
            entities = messageRepository.findAllForRecipientOrderByReceivedDateDesc(recipientEmail);
        }

        return entities.stream().map(messageMapper::toDomain).collect(Collectors.toList());

    }

    public void deleteMessagesForRecepient(@NotBlank @Email String recipientEmail ) {
        List<MessageEntity> entities = messageRepository.findAllForRecipientOrderByReceivedDateDesc(recipientEmail);

        messageRepository.delete(entities);
    }

    public void forwardMessage(long messageId, @NotBlank @Email String recipientEmail) {

        Message message = getMessage(messageId);
        outgoingMailSender.sendEmail(recipientEmail, message.getRawMessage());
    }

}
