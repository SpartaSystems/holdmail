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
import org.springframework.data.domain.Pageable;
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
    private OutgoingMailSender outgoingMailSender;

    @Autowired
    private MessageListMapper messageListMapper;

    public Message saveMessage(Message message) {

        MessageEntity entity = messageMapper.fromDomain(message);

        entity = messageRepository.save(entity);

        return messageMapper.toDomain(entity);
    }

    public void deleteMessage(Long id) {
        messageRepository.delete(id);
    }

    public Message getMessage(long messageId) {

        MessageEntity entity = messageRepository.findOne(messageId);

        return messageMapper.toDomain(entity);
    }

    public MessageList findMessages(@Null @Email String recipientEmail, Pageable pageRequest) {

        List<MessageEntity> entities;

        if (StringUtils.isBlank(recipientEmail)) {
            entities = messageRepository.findAllByOrderByReceivedDateDesc(pageRequest);
        }
        else {
            entities = messageRepository.findAllForRecipientOrderByReceivedDateDesc(recipientEmail, pageRequest);
        }

        return messageListMapper.toMessageList(entities);
    }

    public void forwardMessage(long messageId, @NotBlank @Email String recipientEmail) {

        Message message = getMessage(messageId);
        outgoingMailSender.redirectMessage(recipientEmail, message.getRawMessage());
    }

}
