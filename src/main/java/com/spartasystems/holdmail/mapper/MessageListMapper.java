package com.spartasystems.holdmail.mapper;

import com.spartasystems.holdmail.model.MessageList;
import com.spartasystems.holdmail.model.MessageListItem;
import com.spartasystems.holdmail.persistence.MessageEntity;
import com.spartasystems.holdmail.persistence.MessageRecipientEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MessageListMapper {


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

}
