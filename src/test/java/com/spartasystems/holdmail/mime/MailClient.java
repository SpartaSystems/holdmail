package com.spartasystems.holdmail.mime;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public final class MailClient {

    private static final Logger logger = LoggerFactory
            .getLogger(MailClient.class);


    private final int port;
    private final String smtpHost;
    private final Session session;

    public MailClient(int port, String smtpHost) {

        this.port = port;
        this.smtpHost = smtpHost;

        Properties props = new Properties();

        props.put("mail.smtp.auth", "false");
        //Put below to false, if no https is needed
        props.put("mail.smtp.starttls.enable", "false");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", port);

        session = Session.getInstance(props);
    }


    public void sendEmail(String fromEmail, String toEmail, String subject, String textBody, String htmlBody) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toEmail));
//            message.addRecipient(Message.RecipientType.BCC, new InternetAddress("some@example.org"));
            message.setSubject(subject);


            // Set the message
            createMultiMimePart(message, textBody, htmlBody);

            Transport.send(message);
        } catch (MessagingException e) {
            logger.error("Failed to send email", e);
        }
    }

    public void sendTextEmail(String fromEmail, String toEmail, String subject, String textBody) {
        sendEmail(fromEmail, toEmail, subject, textBody, null);
    }

    public void sendHtmlEmail(String fromEmail, String toEmail, String subject, String htmlBody) {
        sendEmail(fromEmail, toEmail, subject, null, htmlBody);
    }

    private void createMultiMimePart(Message message, String textBody, String htmlBody) throws MessagingException {
        Multipart mp = new MimeMultipart();

        if (StringUtils.isNotBlank(textBody)) {
            mp.addBodyPart(createTextBodyPart(textBody));
        }
        if (StringUtils.isNotBlank(htmlBody)) {
            mp.addBodyPart(createHtmlBodyPart(htmlBody));
        }

        message.setContent(mp);
    }

    private BodyPart createHtmlBodyPart(String htmlMessageBody) throws MessagingException {
        BodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(htmlMessageBody, "text/html");
        return htmlPart;

    }

    private BodyPart createTextBodyPart(String messageBody) throws MessagingException {
        BodyPart textPart = new MimeBodyPart();
        textPart.setText(messageBody);
        return textPart;
    }

}
