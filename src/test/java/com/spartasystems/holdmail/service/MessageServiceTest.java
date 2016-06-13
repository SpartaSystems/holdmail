package com.spartasystems.holdmail.service;

import com.spartasystems.holdmail.domain.Message;
import com.spartasystems.holdmail.mapper.MessageMapper;
import com.spartasystems.holdmail.persistence.MessageRepository;
import com.spartasystems.holdmail.smtp.OutgoingMailSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MessageServiceTest {

    @Mock
    private MessageRepository messageRepositoryMock;

    @Mock
    private MessageMapper messageMapperMock;

    @Mock
    private OutgoingMailSender outgoingMailSenderMock;

    @InjectMocks
    private MessageService messageService;

    @Test
    public void shouldForwardMail() throws Exception {

        Message messageMock = mock(Message.class);
        when(messageMock.getRawMessage()).thenReturn("RAW message");

        MessageService messageServiceSpy = spy(messageService);
        when(messageServiceSpy.getMessage(359)).thenReturn(messageMock);

        messageServiceSpy.forwardMessage(359, "some@guy.com");
        verify(outgoingMailSenderMock).sendEmail("some@guy.com", "RAW message");

    }

}