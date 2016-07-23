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

package com.spartasystems.holdmail.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spartasystems.holdmail.mime.MimeBodyPart;
import com.spartasystems.holdmail.mime.MimeBodyParts;
import com.spartasystems.holdmail.mime.MimeHeaders;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

public class MessageSummary {

    private long   messageId;
    private String identifier;
    private String subject;
    private String senderEmail;
    private Date   receivedDate;
    private String senderHost;
    private int    messageSize;
    private String recipients;
    private String messageRaw;
    private MimeHeaders messageHeaders = new MimeHeaders();
    private MimeBodyParts mimeBodyParts;

    public MessageSummary(long messageId, String identifier, String subject, String senderEmail,
            Date receivedDate, String senderHost, int messageSize, String recipients, String messageRaw,
            MimeHeaders messageHeaders, MimeBodyParts mimeBodyParts) {
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
        this.mimeBodyParts = mimeBodyParts;
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

    public MimeHeaders getMessageHeaders() {
        return messageHeaders;
    }

    public String getMessageRaw() {
        return messageRaw;
    }

    public String getMessageBodyHTML() {
        MimeBodyPart mime = mimeBodyParts.findFirstHTMLBody();
        return mime == null ? null : mime.getContentString();
    }

    public String getMessageBodyText() {
        MimeBodyPart mime = mimeBodyParts.findFirstTextBody();
        return mime == null ? null : mime.getContentString();
    }

    @JsonIgnore
    public MimeBodyPart getMessageContentById(String contentId) {
        return mimeBodyParts.findByContentId(contentId);
    }

    public boolean getMessageHasBodyHTML() {
        return getMessageBodyHTML() != null;
    }

    public boolean getMessageHasBodyText() {
        return getMessageBodyText() != null;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
