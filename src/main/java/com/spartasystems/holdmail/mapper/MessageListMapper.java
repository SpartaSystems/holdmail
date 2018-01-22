/*******************************************************************************
 * Copyright 2016 - 2018 Sparta Systems, Inc
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

import com.spartasystems.holdmail.model.MessageList;
import com.spartasystems.holdmail.model.MessageListItem;
import com.spartasystems.holdmail.persistence.MessageEntity;
import com.spartasystems.holdmail.persistence.MessageRecipientEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class MessageListMapper {


    public MessageList toMessageList(List<MessageEntity> messageEntityList) {
        return toMessageList(messageEntityList.stream());
    }

    public MessageList toMessageList(Stream<MessageEntity> messageEntityStream) {
        return new MessageList(messageEntityStream
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
                entity.getSubject(),
                entity.getHasAttachments());
    }



}
