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

import com.spartasystems.holdmail.domain.Message;
import com.spartasystems.holdmail.domain.MessageHeaders;
import com.spartasystems.holdmail.persistence.MessageEntity;
import com.spartasystems.holdmail.persistence.MessageHeaderEntity;
import com.spartasystems.holdmail.persistence.MessageRecipientEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MessageMapper {

    public MessageEntity fromDomain(Message message) {

        MessageEntity entity = new MessageEntity();
        entity.setMessageId(message.getMessageId());
        entity.setIdentifier(message.getIdentifier());
        entity.setSubject(message.getSubject());
        entity.setSenderEmail(message.getSenderEmail());
        entity.setReceivedDate(message.getReceivedDate());
        entity.setSenderHost(message.getSenderHost());
        entity.setMessageSize(message.getMessageSize());
        entity.setRawMessage(message.getRawMessage());
        message.getHeaders().asMap().forEach((k, v)
                -> entity.getHeaders().add(new MessageHeaderEntity(k, v)));

        entity.setRecipients(message.getRecipients()
                                    .stream()
                                    .map(MessageRecipientEntity::new)
                                    .collect(Collectors.toSet()));
        return entity;

    }

    public Message toDomain(MessageEntity entity) {

        MessageHeaders headers = new MessageHeaders();
        entity.getHeaders().forEach(h -> headers.put(h.getHeaderName(), h.getHeaderValue()));

        List<String> recipients = entity.getRecipients()
                                        .stream()
                                        .map(MessageRecipientEntity::getRecipientEmail)
                                        .collect(Collectors.toList());

        return new Message(entity.getMessageId(),
                entity.getIdentifier(),
                entity.getSubject(),
                entity.getSenderEmail(),
                entity.getReceivedDate(),
                entity.getSenderHost(),
                entity.getMessageSize(),
                entity.getRawMessage(),
                recipients,
                headers
        );

    }

}
