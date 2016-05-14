package com.spartasystems.holdmail.rest;

import com.spartasystems.holdmail.model.Message;
import com.spartasystems.holdmail.model.MessageList;
import com.spartasystems.holdmail.model.MessageListItem;
import com.spartasystems.holdmail.rest.mime.MessageMimeConverter;
import com.spartasystems.holdmail.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value="/rest/messages", produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageMimeConverter messageMimeConverter;

    @RequestMapping(method = GET)
    public MessageList listMessages() {

        MessageList messageList = messageService.loadAll();

        // TODO: pagination needed, limit for now
        List<MessageListItem> messages = messageList.getMessages();
        if(messages.size() > 100) {
            messageList.setMessages(messages.subList(0, 100));
            messageList.getMessages().add(new MessageListItem(0, new Date().getTime(),
                    "system", "system", "hold-mail return max 100 mails (for now)"));
        }

        return messageList;
    }


    @RequestMapping(value = "/{messageId}", method = GET)
    public Message getMessage(@PathVariable long messageId) {

        return messageService.getMessage(messageId);

    }

    @RequestMapping(value = "/{messageId}/content", method = GET)
    public ResponseEntity getMessageContent(@PathVariable("messageId") long messageId,
                                        @RequestParam(value = "mode", defaultValue = "TEXT") String mode) throws Exception {

        String messageBody = messageService.getMessage(messageId).getMessageBody();

        return messageMimeConverter.convertContentType(new ByteArrayInputStream(messageBody.getBytes()), null, mode);

    }
}
