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

package com.spartasystems.holdmail.domain;

import com.spartasystems.holdmail.mime.MimeUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Message {

    private final long           messageId;
    private final String         identifier;
    private final String         subject;
    private final String         senderEmail;
    private final Date           receivedDate;
    private final String         senderHost;
    private final int            messageSize;
    private final String         rawMessage;
    private final List<String>   recipients;
    private final MessageHeaders headers;



    public Message(long messageId, String identifier, String subject,
            String senderEmail, Date receivedDate, String senderHost,
            int messageSize, String rawMessage, List<String> recipients,
            MessageHeaders headers) {

        this.messageId = messageId;
        this.identifier = StringUtils.isNotBlank(identifier) ? identifier : UUID.randomUUID().toString();
        this.subject = subject;
        this.senderEmail = senderEmail;
        this.receivedDate = receivedDate;
        this.senderHost = senderHost;
        this.messageSize = messageSize;
        this.rawMessage = rawMessage;
        this.recipients = recipients;
        this.headers = headers;
    }

    public long getMessageId() {
        return messageId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getSubject() {
        return subject;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public String getSenderHost() {
        return senderHost;
    }

    public int getMessageSize() {
        return messageSize;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public MessageHeaders getHeaders() {
        return headers;
    }

    public MessageContent getContent() {
        return MimeUtils.parseMessageContent(IOUtils.toInputStream(getRawMessage(), StandardCharsets.UTF_8));
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
