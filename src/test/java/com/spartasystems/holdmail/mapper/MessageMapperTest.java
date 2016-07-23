/*******************************************************************************
 * Copyright 2016 Sparta Systems, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.spartasystems.holdmail.mapper;

import com.google.common.collect.ImmutableMap;
import com.spartasystems.holdmail.domain.Message;
import com.spartasystems.holdmail.mime.MimeHeaders;
import com.spartasystems.holdmail.persistence.MessageEntity;
import com.spartasystems.holdmail.persistence.MessageHeaderEntity;
import com.spartasystems.holdmail.persistence.MessageRecipientEntity;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;

public class MessageMapperTest {

    private static final long          messageId     = 1L;
    private static final String        IDENTIFIER    = "IDENTIFIER";
    private static final String        subject       = "Message Subject";
    private static final String        senderEmail   = "email@example.org";
    private static final Date          receivedDate  = new Date();
    private static final String        senderHost    = "mail.example.org";
    private static final int           messageSize   = 12345;
    private static final String        rawMessage    = "RAW MESSAGE";
    private static final String        recipient1    = "person1@example.org";
    private static final String        recipient2    = "person2@example.com";
    private static final List<String>  recipients    = newArrayList(recipient1, recipient2);
    private static final String        header1       = "header-1";
    private static final String        header1Val    = "header-1-value";
    private static final String        header2       = "header-2";
    private static final String        header2Val    = "header-2-value";
    private static final MimeHeaders   headers       = new MimeHeaders(ImmutableMap.of(header1, header1Val,
            header2, header2Val));
    private static final MessageMapper messageMapper = new MessageMapper();

    @Test
    public void shouldMapFromDomain() throws Exception {

        Message messageDomain = new Message(messageId, IDENTIFIER, subject, senderEmail, receivedDate,
                senderHost, messageSize, rawMessage, recipients, headers);

        MessageEntity actualEntity = messageMapper.fromDomain(messageDomain);

        assertThat(actualEntity.getMessageId()).isEqualTo(messageId);
        assertThat(actualEntity.getIdentifier()).isEqualTo(IDENTIFIER);
        assertThat(actualEntity.getSubject()).isEqualTo(subject);
        assertThat(actualEntity.getSenderEmail()).isEqualTo(senderEmail);
        assertThat(actualEntity.getReceivedDate()).isEqualTo(receivedDate);
        assertThat(actualEntity.getSenderHost()).isEqualTo(senderHost);
        assertThat(actualEntity.getMessageSize()).isEqualTo(messageSize);
        assertThat(actualEntity.getMessageBody()).isEqualTo(rawMessage);
        assertThat(actualEntity.getRecipients()).hasSize(2)
                                                .contains(new MessageRecipientEntity(recipient1),
                                                        new MessageRecipientEntity(recipient2))
                                                .doesNotHaveDuplicates();
        assertThat(actualEntity.getHeaders()).hasSize(2)
                                             .contains(new MessageHeaderEntity(header1, header1Val),
                                                     new MessageHeaderEntity(header2, header2Val))
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
        assertThat(message.getRecipients()).hasSize(2).contains(recipient1, recipient2).doesNotHaveDuplicates();
        assertThat(message.getHeaders()).isEqualTo(new MimeHeaders(ImmutableMap.of(header1, header1Val,
                header2, header2Val)));
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
        messageEntity.setRecipients(recipients.stream().map(MessageRecipientEntity::new).collect(Collectors.toSet()));
        headers.asMap().forEach((k, v) -> {
            MessageHeaderEntity header = new MessageHeaderEntity(k, v);
            messageEntity.getHeaders().add(header);
        });
        return messageEntity;
    }

}
