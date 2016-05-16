package com.spartasystems.holdmail.mapper;

import com.spartasystems.holdmail.domain.Message;
import com.spartasystems.holdmail.model.MessageSummary;
import com.spartasystems.holdmail.rest.mime.MimeBodyParser;
import com.spartasystems.holdmail.rest.mime.MimeBodyPart;
import com.spartasystems.holdmail.rest.mime.MimeBodyParts;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.james.mime4j.MimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class MessageSummaryMapper {

    @Autowired
    private MimeBodyParser mimeBodyParser;

    public MessageSummary toMessageSummary(Message message) throws MimeException, IOException {

        String rawContent = message.getRawMessage();

        MimeBodyParts allBodyParts = mimeBodyParser
                .findAllBodyParts(IOUtils.toInputStream(rawContent, StandardCharsets.UTF_8));

        String htmlContent = null;
        String textContent = null;

        MimeBodyPart htmlBody = allBodyParts.findFirstHTMLBody();
        if(htmlBody != null){
            htmlContent = htmlBody.getContentString();
        }

        MimeBodyPart textBody = allBodyParts.findFirstTextBody();
        if(textBody != null){
            textContent = textBody.getContentString();
        }

        return new MessageSummary(
                message.getMessageId(),
                message.getIdentifier(),
                message.getSubject(),
                message.getSenderEmail(),
                message.getReceivedDate(),
                message.getSenderHost(),
                message.getMessageSize(),
                StringUtils.join(message.getRecipients(), ","),
                message.getRawMessage(),
                htmlContent,
                textContent);

    }

}
