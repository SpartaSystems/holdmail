package com.spartasystems.holdmail.smtp;

import com.spartasystems.holdmail.domain.Message;
import com.spartasystems.holdmail.service.MessageService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.subethamail.smtp.MessageContext;
import org.subethamail.smtp.MessageHandler;
import org.subethamail.smtp.RejectException;

import javax.mail.Header;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Component
public class SMTPHandler implements MessageHandler {

    private Logger logger = LoggerFactory.getLogger(SMTPHandler.class);

    private Message message;

    @Autowired
    private MessageService messageService;

    private byte[] data;

    public SMTPHandler() {
    }

    public SMTPHandler(MessageContext ctx) {

        InetSocketAddress senderHost = (InetSocketAddress) ctx.getRemoteAddress();

        message = new Message();
        message.setSenderHost(senderHost.getAddress().getHostAddress());
        message.setReceivedDate(new Date());

    }

    public void from(String from) throws RejectException {
        message.setSenderEmail(from);
    }

    public void recipient(String recipient) throws RejectException {
        logger.error("ADDING RECIPIENT: " + recipient);
        message.getRecipients().add(recipient);
    }

    public void data(InputStream is) throws IOException {

        data = IOUtils.toByteArray(is);
        message.setMessageSize(data.length);
        message.getRawMessageBody(IOUtils.toString(data, CharEncoding.UTF_8));
    }

    public void done() {

        try {

            Session s = Session.getDefaultInstance(new Properties());
            MimeMessage mimeMsg = new MimeMessage(s, new ByteArrayInputStream(data));

            // set any data from the mimemessage itself
            Map<String, List<String>> headers = getHeaders(mimeMsg);

            message.setIdentifier(mimeMsg.getMessageID());
            message.setSubject(headers.get("Subject").get(0));
            message.setHeaders(headers);

            messageService.saveMessage(message);

            logger.info("Stored mail from " + message.getSenderEmail()
                    + " to " + StringUtils.join(message.getRecipients(), ","));


        }
        catch (Exception e) {

            logger.error("Couldn't handle message: " + e.getMessage(), e);

        }

    }

    private Map<String, List<String>> getHeaders(MimeMessage message) throws MessagingException {

        Map<String, List<String>> result = new HashMap<>();

        // oh wow 2015 and it's untyped and uses Enumeration
        Enumeration allHeaders = message.getAllHeaders();
        while (allHeaders.hasMoreElements()) {
            Header header = (Header) allHeaders.nextElement();
            String headerName = header.getName();
            String headerVal = header.getValue();

            if(!result.containsKey(headerName)){
                result.put(headerName, new ArrayList<>());
            }

            result.get(headerName).add(headerVal);

        }

        return result;
    }

}
