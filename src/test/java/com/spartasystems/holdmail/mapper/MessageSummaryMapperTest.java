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

package com.spartasystems.holdmail.mapper;

import com.spartasystems.holdmail.domain.Message;
import com.spartasystems.holdmail.domain.MessageContent;
import com.spartasystems.holdmail.domain.MessageHeaders;
import com.spartasystems.holdmail.model.MessageSummary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MessageSummaryMapperTest {

    public static final  long                MESSAGE_ID   = 1233;
    public static final  String              IDENTIFIER   = "mesgId";
    public static final  String              SUBJECT      = "msgSubj";
    public static final  String              SENDER_MAIL  = "msgSenderMail";
    public static final  Date                RECEIVED     = new Date();
    public static final  String              SENDER_HOST  = "msgSenderHost";
    public static final  int                 MESSAGE_SIZE = 4534;
    public static final  String              RAW_CONTENT  = "rawText";
    public static final  List<String>        RECIPIENTS   = asList("recip1", "recip2");
    public static final  Map<String, String> HEADER_VALS  = of("k1", "v1", "k2", "v2");
    public static final  MessageHeaders      HEADERS      = new MessageHeaders(HEADER_VALS);
    private static final String              CONTENT_TXT  = "content_text";
    private static final String              CONTENT_HTML = "content_html";

    @InjectMocks
    private MessageSummaryMapper messageSummaryMapper;

    @Test
    public void shouldMapToMessageSummary() throws Exception {

        Message messageSpy = spy(new Message(MESSAGE_ID, IDENTIFIER, SUBJECT, SENDER_MAIL,
                RECEIVED, SENDER_HOST, MESSAGE_SIZE,
                RAW_CONTENT, RECIPIENTS, HEADERS));

        MessageContent contentMock = mock(MessageContent.class);
        when(contentMock.findFirstHTMLPart()).thenReturn(CONTENT_HTML);
        when(contentMock.findFirstTextPart()).thenReturn(CONTENT_TXT);
        doReturn(contentMock).when(messageSpy).getContent();

        MessageSummary expected = new MessageSummary(MESSAGE_ID, IDENTIFIER, SUBJECT, SENDER_MAIL,
                RECEIVED, SENDER_HOST, MESSAGE_SIZE, "recip1,recip2",
                RAW_CONTENT, HEADER_VALS, CONTENT_TXT, CONTENT_HTML);

        MessageSummary actual = messageSummaryMapper.toMessageSummary(messageSpy);

        assertThat(actual).isEqualTo(expected);

    }

}
