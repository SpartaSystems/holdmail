package com.spartasystems.holdmail;

import com.spartasystems.holdmail.model.Message;
import com.spartasystems.holdmail.model.MessageList;
import com.spartasystems.holdmail.model.MessageListItem;
import com.spartasystems.holdmail.persistence.MessageEntity;
import com.spartasystems.holdmail.persistence.MessageHeaderEntity;
import com.spartasystems.holdmail.persistence.MessageRecipientEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MessageMapper {

    public MessageEntity fromMessage(Message message) {

        MessageEntity entity = new MessageEntity();
        entity.setIdentifier(message.getIdentifier());
        entity.setSubject(message.getSubject());
        entity.setSenderEmail(message.getSenderEmail());
        entity.setReceivedDate(message.getReceivedDate());
        entity.setSenderHost(message.getSenderHost());
        entity.setMessageSize(message.getMessageSize());
        entity.setMessageBody(message.getMessageBody());


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

    public MessageList toMessageList(List<MessageEntity> messageEntityList) {

        return new MessageList(messageEntityList.stream()
                                                .map(this::toMessageListItem)
                                                .collect(Collectors.toList()));

    }

    protected MessageListItem toMessageListItem(MessageEntity entity) {

        List<String> recipients = entity.getRecipients()
                                        .stream()
                                        .map(MessageRecipientEntity::getRecipientEmail).collect(Collectors.toList());

        String recipientString = StringUtils.join(recipients, ", ");

        return new MessageListItem(
                entity.getMessageId(),
                entity.getReceivedDate().getTime(),
                entity.getSenderEmail(),
                recipientString,
                entity.getSubject());
    }

    public Message toMessage(MessageEntity entity) {

        Message model = new Message();
        model.setIdentifier(entity.getIdentifier());
        model.setSubject(entity.getSubject());
        model.setSenderEmail(entity.getSenderEmail());
        model.setReceivedDate(entity.getReceivedDate());
        model.setSenderHost(entity.getSenderHost());
        model.setMessageSize(entity.getMessageSize());
        model.setMessageBody(entity.getMessageBody());

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
