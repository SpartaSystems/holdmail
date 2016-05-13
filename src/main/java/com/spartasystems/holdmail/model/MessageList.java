package com.spartasystems.holdmail.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class MessageList {

    private List<MessageListItem> messages = new ArrayList<>();

    public MessageList(List<MessageListItem> messages) {
        this.messages = messages;
    }

    public List<MessageListItem> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageListItem> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
