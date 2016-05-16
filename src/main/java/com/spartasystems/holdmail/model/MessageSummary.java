package com.spartasystems.holdmail.model;

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
    private String messageBodyHTML;
    private String messageBodyText;

    public MessageSummary(long messageId, String identifier, String subject, String senderEmail,
            Date receivedDate, String senderHost, int messageSize, String recipients, String messageRaw,
            String messageBodyHTML, String messageBodyText) {
        this.messageId = messageId;
        this.identifier = identifier;
        this.subject = subject;
        this.senderEmail = senderEmail;
        this.receivedDate = receivedDate;
        this.senderHost = senderHost;
        this.messageSize = messageSize;
        this.recipients = recipients;
        this.messageRaw = messageRaw;
        this.messageBodyHTML = messageBodyHTML;
        this.messageBodyText = messageBodyText;
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

    public String getMessageBodyHTML() {
        return messageBodyHTML;
    }

    public String getMessageBodyText() {
        return messageBodyText;
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
