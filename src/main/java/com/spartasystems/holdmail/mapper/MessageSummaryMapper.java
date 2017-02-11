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
import com.spartasystems.holdmail.model.MessageSummary;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class MessageSummaryMapper {

    public MessageSummary toMessageSummary(Message message) {

        MessageContent messageContent = message.getContent();

        return new MessageSummary(
                message.getMessageId(),
                message.getIdentifier(),
                message.getSubject(),
                message.getSenderEmail(),
                message.getReceivedDate(),
                message.getSenderHost(),
                message.getMessageSize(),
                StringUtils.join(message.getRecipients(), ","),
                message.getRawMessage(),
                message.getHeaders().asMap(),
                messageContent.findFirstTextPart(),
                messageContent.findFirstHTMLPart());

    }

}
