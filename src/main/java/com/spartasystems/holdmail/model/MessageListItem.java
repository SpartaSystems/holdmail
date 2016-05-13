package com.spartasystems.holdmail.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class MessageListItem {

    private long messageId;
    private long receivedDate;
    private String senderEmail;
    private String recipients;
    private String subject;

    public MessageListItem(long messageId, long receivedDate, String senderEmail, String recipients, String subject) {
        this.messageId = messageId;
        this.receivedDate = receivedDate;
        this.senderEmail = senderEmail;
        this.recipients = recipients;
        this.subject = subject;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
