package com.spartasystems.holdmail.persistence;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

@Entity
@Table(name = "message_recipient")
public class MessageRecipientEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "message_recipient_id")
    private long messageRecipientId;

    @NotNull
    @Size(max = 255)
    @Column(name = "recipient_email")
    private String recipientEmail;

    public MessageRecipientEntity() {
    }

    public MessageRecipientEntity(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public long getMessageRecipientId() {
        return messageRecipientId;
    }

    public void setMessageRecipientId(long messageRecipientId) {
        this.messageRecipientId = messageRecipientId;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, SHORT_PREFIX_STYLE);
    }
}
