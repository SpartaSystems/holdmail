package com.spartasystems.holdmail.smtp;

import com.spartasystems.holdmail.exception.HoldMailException;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Component
public class OutgoingMailSender {

    @Value("${holdmail.outgoing.smtp.server:localhost}")
    private String outgoingServer;

    @Value("${holdmail.outgoing.smtp.port:25000}")
    private int outgoingPort;

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

            Transport.send(message);

        } catch (MessagingException e) {
            throw new HoldMailException("couldn't send mail: " + e.getMessage(), e);
        }

    }
}
