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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import java.util.Properties;

import static javax.mail.Message.RecipientType.BCC;
import static javax.mail.Message.RecipientType.CC;
import static javax.mail.Message.RecipientType.TO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.powermock.api.support.membermodification.MemberMatcher.methodsDeclaredIn;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Transport.class)
public class OutgoingMailSenderTest {

    public static final String OUTGOING_SERVER = "myServer";
    public static final int    OUTGOING_PORT   = 456;
    public static final String MAIL_BODY       = "blah blah blah";
    public static final String RECIPIENT       = "recipient@host.tld";
    public static final String SENDER_FROM     = "sender@host.com";

    @Spy
    private OutgoingMailSender mailSenderSpy = new OutgoingMailSender();

    @Before
    public void setUp() throws Exception {
        suppress(methodsDeclaredIn(Transport.class));
    }

    @Test
    public void shouldRedirectMessage() throws Exception {

        Session session = Session.getInstance(new Properties());
        doReturn(session).when(mailSenderSpy).getMailSession();

        Message messageMock = mock(Message.class);
        doReturn(messageMock).when(mailSenderSpy).initializeMimeMessage(MAIL_BODY, session);
        doReturn(SENDER_FROM).when(mailSenderSpy).getSenderFrom();

        mailSenderSpy.redirectMessage(RECIPIENT, MAIL_BODY);

        InOrder inOrder = inOrder(messageMock, mailSenderSpy);
        inOrder.verify(messageMock).setRecipients(TO, new Address[] {});
        inOrder.verify(messageMock).setRecipients(CC, new Address[] {});
        inOrder.verify(messageMock).setRecipients(BCC, new Address[] {});
        inOrder.verify(messageMock).setRecipients(TO, InternetAddress.parse(RECIPIENT));
        inOrder.verify(messageMock).setFrom(InternetAddress.parse(SENDER_FROM)[0]);

        inOrder.verify(mailSenderSpy).sendMessage(messageMock);

    }

    @Test
    public void shouldHandleExceptionDuringRedirectMessage() throws Exception {

        doReturn(Session.getInstance(new Properties())).when(mailSenderSpy).getMailSession();
        doReturn(mock(Message.class)).when(mailSenderSpy).initializeMimeMessage(anyString(), any());
        doReturn(SENDER_FROM).when(mailSenderSpy).getSenderFrom();

        MessagingException expectedCause = new MessagingException("BOOM");
        doThrow(expectedCause).when(mailSenderSpy).sendMessage(any());

        assertThatThrownBy(() -> mailSenderSpy.redirectMessage(RECIPIENT, MAIL_BODY))
                .isInstanceOf(HoldMailException.class)
                .hasMessage("couldn't send mail: BOOM")
                .hasCause(expectedCause);

    }

    @Test
    public void shouldInitializeMessage() throws Exception {

        String rawBody = "herpd derp";
        Session session = Session.getInstance(new Properties());

        Message message = mailSenderSpy.initializeMimeMessage(rawBody, session);

        Session actualSession = Whitebox.getInternalState(message, "session");
        assertThat(actualSession).isEqualTo(session);

    }

    @Test
    public void shouldGetMailSession() throws Exception {

        doReturn(OUTGOING_SERVER).when(mailSenderSpy).getOutgoingServer();
        doReturn(OUTGOING_PORT).when(mailSenderSpy).getOutgoingPort();

        Session mailSession = mailSenderSpy.getMailSession();

        Properties sessionProps = mailSession.getProperties();
        assertThat(sessionProps.get("mail.smtp.auth")).isEqualTo("false");
        assertThat(sessionProps.get("mail.smtp.starttls.enable")).isEqualTo("false");
        assertThat(sessionProps.get("mail.smtp.host")).isEqualTo(OUTGOING_SERVER);
        assertThat(sessionProps.get("mail.smtp.port")).isEqualTo(OUTGOING_PORT);

    }

    @Test
    public void shouldGetInjectedProps() throws Exception {

        Whitebox.setInternalState(mailSenderSpy, "outgoingServer", "outserver");
        Whitebox.setInternalState(mailSenderSpy, "outgoingPort", 999);
        Whitebox.setInternalState(mailSenderSpy, "senderFrom", "parse@host.tld");

        assertThat(mailSenderSpy.getOutgoingServer()).isEqualTo("outserver");
        assertThat(mailSenderSpy.getOutgoingPort()).isEqualTo(999);
        assertThat(mailSenderSpy.getSenderFrom()).isEqualTo("parse@host.tld");
    }

}
