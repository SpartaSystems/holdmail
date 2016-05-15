package com.spartasystems.holdmail.persistence;

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
    private long messageId;

    @NotNull
    @Size(max = 100)
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

    @NotNull
    @Column(name = "message_size")
    private int messageSize;

    @NotNull
    @Column(name = "message_body")
    private String messageBody;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "message_id", nullable = false)
    private Set<MessageHeaderEntity> headers = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "message_id", nullable = false)
    private Set<MessageRecipientEntity> recipients = new HashSet<>();

    public MessageEntity() {
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

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
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
}
