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

package com.spartasystems.holdmail.service;

import com.spartasystems.holdmail.domain.Message;
import com.spartasystems.holdmail.mapper.MessageListMapper;
import com.spartasystems.holdmail.mapper.MessageMapper;
import com.spartasystems.holdmail.model.MessageList;
import com.spartasystems.holdmail.persistence.MessageEntity;
import com.spartasystems.holdmail.persistence.MessageRepository;
import com.spartasystems.holdmail.smtp.OutgoingMailSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MessageServiceTest {

    private static final String SUBJECT = "email subject";
    private static final String SENDER_EMAIL = "sender@email.org";
    @Mock
    private MessageRepository messageRepositoryMock;

    @Mock
    private MessageMapper messageMapperMock;

    @Mock
    private MessageListMapper messageListMapper;

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

    @Test
    public void shouldFindMessagesBySubject() throws Exception {
        MessageEntity messageEntityMock1 = new MessageEntity();
        MessageEntity messageEntityMock2 = new MessageEntity();
        Stream<MessageEntity> entityStream = Stream.of(messageEntityMock1, messageEntityMock2);
        when(messageRepositoryMock.findBySubject(eq(SUBJECT),any(Pageable.class))).thenReturn(entityStream);
        MessageList messageListMock = mock(MessageList.class);
        when(messageListMapper.toMessageList(entityStream)).thenReturn(messageListMock);

        MessageList messageListBySubject = messageService.findMessageListBySubject(SUBJECT);

        assertThat(messageListBySubject).isEqualTo(messageListMock);

    }

    @Test
    public void shouldFindMessageListBySenderEmail() throws Exception {
        MessageEntity messageEntityMock1 = new MessageEntity();
        MessageEntity messageEntityMock2 = new MessageEntity();
        Stream<MessageEntity> entityStream = Stream.of(messageEntityMock1, messageEntityMock2);
        when(messageRepositoryMock.findBySenderEmail(eq(SENDER_EMAIL),any(Pageable.class))).thenReturn(entityStream);
        MessageList messageListMock = mock(MessageList.class);
        when(messageListMapper.toMessageList(entityStream)).thenReturn(messageListMock);

        MessageList messageListBySubject = messageService.findMessageListBySenderEmail(SENDER_EMAIL);

        assertThat(messageListBySubject).isEqualTo(messageListMock);
    }

}
