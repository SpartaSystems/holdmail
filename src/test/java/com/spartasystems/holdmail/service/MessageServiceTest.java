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

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MessageServiceTest {

    private static final String SENDER_EMAIL = "sender@email.org";

    @Mock
    private MessageRepository messageRepositoryMock;

    @Mock
    private MessageMapper messageMapperMock;

    @Mock
    private OutgoingMailSender outgoingMailSenderMock;

    @Mock
    private MessageListMapper messageListMapper;

    @Mock
    private Pageable pageableMock;

    @InjectMocks
    private MessageService messageService;

    @Test
    public void shouldSaveMessage() {

        Message messageToSave = mock(Message.class);
        Message savedMessage = mock(Message.class);
        MessageEntity entityToSave = mock(MessageEntity.class);
        MessageEntity savedEntity = mock(MessageEntity.class);

        when(messageMapperMock.fromDomain(messageToSave)).thenReturn(entityToSave);
        when(messageRepositoryMock.save(entityToSave)).thenReturn(savedEntity);
        when(messageMapperMock.toDomain(savedEntity)).thenReturn(savedMessage);

        assertThat(messageService.saveMessage(messageToSave)).isEqualTo(savedMessage);

    }

    @Test
    public void shouldGetMessage() {

        long messageId = 34234;

        MessageEntity messageEntityMock = mock(MessageEntity.class);
        Message messageMock = mock(Message.class);

        when(messageRepositoryMock.findOne(messageId)).thenReturn(messageEntityMock);
        when(messageMapperMock.toDomain(messageEntityMock)).thenReturn(messageMock);

        assertThat(messageService.getMessage(messageId)).isEqualTo(messageMock);

    }

    @Test
    public void shouldFindMessagesAndReturnAllIfEmailIsBlank() {

        List<MessageEntity> entities = asList(mock(MessageEntity.class), mock(MessageEntity.class));
        MessageList messageListMock = mock(MessageList.class);

        when(messageRepositoryMock.findAllByOrderByReceivedDateDesc(pageableMock)).thenReturn(entities);
        when(messageListMapper.toMessageList(entities)).thenReturn(messageListMock);

        assertThat(messageService.findMessages(null, null, pageableMock)).isEqualTo(messageListMock);
        assertThat(messageService.findMessages("", null, pageableMock)).isEqualTo(messageListMock);
    }

    @Test
    public void shouldFindMessagesForRecipientIfEmailIsNotBlank() {

        List<MessageEntity> entities = asList(mock(MessageEntity.class), mock(MessageEntity.class));
        MessageList messageListMock = mock(MessageList.class);

        when(messageRepositoryMock.findAllForRecipientOrderByReceivedDateDesc(SENDER_EMAIL, pageableMock))
                .thenReturn(entities);
        when(messageListMapper.toMessageList(entities)).thenReturn(messageListMock);

        assertThat(messageService.findMessages(SENDER_EMAIL, null, pageableMock)).isEqualTo(messageListMock);
    }

    @Test
    public void shouldForwardMail() {

        Message messageMock = mock(Message.class);
        when(messageMock.getRawMessage()).thenReturn("RAW message");

        MessageService messageServiceSpy = spy(messageService);
        when(messageServiceSpy.getMessage(359)).thenReturn(messageMock);

        messageServiceSpy.forwardMessage(359, "some@guy.com");
        verify(outgoingMailSenderMock).redirectMessage("some@guy.com", "RAW message");

    }
}
