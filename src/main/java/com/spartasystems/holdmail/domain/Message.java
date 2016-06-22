package com.spartasystems.holdmail.domain;

import com.spartasystems.holdmail.mime.MimeHeaders;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Message {

    private long   messageId;
    private String identifier;
    private String subject;
    private String senderEmail;
    private Date   receivedDate;
    private String senderHost;
    private int    messageSize;
    private String rawMessage;
    private List<String> recipients = new ArrayList<>();
    private MimeHeaders  headers    = new MimeHeaders();

    public Message() {
        this(null);
    }

    public Message(String identifier) {
        setIdentifier(identifier);
    }

    public Message(long messageId, String identifier, String subject, String senderEmail, Date receivedDate,
                   String senderHost, int messageSize, String rawMessage, List<String> recipients,
                   MimeHeaders headers) {
        this.messageId = messageId;
        this.identifier = identifier;
        this.subject = subject;
        this.senderEmail = senderEmail;
        this.receivedDate = receivedDate;
        this.senderHost = senderHost;
        this.messageSize = messageSize;
        this.rawMessage = rawMessage;
        this.recipients = recipients;
        this.headers = headers;
    }

    public void setIdentifier(String identifier) {
        if (StringUtils.isBlank(identifier)) {
            this.identifier = UUID.randomUUID().toString();
        } else {
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

    public MimeHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(MimeHeaders headers) {
        this.headers = headers;
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
