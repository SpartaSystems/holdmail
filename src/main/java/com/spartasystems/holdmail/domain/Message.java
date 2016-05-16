package com.spartasystems.holdmail.domain;

import org.apache.commons.lang3.StringUtils;

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
    private       String senderEmail;
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

    public void getRawMessageBody(String rawMessageBody) {
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
}
