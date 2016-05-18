package com.spartasystems.holdmail.rest;

import com.spartasystems.holdmail.domain.Message;
import com.spartasystems.holdmail.mapper.MessageSummaryMapper;
import com.spartasystems.holdmail.model.MessageList;
import com.spartasystems.holdmail.model.MessageListItem;
import com.spartasystems.holdmail.model.MessageSummary;
import com.spartasystems.holdmail.rest.mime.MimeBodyPart;
import com.spartasystems.holdmail.service.MessageService;
import org.hibernate.validator.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.http.MediaType.TEXT_PLAIN;

@RestController
@RequestMapping(value="/rest/messages", produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageSummaryMapper messageSummaryMapper;

    @RequestMapping()
    public MessageList getMessages(@RequestParam(name="recipient", required = false) @Email String recipientEmail) {

        MessageList messageList = messageService.findMessages(recipientEmail);

        // TODO: pagination needed, limit for now
        List<MessageListItem> messages = messageList.getMessages();
        if(messages.size() > 150) {
            messageList.setMessages(messages.subList(0, 150));
            messageList.getMessages().add(new MessageListItem(0, new Date().getTime(),
                    "system", "system", "hold-mail return max 150 mails (for now)"));
        }

        return messageList;
    }


    @RequestMapping(value = "/{messageId}")
    public ResponseEntity getMessageContent(@PathVariable("messageId") long messageId) throws Exception {

        MessageSummary summary = loadSummary(messageId);
        return ResponseEntity.ok().body(summary);
    }

    @RequestMapping(value = "/{messageId}/html")
    public ResponseEntity getMessageContentHTML(@PathVariable("messageId") long messageId) throws Exception {

        MessageSummary summary = loadSummary(messageId);
        return serveContent(summary.getMessageBodyHTML(), TEXT_HTML);
    }

    @RequestMapping(value = "/{messageId}/text")
    public ResponseEntity getMessageContentTEXT(@PathVariable("messageId") long messageId) throws Exception {

        MessageSummary summary = loadSummary(messageId);
        return serveContent(summary.getMessageBodyText(), TEXT_PLAIN);
    }

    @RequestMapping(value = "/{messageId}/raw")
    public ResponseEntity getMessageContentRAW(@PathVariable("messageId") long messageId) throws Exception {

        MessageSummary summary = loadSummary(messageId);
        return serveContent(summary.getMessageRaw(), TEXT_PLAIN);
    }


    @RequestMapping(value = "/{messageId}/content/{contentId}")
    public ResponseEntity getMessageContentHTML(@PathVariable("messageId") long messageId,
                                                @PathVariable("contentId") String contentId) throws Exception {

        MessageSummary summary = loadSummary(messageId);

        MimeBodyPart content = summary.getMessageContentById(contentId);

        return ResponseEntity.ok()
                             .header("Content-Type", content.getContentType())
                             .body(new InputStreamResource(content.getContentStream()));
    }

    // -------------------------- utility ------------------------------------

    private MessageSummary loadSummary(long messageId) throws Exception {
        Message message = messageService.getMessage(messageId);
        return messageSummaryMapper.toMessageSummary(message);
    }

    private ResponseEntity serveContent(Object data, MediaType mediaType) throws Exception {

        if(data == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().contentType(mediaType).body(data);
    }

}
