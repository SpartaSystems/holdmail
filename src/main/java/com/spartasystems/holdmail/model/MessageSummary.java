package com.spartasystems.holdmail.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spartasystems.holdmail.rest.mime.MimeBodyPart;
import com.spartasystems.holdmail.rest.mime.MimeBodyParts;
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
    private MimeBodyParts mimeBodyParts;

    public MessageSummary(long messageId, String identifier, String subject, String senderEmail,
            Date receivedDate, String senderHost, int messageSize, String recipients, String messageRaw,
            MimeBodyParts mimeBodyParts) {
        this.messageId = messageId;
        this.identifier = identifier;
        this.subject = subject;
        this.senderEmail = senderEmail;
        this.receivedDate = receivedDate;
        this.senderHost = senderHost;
        this.messageSize = messageSize;
        this.recipients = recipients;
        this.messageRaw = messageRaw;
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
