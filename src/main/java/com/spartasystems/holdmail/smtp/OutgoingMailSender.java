package com.spartasystems.holdmail.smtp;

import com.spartasystems.holdmail.exception.HoldMailException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Component
public class OutgoingMailSender {

    private Logger logger = LoggerFactory.getLogger(OutgoingMailSender.class);

    @Value("${holdmail.outgoing.smtp.server:localhost}")
    private String outgoingServer;

    @Value("${holdmail.outgoing.smtp.port:25000}")
    private int outgoingPort;

    @Value("${holdmail.outgoing.mail.from:holdmail@localhost.localdomain}")
    private String senderFrom;

    public void sendEmail(String recipient, String rawBody) {

        // WARNING HERE BE DRAGONS
        // TODO: this is a temporary hacky solution that just bounces back to self, rather than the REAL server
        // as I figure out the appropriate method to perform SMTP bounces

        try {

            Properties props = new Properties();
            props.put("mail.smtp.auth", "false");
            props.put("mail.smtp.starttls.enable", "false");
            props.put("mail.smtp.host", outgoingServer);
            props.put("mail.smtp.port", outgoingPort);

            Session session = Session.getInstance(props);

            // read back in the original
            Message message = new MimeMessage(session, IOUtils.toInputStream(rawBody, StandardCharsets.UTF_8));

            // wipe out ALL exisitng recipients
            message.setRecipients(Message.RecipientType.TO, new Address[]{});
            message.setRecipients(Message.RecipientType.CC, new Address[]{});
            message.setRecipients(Message.RecipientType.BCC, new Address[]{});

            // and set the new recipient
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));

            InternetAddress[] parsedFrom = InternetAddress.parse(senderFrom);
            if(parsedFrom.length > 0) {
                message.setFrom(parsedFrom[0]);
                logger.info("Outgoing mail will have From: " + parsedFrom[0].getAddress());
            }


            Transport.send(message);

            logger.info("Outgoing mail forwarded to " + recipient);

        } catch (MessagingException e) {
            throw new HoldMailException("couldn't send mail: " + e.getMessage(), e);
        }

    }
}
