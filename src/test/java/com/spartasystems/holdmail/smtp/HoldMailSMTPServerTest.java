package com.spartasystems.holdmail.smtp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.subethamail.smtp.server.SMTPServer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HoldMailSMTPServer.class})
public class HoldMailSMTPServerTest {

    @Mock
    private SMTPHandlerFactory smtpHandlerFactoryMock;

    @InjectMocks
    private HoldMailSMTPServer holdMailSMTPServer;

    @Test
    public void shouldStartServer() throws Exception{

        SMTPServer smtpServerMock = mock(SMTPServer.class);
        whenNew(SMTPServer.class).withArguments(smtpHandlerFactoryMock).thenReturn(smtpServerMock);

        holdMailSMTPServer.startup();

        verify(smtpServerMock).setSoftwareName("HoldMailSMTPServer SMTP");
        verify(smtpServerMock).setPort(0);
        verify(smtpServerMock).start();

    }


    @Test
    public void shouldStopServer() throws Exception{

        SMTPServer smtpServerMock = mock(SMTPServer.class);
        whenNew(SMTPServer.class).withArguments(smtpHandlerFactoryMock).thenReturn(smtpServerMock);

        holdMailSMTPServer.startup();
        holdMailSMTPServer.shutdown();

        verify(smtpServerMock).stop();

    }
}
