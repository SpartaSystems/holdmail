package com.spartasystems.holdmail;

import com.spartasystems.holdmail.domain.Message;
import com.spartasystems.holdmail.service.MessageService;
import com.spartasystems.holdmail.mime.MailClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MailIntegrationTest extends BaseIntegrationTest{

    private MailClient mailClient;

    @Value("${holdmail.smtp.port:25000}")
    private int smtpServerPort;

    @Autowired
    private MessageService messageService;

    private  static final String TO_EMAIL = "other@example.org";

    @Before
    public void setUp() throws Exception {
        super.setUp();
        mailClient = new MailClient(smtpServerPort,"localhost");
    }

    @After
    public void tearDown() throws Exception {
        messageService.deleteMessagesForRecepient(TO_EMAIL);

    }

    @Test
    public void shouldSendEmailToSmtpServer() throws Exception {
        String subject = "Test Email";
        String fromEmail = "someone@example.org";
        String textBody = "Test email body";
        mailClient.sendTextEmail(fromEmail, TO_EMAIL, subject, textBody);

        List<Message> messages = messageService.findDomainMessages(TO_EMAIL);
        assertThat(messages).hasSize(1);

        Message message = messages.get(0);

        assertThat(message.getSubject()).isEqualTo(subject);
        assertThat(message.getSenderEmail()).isEqualTo(fromEmail);
        assertThat(message.getRecipients()).hasSize(1).contains(TO_EMAIL);
        assertThat(message.getRawMessage()).contains(textBody);


    }

}
