/*******************************************************************************
 * Copyright 2016 - 2017 Sparta Systems, Inc
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
import com.spartasystems.holdmail.domain.MessageContent;
import com.spartasystems.holdmail.domain.MessageContentPart;
import com.spartasystems.holdmail.mapper.MessageSummaryMapper;
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
import org.springframework.data.domain.Pageable;
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

    @Mock
    private Pageable pageableMock;

    @Spy
    @InjectMocks
    private MessageController messageControllerSpy;
    public static final int MSG_ID = 55;
    public static final int SEQ_ID = 4523;
    public static final String CONTENT_TYPE = "attach/type";
    public static final String FILE_NAME = "wibble.pdf";

    @Test
    public void shouldGetMessages() throws Exception {

        List<MessageListItem> messageMocks = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            messageMocks.add(mock(MessageListItem.class));
        }

        MessageList expectedMessages = new MessageList(messageMocks);

        when(messageServiceMock.findMessages(MOE_SZYSLAK, pageableMock)).thenReturn(expectedMessages);

        assertThat(messageControllerSpy.getMessages(MOE_SZYSLAK, pageableMock)).isEqualTo(expectedMessages);
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

        String raw = "original-mime-message";

        Message domainMock = mock(Message.class);
        when(domainMock.getRawMessage()).thenReturn(raw);
        when(messageServiceMock.getMessage(129)).thenReturn(domainMock);

        ResponseEntity expectedResponse = mock(ResponseEntity.class);
        doReturn(expectedResponse).when(messageControllerSpy).serveContent(raw, TEXT_PLAIN);

        assertThat(messageControllerSpy.getMessageContentRAW(129)).isEqualTo(expectedResponse);

    }

    @Test
    public void shouldGetMessageContentByPartId() throws Exception{

        final int MSG_ID = 983;
        final String PART_ID = "derpPart";
        final String CONTENT_TYPE = "some/type";
        final InputStream CONTENT_STREAM = mock(InputStream.class);

        MessageContentPart contentPartMock = mock(MessageContentPart.class);
        when(contentPartMock.getContentType()).thenReturn(CONTENT_TYPE);
        when(contentPartMock.getContentStream()).thenReturn(CONTENT_STREAM);

        Message messageMock = mock(Message.class);
        when(messageServiceMock.getMessage(MSG_ID)).thenReturn(messageMock);

        MessageContent contentMock = mock(MessageContent.class);
        when(messageMock.getContent()).thenReturn(contentMock);
        when(contentMock.findByContentId(PART_ID)).thenReturn(contentPartMock);

        ResponseEntity response = messageControllerSpy.getMessageContentByPartId(MSG_ID, PART_ID);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().get("Content-Type")).hasSize(1).contains(CONTENT_TYPE);
        assertThat(response.getBody()).isEqualTo(new InputStreamResource(CONTENT_STREAM));

    }

    @Test
    public void shouldGetMessageAttachmentBySequenceId() throws Exception{

        final InputStream streamMock = mock(InputStream.class);

        setupForFetchAttachment(streamMock, FILE_NAME);

        String expectedDisposition = "attachment; filename=\"" + FILE_NAME + "\";";

        ResponseEntity response = messageControllerSpy.getMessageContentByAttachmentId(MSG_ID, SEQ_ID);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().get("Content-Type")).hasSize(1).contains(CONTENT_TYPE);
        assertThat(response.getHeaders().get("Content-Disposition")).hasSize(1).contains(expectedDisposition);
        assertThat(response.getBody()).isEqualTo(new InputStreamResource(streamMock));

    }

    @Test
    public void shouldNotSetFilenameDispositionWhenFilenameNull() throws Exception{

        final InputStream streamMock = mock(InputStream.class);
        setupForFetchAttachment(streamMock, null);

        ResponseEntity response = messageControllerSpy.getMessageContentByAttachmentId(MSG_ID, SEQ_ID);
        assertThat(response.getHeaders().get("Content-Disposition")).hasSize(1).contains("attachment;");

    }


    private void setupForFetchAttachment(InputStream streamMock, String attachmentName) {

        MessageContentPart contentPartMock = mock(MessageContentPart.class);
        when(contentPartMock.getAttachmentFilename()).thenReturn(attachmentName);
        when(contentPartMock.getContentType()).thenReturn(CONTENT_TYPE);
        when(contentPartMock.getContentStream()).thenReturn(streamMock);

        Message messageMock = mock(Message.class);
        when(messageServiceMock.getMessage(MSG_ID)).thenReturn(messageMock);

        MessageContent contentMock = mock(MessageContent.class);
        when(messageMock.getContent()).thenReturn(contentMock);
        when(contentMock.findBySequenceId(SEQ_ID)).thenReturn(contentPartMock);
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


}
