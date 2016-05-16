package com.spartasystems.holdmail.mapper;

import com.spartasystems.holdmail.domain.Message;
import com.spartasystems.holdmail.persistence.MessageEntity;
import com.spartasystems.holdmail.persistence.MessageHeaderEntity;
import com.spartasystems.holdmail.persistence.MessageRecipientEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class MessageMapper {

    public MessageEntity fromDomain(Message message) {

        MessageEntity entity = new MessageEntity();
        entity.setIdentifier(message.getIdentifier());
        entity.setSubject(message.getSubject());
        entity.setSenderEmail(message.getSenderEmail());
        entity.setReceivedDate(message.getReceivedDate());
        entity.setSenderHost(message.getSenderHost());
        entity.setMessageSize(message.getMessageSize());
        entity.setMessageBody(message.getRawMessage());


        message.getHeaders().forEach((name, vals) -> vals.forEach(val -> {
            MessageHeaderEntity header = new MessageHeaderEntity(name, val);
            entity.getHeaders().add(header);
        }));

        entity.setRecipients(message.getRecipients()
                                    .stream()
                                    .map(MessageRecipientEntity::new)
                                    .collect(Collectors.toSet()));


        return entity;

    }


    public Message toDomain(MessageEntity entity) {

        Message model = new Message();
        model.setMessageId(entity.getMessageId());
        model.setIdentifier(entity.getIdentifier());
        model.setSubject(entity.getSubject());
        model.setSenderEmail(entity.getSenderEmail());
        model.setReceivedDate(entity.getReceivedDate());
        model.setSenderHost(entity.getSenderHost());
        model.setMessageSize(entity.getMessageSize());
        model.getRawMessageBody(entity.getMessageBody());

        entity.getHeaders().forEach(h -> {
            String headerName = h.getHeaderName();
            if(!model.getHeaders().containsKey(headerName)){
                model.getHeaders().put(headerName, new ArrayList<>());
            }
            model.getHeaders().get(headerName).add(h.getHeaderValue());
        });

        model.setRecipients(entity.getRecipients()
                                  .stream()
                                  .map(MessageRecipientEntity::getRecipientEmail)
                                  .collect(Collectors.toList()));

        return model;
    }




}
