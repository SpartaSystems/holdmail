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
