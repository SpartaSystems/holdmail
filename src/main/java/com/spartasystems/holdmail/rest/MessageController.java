package com.spartasystems.holdmail.rest;

import com.spartasystems.holdmail.domain.Message;
import com.spartasystems.holdmail.mapper.MessageSummaryMapper;
import com.spartasystems.holdmail.model.MessageList;
import com.spartasystems.holdmail.model.MessageListItem;
import com.spartasystems.holdmail.model.MessageSummary;
import com.spartasystems.holdmail.service.MessageService;
import org.hibernate.validator.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value="/rest/messages", produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageSummaryMapper messageSummaryMapper;

    @RequestMapping(method = GET)
    public MessageList getMessages(@RequestParam(name="recipient", required = false) @Email String recipientEmail) {

        MessageList messageList = messageService.findMessages(recipientEmail);

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
    public ResponseEntity getMessageContent(@PathVariable("messageId") long messageId) throws Exception {

        Message message = messageService.getMessage(messageId);
        MessageSummary summary = messageSummaryMapper.toMessageSummary(message);
        return ResponseEntity.ok().body(summary);
    }

    @RequestMapping(value = "/{messageId}/html", method = GET)
    public ResponseEntity getMessageContentHTML(@PathVariable("messageId") long messageId) throws Exception {

        Message message = messageService.getMessage(messageId);
        MessageSummary summary = messageSummaryMapper.toMessageSummary(message);

        return ResponseEntity.ok()
                             .contentType(MediaType.TEXT_HTML)
                             .body(summary.getMessageBodyHTML());
    }
}
