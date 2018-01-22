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

package com.spartasystems.holdmail.persistence;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

@Entity()
@Table(name = "message")
public class MessageEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;

    @NotNull
    @Size(max = 255)
    @Column(name = "identifier")
    private String identifier;

    @NotNull
    @Size(max = 255)
    @Column(name = "subject")
    private String subject;

    @NotNull
    @Size(max = 255)
    @Column(name = "sender_email")
    private String senderEmail;

    @NotNull
    @Column(name = "received_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receivedDate;

    @Size(max = 255)
    @Column(name = "sender_host")
    private String senderHost;

    @Column(name = "has_attachments")
    private boolean hasAttachments;

    @NotNull
    @Column(name = "message_size")
    private int messageSize;

    @NotNull
    @Column(name = "message_body")
    private String rawMessage;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "message_id", nullable = false)
    private Set<MessageHeaderEntity> headers = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "message_id", nullable = false)
    private Set<MessageRecipientEntity> recipients = new HashSet<>();

    @SuppressWarnings("unused")
    public MessageEntity() {
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public boolean getHasAttachments() {
        return hasAttachments;
    }

    public void setHasAttachments(boolean hasAttachments) {
        this.hasAttachments = hasAttachments;
    }

    public int getMessageSize() {
        return messageSize;
    }

    public void setMessageSize(int messageSize) {
        this.messageSize = messageSize;
    }

    public String getSenderHost() {
        return senderHost;
    }

    public void setSenderHost(String senderHost) {
        this.senderHost = senderHost;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    public void setRawMessage(String rawMessage) {
        this.rawMessage = rawMessage;
    }

    public Set<MessageHeaderEntity> getHeaders() {
        return headers;
    }

    public void setHeaders(Set<MessageHeaderEntity> headers) {
        this.headers = headers;
    }

    public Set<MessageRecipientEntity> getRecipients() {
        return recipients;
    }

    public void setRecipients(Set<MessageRecipientEntity> recipients) {
        this.recipients = recipients;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, SHORT_PREFIX_STYLE);
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
