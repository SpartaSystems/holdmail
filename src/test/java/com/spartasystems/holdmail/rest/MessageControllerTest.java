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

package com.spartasystems.holdmail.rest;

import com.spartasystems.holdmail.domain.Message;
import com.spartasystems.holdmail.mapper.MessageSummaryMapper;
import com.spartasystems.holdmail.mime.MimeBodyPart;
import com.spartasystems.holdmail.model.MessageForwardCommand;
import com.spartasystems.holdmail.model.MessageList;
import com.spartasystems.holdmail.model.MessageListItem;
import com.spartasystems.holdmail.model.MessageSummary;
import com.spartasystems.holdmail.service.MessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.omg.CORBA.portable.InputStream;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.http.MediaType.TEXT_PLAIN;

@RunWith(MockitoJUnitRunner.class)
public class MessageControllerTest {

    public static final String MOE_SZYSLAK = "moe szyslak";

    @Mock
    private MessageService messageServiceMock;

    @Mock
    private MessageSummaryMapper messageSummaryMapperMock;

    @Mock
    private MimeContentIdPreParser mimeContentIdPreParserMock;

    @Spy
    @InjectMocks
    private MessageController messageControllerSpy;

    @Test
    public void shouldGetMessagesLessThanMax() throws Exception {

        MessageList expectedMessages = mockMessages(100);
        when(messageServiceMock.findMessages(MOE_SZYSLAK)).thenReturn(expectedMessages);

        assertThat(messageControllerSpy.getMessages(MOE_SZYSLAK)).isEqualTo(expectedMessages);
    }

    @Test
    public void shouldGetMessagesMoreThanMax() throws Exception {

        MessageList expectedMessages = mockMessages(200);
        when(messageServiceMock.findMessages(MOE_SZYSLAK)).thenReturn(expectedMessages);

        List<MessageListItem> messages = messageControllerSpy.getMessages(MOE_SZYSLAK).getMessages();

        assertThat(messages).hasSize(151);

        MessageListItem lastItem = messages.get(messages.size() - 1);
        assertThat(lastItem.getSubject()).isEqualTo("hold-mail return max 150 mails (for now)");

    }

    @Test
    public void shouldGetMessageContent() throws Exception {

        MessageSummary summaryMock = mock(MessageSummary.class);
        doReturn(summaryMock).when(messageControllerSpy).loadMessageSummary(445);

        ResponseEntity response = messageControllerSpy.getMessageContent(445);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(summaryMock);

    }

    @Test
    public void shouldGetMessageContentHTML() throws Exception {

        MessageSummary summaryMock = setupSpyToLoadMessageSummaryMock(900, null, null, "original_html");

        when(mimeContentIdPreParserMock.replaceWithRestPath(900, summaryMock.getMessageBodyHTML())).thenReturn("modified_html");

        ResponseEntity expectedResponse = mock(ResponseEntity.class);
        doReturn(expectedResponse).when(messageControllerSpy).serveContent("modified_html", TEXT_HTML);

        assertThat(messageControllerSpy.getMessageContentHTML(900)).isEqualTo(expectedResponse);

    }

    @Test
    public void shouldGetMessageContextTEXT() throws Exception {

        MessageSummary summaryMock = setupSpyToLoadMessageSummaryMock(555, null, "some_text", null);

        ResponseEntity expectedResponse = mock(ResponseEntity.class);
        String text = summaryMock.getMessageBodyText();
        doReturn(expectedResponse).when(messageControllerSpy).serveContent(text, TEXT_PLAIN);

        assertThat(messageControllerSpy.getMessageContentTEXT(555)).isEqualTo(expectedResponse);

    }

    @Test
    public void shouldGetMessageContextRAW() throws Exception {

        MessageSummary summaryMock = setupSpyToLoadMessageSummaryMock(129, "raw_msg", null, null);

        ResponseEntity expectedResponse = mock(ResponseEntity.class);
        String raw = summaryMock.getMessageRaw();
        doReturn(expectedResponse).when(messageControllerSpy).serveContent(raw, TEXT_PLAIN);

        assertThat(messageControllerSpy.getMessageContentRAW(129)).isEqualTo(expectedResponse);

    }

    @Test
    public void shouldGetMessageContentByPartId() throws Exception{

        final int MSG_ID = 983;
        final String PART_ID = "derpPart";
        final String CONTENT_TYPE = "some/type";
        final InputStream CONTENT_STREAM = mock(InputStream.class);

        MimeBodyPart contentMock = mock(MimeBodyPart.class);
        when(contentMock.getContentType()).thenReturn(CONTENT_TYPE);
        when(contentMock.getContentStream()).thenReturn(CONTENT_STREAM);

        MessageSummary summaryMock = setupSpyToLoadMessageSummaryMock(MSG_ID, null, null, null);
        when(summaryMock.getMessageContentById(PART_ID)).thenReturn(contentMock);

        ResponseEntity response = messageControllerSpy.getMessageContentByPartId(MSG_ID, PART_ID);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().get("Content-Type")).hasSize(1).contains(CONTENT_TYPE);
        assertThat(response.getBody()).isEqualTo(new InputStreamResource(CONTENT_STREAM));

    }

    @Test
    public void shouldCallMessageServiceToForwardMail() throws Exception {

        final int ID = 345;
        final String RECIPIENT = "some@guy.com";
        ResponseEntity response = messageControllerSpy.fowardMail(ID, new MessageForwardCommand(RECIPIENT));

        Mockito.verify(messageServiceMock).forwardMessage(ID, RECIPIENT);
        assertThat(response).isEqualTo(ResponseEntity.accepted().build());

    }

    private MessageSummary setupSpyToLoadMessageSummaryMock(int messageId, String raw, String text, String html)
            throws Exception {

        MessageSummary summaryMock = mock(MessageSummary.class);
        when(summaryMock.getMessageRaw()).thenReturn(raw);
        when(summaryMock.getMessageBodyText()).thenReturn(text);
        when(summaryMock.getMessageBodyHTML()).thenReturn(html);
        doReturn(summaryMock).when(messageControllerSpy).loadMessageSummary(messageId);

        return summaryMock;
    }

    @Test
    public void shouldLoadSummary() throws Exception {

        Message messageMock = mock(Message.class);
        MessageSummary summaryMock = mock(MessageSummary.class);

        when(messageServiceMock.getMessage(123)).thenReturn(messageMock);
        when(messageSummaryMapperMock.toMessageSummary(messageMock)).thenReturn(summaryMock);

        assertThat(messageControllerSpy.loadMessageSummary(123)).isEqualTo(summaryMock);
    }

    @Test
    public void shouldServeNotFoundWhenDataIsNull() throws Exception {

        ResponseEntity response = messageControllerSpy.serveContent(null, null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    public void shouldServeContentWhenDataIsNull() throws Exception {

        Object content = "{}";
        MediaType type = MediaType.APPLICATION_JSON;

        ResponseEntity response = messageControllerSpy.serveContent(content, type);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(type);
        assertThat(response.getBody()).isEqualTo(content);

    }

    private MessageList mockMessages(int numMessages) {

        List<MessageListItem> messages = new ArrayList<>(numMessages);
        for (int i = 0; i < numMessages; i++) {
            messages.add(mock(MessageListItem.class));
        }

        return new MessageList(messages);
    }

}
