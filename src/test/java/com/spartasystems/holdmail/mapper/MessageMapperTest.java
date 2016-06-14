package com.spartasystems.holdmail.mapper;

import com.google.common.collect.ImmutableMap;
import com.spartasystems.holdmail.domain.Message;
import com.spartasystems.holdmail.persistence.MessageEntity;
import com.spartasystems.holdmail.persistence.MessageHeaderEntity;
import com.spartasystems.holdmail.persistence.MessageRecipientEntity;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;

public class MessageMapperTest {

    private static final long messageId = 1L;
    private static final String IDENTIFIER = "IDENTIFIER";
    private static final String subject = "Message Subject";
    private static final String senderEmail = "email@example.org";
    private static final Date receivedDate = new Date();
    private static final String senderHost = "mail.example.org";
    private static final int messageSize = 12345;
    private static final String rawMessage = "RAW MESSAGE";
    private static final String recipient1 = "person1@example.org";
    private static final String recipient2 = "person2@example.com";
    private static final List<String> recipients = newArrayList(recipient1, recipient2);
    private static final String headerValue1 = "header-1-value";
    private static final String headerValue2 = "header-1-value2";
    private static final String header1 = "header-1";
    private static final Map<String, List<String>> headers = ImmutableMap.of(header1, newArrayList(headerValue1, headerValue2));
    private static final MessageMapper messageMapper = new MessageMapper();

    @Test
    public void shouldMapFromDomain() throws Exception {

        MessageEntity actualMessageEntity = messageMapper.fromDomain(getDomainMessage());

        MessageEntity expectedMessageEntity = getMessageEntity();

        assertThat(expectedMessageEntity.getMessageId()).isEqualTo(messageId);
        assertThat(expectedMessageEntity.getIdentifier()).isEqualTo(IDENTIFIER);
        assertThat(expectedMessageEntity.getSubject()).isEqualTo(subject);
        assertThat(expectedMessageEntity.getSenderEmail()).isEqualTo(senderEmail);
        assertThat(expectedMessageEntity.getReceivedDate()).isEqualTo(receivedDate);
        assertThat(expectedMessageEntity.getSenderHost()).isEqualTo(senderHost);
        assertThat(expectedMessageEntity.getMessageSize()).isEqualTo(messageSize);
        assertThat(expectedMessageEntity.getMessageBody()).isEqualTo(rawMessage);
        assertThat(expectedMessageEntity.getRecipients()).hasSize(2)
                .contains(new MessageRecipientEntity(recipient1), new MessageRecipientEntity(recipient2))
                .doesNotHaveDuplicates();
        assertThat(expectedMessageEntity.getHeaders()).hasSize(2)
                .contains(new MessageHeaderEntity(header1,headerValue1),new MessageHeaderEntity(header1,headerValue2))
                .doesNotHaveDuplicates();
    }

    @Test
    public void shouldMapToDomain() throws Exception {
        MessageEntity messageEntity = getMessageEntity();

        Message message = messageMapper.toDomain(messageEntity);

        assertThat(message.getMessageId()).isEqualTo(messageId);
        assertThat(message.getIdentifier()).isEqualTo(IDENTIFIER);
        assertThat(message.getSubject()).isEqualTo(subject);
        assertThat(message.getSenderEmail()).isEqualTo(senderEmail);
        assertThat(message.getReceivedDate()).isEqualTo(receivedDate);
        assertThat(message.getSenderHost()).isEqualTo(senderHost);
        assertThat(message.getMessageSize()).isEqualTo(messageSize);
        assertThat(message.getRawMessage()).isEqualTo(rawMessage);
        assertThat(message.getRecipients()).hasSize(2).contains(recipient1,recipient2).doesNotHaveDuplicates();
        assertThat(message.getHeaders()).hasSize(1).containsKeys(header1);
        assertThat(message.getHeaders().get(header1)).hasSize(2).contains(headerValue1,headerValue2);
    }

    private Message getDomainMessage() {
        return new Message(messageId, IDENTIFIER, subject, senderEmail, receivedDate, senderHost, messageSize, rawMessage, recipients, headers);
    }

    private MessageEntity getMessageEntity() {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setMessageId(messageId);
        messageEntity.setIdentifier(IDENTIFIER);
        messageEntity.setSubject(subject);
        messageEntity.setSenderEmail(senderEmail);
        messageEntity.setReceivedDate(receivedDate);
        messageEntity.setSenderHost(senderHost);
        messageEntity.setMessageSize(messageSize);
        messageEntity.setMessageBody(rawMessage);
        messageEntity.setRecipients(recipients.stream().map(r -> new MessageRecipientEntity(r)).collect(Collectors.toSet()));
        headers.forEach((k,v)-> v.forEach(h -> {
            MessageHeaderEntity header = new MessageHeaderEntity(k, h);
            messageEntity.getHeaders().add(header);
        }));
        return messageEntity;
    }

}