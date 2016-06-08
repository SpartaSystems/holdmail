package com.spartasystems.holdmail.domain;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Message {

    private long         messageId;
    private String       identifier;
    private String       subject;
    private String       senderEmail;
    private Date         receivedDate;
    private String       senderHost;
    private int          messageSize;
    private String       rawMessage;
    private List<String>              recipients = new ArrayList<>();
    private Map<String, List<String>> headers    = new HashMap<>();

    public Message() {
        this(null);
    }

    public Message(String identifier) {
        setIdentifier(identifier);
    }

    public void setIdentifier(String identifier) {
        if(StringUtils.isBlank(identifier)){
            this.identifier = UUID.randomUUID().toString();
        }
        else {
            this.identifier = identifier;
        }
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getIdentifier() {
        return identifier;
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

    public String getSenderHost() {
        return senderHost;
    }

    public void setSenderHost(String senderHost) {
        this.senderHost = senderHost;
    }

    public int getMessageSize() {
        return messageSize;
    }

    public void setMessageSize(int messageSize) {
        this.messageSize = messageSize;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    public void setRawMessageBody(String rawMessageBody) {
        this.rawMessage = rawMessageBody;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", identifier='" + identifier + '\'' +
                ", subject='" + subject + '\'' +
                ", senderEmail='" + senderEmail + '\'' +
                ", receivedDate=" + receivedDate +
                ", senderHost='" + senderHost + '\'' +
                ", messageSize=" + messageSize +
                ", rawMessage='" + rawMessage + '\'' +
                ", recipients=" + recipients +
                ", headers=" + headers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        return new EqualsBuilder()
                .append(messageId, message.messageId)
                .append(messageSize, message.messageSize)
                .append(identifier, message.identifier)
                .append(subject, message.subject)
                .append(senderEmail, message.senderEmail)
                .append(receivedDate, message.receivedDate)
                .append(senderHost, message.senderHost)
                .append(rawMessage, message.rawMessage)
                .append(recipients, message.recipients)
                .append(headers, message.headers)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(messageId)
                .append(identifier)
                .append(subject)
                .append(senderEmail)
                .append(receivedDate)
                .append(senderHost)
                .append(messageSize)
                .append(rawMessage)
                .append(recipients)
                .append(headers)
                .toHashCode();
    }
}
