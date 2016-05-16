package com.spartasystems.holdmail.rest;

import com.spartasystems.holdmail.model.Message;
import com.spartasystems.holdmail.model.MessageList;
import com.spartasystems.holdmail.model.MessageListItem;
import com.spartasystems.holdmail.rest.mime.MimeBodyParser;
import com.spartasystems.holdmail.rest.mime.MimeBodyPart;
import com.spartasystems.holdmail.rest.mime.MimeBodyParts;
import com.spartasystems.holdmail.service.MessageService;
import org.apache.commons.io.IOUtils;
import org.hibernate.validator.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value="/rest/messages", produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MimeBodyParser mimeBodyParser;

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
    public Message getMessage(@PathVariable long messageId) {

        return messageService.getMessage(messageId);

    }

    @RequestMapping(value = "/{messageId}/content", method = GET)
    public ResponseEntity getMessageContent(@PathVariable("messageId") long messageId) throws Exception {

        String messageBody = messageService.getMessage(messageId).getMessageBody();

        MimeBodyParts allBodyParts = mimeBodyParser
                .findAllBodyParts(IOUtils.toInputStream(messageBody, StandardCharsets.UTF_8));

        Optional<MimeBodyPart> htmlBody = allBodyParts.findFirstHTMLBody();
        if(htmlBody.isPresent()){
            return htmlBody.get().toResponseEntity();
        }

        Optional<MimeBodyPart> textBody = allBodyParts.findFirstTextBody();
        if(textBody.isPresent()){
            return textBody.get().toResponseEntity();
        }
        //        return messageMimeConverter.convertContentType(new ByteArrayInputStream(messageBody.getBytes()), null, mode);

        return ResponseEntity.ok()
                             .header("Content-type: text/plain")
                             .body("no html or text found to serve. I have: \r\n" + allBodyParts);


    }
}
