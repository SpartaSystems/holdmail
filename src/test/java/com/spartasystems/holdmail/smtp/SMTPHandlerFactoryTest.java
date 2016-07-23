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
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.subethamail.smtp.MessageContext;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SMTPHandlerFactory.class})
public class SMTPHandlerFactoryTest {

    @Mock
    private AutowireCapableBeanFactory beanFactoryMock;

    @InjectMocks
    private SMTPHandlerFactory smtpHandlerFactory;

    @Test
    public void shouldCreateHandler() throws Exception{

        MessageContext ctxMock = mock(MessageContext.class);
        SMTPHandler handlerMock = mock(SMTPHandler.class);
        whenNew(SMTPHandler.class).withArguments(ctxMock).thenReturn(handlerMock);

        SMTPHandler actual = smtpHandlerFactory.create(ctxMock);

        assertThat(actual).isEqualTo(handlerMock);
        verify(beanFactoryMock).autowireBean(handlerMock);



    }


}
