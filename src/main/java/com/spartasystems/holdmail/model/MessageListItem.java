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

package com.spartasystems.holdmail.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class MessageListItem {

    private long messageId;
    private long receivedDate;
    private String senderEmail;
    private String recipients;
    private String subject;
    private boolean hasAttachments;

    public MessageListItem(long messageId, long receivedDate, String senderEmail, String recipients, String subject, boolean hasAttachments) {
        this.messageId = messageId;
        this.receivedDate = receivedDate;
        this.senderEmail = senderEmail;
        this.recipients = recipients;
        this.subject = subject;
        this.hasAttachments = hasAttachments;
    }

    public long getMessageId() {
        return messageId;
    }

    public long getReceivedDate() {
        return receivedDate;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getRecipients() {
        return recipients;
    }

    public String getSubject() {
        return subject;
    }

    public boolean getHasAttachments() {
        return hasAttachments;
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
