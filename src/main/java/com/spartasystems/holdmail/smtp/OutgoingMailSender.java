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

    @Value("${holdmail.outgoing.mail.parse:holdmail@localhost.localdomain}")
    private String senderFrom;

    public String getOutgoingServer() {
        return outgoingServer;
    }

    public int getOutgoingPort() {
        return outgoingPort;
    }

    public String getSenderFrom() {
        return senderFrom;
    }

    public void redirectMessage(String recipient, String rawBody) {

        // TODO: this is a crude first pass at bouncing a mail and probably needs to be a little more sophisticated

        try {

            Session session = getMailSession();
            Message message = initializeMimeMessage(rawBody, session);

            // wipe out ALL exisitng recipients
            message.setRecipients(Message.RecipientType.TO, new Address[]{});
            message.setRecipients(Message.RecipientType.CC, new Address[]{});
            message.setRecipients(Message.RecipientType.BCC, new Address[]{});

            // and set the new recipient
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));

            InternetAddress[] parsedFrom = InternetAddress.parse(getSenderFrom());
            if(parsedFrom.length > 0) {
                message.setFrom(parsedFrom[0]);
                logger.info("Outgoing mail will have From: " + parsedFrom[0].getAddress());
            }

            sendMessage(message);

            logger.info("Outgoing mail forwarded to " + recipient);

        } catch (MessagingException e) {
            throw new HoldMailException("couldn't send mail: " + e.getMessage(), e);
        }

    }

    protected void sendMessage(Message message) throws MessagingException {
        Transport.send(message);
    }

    protected Message initializeMimeMessage(String rawBody, Session session) throws MessagingException {
        return new MimeMessage(session, IOUtils.toInputStream(rawBody, StandardCharsets.UTF_8));
    }

    protected Session getMailSession() {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "false");
        props.put("mail.smtp.starttls.enable", "false");
        props.put("mail.smtp.host", getOutgoingServer());
        props.put("mail.smtp.port", getOutgoingPort());

        return Session.getInstance(props);
    }
}
