/*******************************************************************************
 * Copyright 2016 - 2017 Sparta Systems, Inc
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
import com.spartasystems.holdmail.domain.MessageContent;
import com.spartasystems.holdmail.model.MessageAttachment;
import com.spartasystems.holdmail.model.MessageSummary;
import com.spartasystems.holdmail.rest.HTMLPreprocessor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class MessageSummaryMapper {

    @Value("${holdmail.message.summary.enableraw:false}")
    private boolean enableRaw;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Autowired
    private HTMLPreprocessor htmlPreprocessor;

    // this support disappears in v2 (https://github.com/SpartaSystems/holdmail/issues/14)
    boolean getEnableRaw() {
        return enableRaw;
    }

    public MessageSummary toMessageSummary(Message message) {

        MessageContent messageContent = message.getContent();

        List<MessageAttachment> attachments = messageContent.findAllAttachments()
                                                            .stream()
                                                            .map(attachmentMapper::fromMessageContentPart)
                                                            .collect(toList());

        return new MessageSummary(
                message.getMessageId(),
                message.getIdentifier(),
                message.getSubject(),
                message.getSenderEmail(),
                message.getReceivedDate(),
                message.getSenderHost(),
                message.getMessageSize(),
                StringUtils.join(message.getRecipients(), ","),
                getEnableRaw() ? message.getRawMessage() : null,
                message.getHeaders().asMap(),
                messageContent.findFirstTextPart(),
                htmlPreprocessor.preprocess(message.getMessageId(), messageContent.findFirstHTMLPart()),
                attachments);

    }
}
