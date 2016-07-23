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
import com.spartasystems.holdmail.mime.MimeBodyParser;
import com.spartasystems.holdmail.mime.MimeBodyParts;
import com.spartasystems.holdmail.model.MessageSummary;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.james.mime4j.MimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class MessageSummaryMapper {

    @Autowired
    private MimeBodyParser mimeBodyParser;

    public MessageSummary toMessageSummary(Message message) throws MimeException, IOException {

        String rawContent = message.getRawMessage();

        MimeBodyParts allBodyParts = mimeBodyParser.findAllBodyParts(
                IOUtils.toInputStream(rawContent, StandardCharsets.UTF_8));

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
                message.getHeaders(),
                allBodyParts);

    }

}
