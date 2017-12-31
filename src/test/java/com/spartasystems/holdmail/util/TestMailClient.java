/*******************************************************************************
 * Copyright 2016 Sparta Systems, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.spartasystems.holdmail.util;

import com.spartasystems.holdmail.exception.HoldMailException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Address;
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
import java.io.InputStream;
import java.util.Properties;

public final class TestMailClient {

    private static final Logger logger = LoggerFactory.getLogger(TestMailClient.class);

    private final Session session;

    public TestMailClient(int port, String smtpHost) {

        Properties props = new Properties();

        props.put("mail.smtp.auth", "false");
        props.put("mail.smtp.starttls.enable", "false");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", port);

        session = Session.getInstance(props);
    }

    public void sendEmail(String fromEmail, String toEmail, String subject, String textBody, String htmlBody) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);

            // Set the message
            createMultiMimePart(message, textBody, htmlBody);

            Transport.send(message);
        }
        catch (MessagingException e) {
            throw new HoldMailException("Failed to send email : " + e.getMessage(), e);
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

    public void sendResourceEmail(String resourceName, String sender, String recipient) {
        sendResourceEmail(resourceName, sender, recipient, null);
    }

    public void sendResourceEmail(String resourceName, String sender, String recipient, String subjectOverride) {

        try {

            InputStream resource = TestMailClient.class.getClassLoader().getResourceAsStream(resourceName);
            if (resource == null) {
                throw new MessagingException("Couldn't find resource at: " + resourceName);
            }

            Message message = new MimeMessage(session, resource);

            // wipe out ALL exisitng recipients
            message.setRecipients(Message.RecipientType.TO, new Address[] {});
            message.setRecipients(Message.RecipientType.CC, new Address[] {});
            message.setRecipients(Message.RecipientType.BCC, new Address[] {});

            // then set the new data
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setFrom(InternetAddress.parse(sender)[0]);

            if(StringUtils.isNotBlank(subjectOverride)) {
                message.setSubject(subjectOverride);
            }

            Transport.send(message);

            logger.info("Outgoing mail forwarded to " + recipient);

        }
        catch (MessagingException e) {
            throw new HoldMailException("couldn't send mail: " + e.getMessage(), e);
        }

    }

}
