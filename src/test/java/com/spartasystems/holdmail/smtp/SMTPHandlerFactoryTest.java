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
