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

package com.spartasystems.holdmail.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class MessageSummary {

    private final long                    messageId;
    private final String                  identifier;
    private final String                  subject;
    private final String                  senderEmail;
    private final Date                    receivedDate;
    private final String                  senderHost;
    private final int                     messageSize;
    private final String                  recipients;
    private final String                  messageRaw;
    private final Map<String, String>     messageHeaders;
    private final String                  messageBodyText;
    private final String                  messageBodyHTML;
    private final List<MessageAttachment> attachments;

    public MessageSummary(long messageId, String identifier, String subject, String senderEmail,
            Date receivedDate, String senderHost, int messageSize, String recipients, String messageRaw,
            Map<String, String> messageHeaders, String messageBodyText, String messageBodyHTML,
            List<MessageAttachment> attachments) {
        this.messageId = messageId;
        this.identifier = identifier;
        this.subject = subject;
        this.senderEmail = senderEmail;
        this.receivedDate = receivedDate;
        this.senderHost = senderHost;
        this.messageSize = messageSize;
        this.recipients = recipients;
        this.messageRaw = messageRaw;
        this.messageHeaders = messageHeaders;
        this.messageBodyText = messageBodyText;
        this.messageBodyHTML = messageBodyHTML;
        this.attachments = attachments;
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

    public String getRecipients() {
        return recipients;
    }

    public String getMessageRaw() {
        return messageRaw;
    }

    public Map<String, String> getMessageHeaders() {
        return messageHeaders;
    }

    public List<MessageAttachment> getAttachments() {
        return attachments;
    }

    public String getMessageBodyText() {
        return messageBodyText;
    }

    public String getMessageBodyHTML() {
        return messageBodyHTML;
    }

    public boolean getMessageHasBodyHTML() {
        return StringUtils.isNotBlank(messageBodyHTML);
    }

    public boolean getMessageHasBodyText() {
        return StringUtils.isNotBlank(messageBodyText);
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
