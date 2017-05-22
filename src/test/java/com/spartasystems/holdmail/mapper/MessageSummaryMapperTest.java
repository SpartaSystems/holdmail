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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
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

    private static final long                MESSAGE_ID   = 1233;
    private static final String              IDENTIFIER   = "mesgId";
    private static final String              SUBJECT      = "msgSubj";
    private static final String              SENDER_MAIL  = "msgSenderMail";
    private static final Date                RECEIVED     = new Date();
    private static final String              SENDER_HOST  = "msgSenderHost";
    private static final int                 MESSAGE_SIZE = 4534;
    private static final String              RAW_CONTENT  = "rawText";
    private static final List<String>        RECIPIENTS   = asList("recip1", "recip2");
    private static final Map<String, String> HEADER_VALS  = of("k1", "v1", "k2", "v2");
    private static final MessageHeaders      HEADERS      = new MessageHeaders(HEADER_VALS);
    private static final String              CONTENT_TXT  = "content_text";
    private static final String              CONTENT_HTML = "content_html";

    private static final Message MESSAGE_SPY = spy(new Message(MESSAGE_ID, IDENTIFIER, SUBJECT, SENDER_MAIL,
            RECEIVED, SENDER_HOST, MESSAGE_SIZE, RAW_CONTENT, RECIPIENTS, HEADERS));

    private static final MessageContent CONTENT_MOCK = mock(MessageContent.class);

    @Spy
    @InjectMocks
    private MessageSummaryMapper messageSummaryMapperSpy;

    @Before
    public void setUp() throws Exception {
        when(CONTENT_MOCK.findFirstHTMLPart()).thenReturn(CONTENT_HTML);
        when(CONTENT_MOCK.findFirstTextPart()).thenReturn(CONTENT_TXT);
        doReturn(CONTENT_MOCK).when(MESSAGE_SPY).getContent();
    }

    @Test
    public void shouldMapToMessageSummaryWithRAW() throws Exception {

        MessageSummary expected = new MessageSummary(MESSAGE_ID, IDENTIFIER, SUBJECT, SENDER_MAIL,
                RECEIVED, SENDER_HOST, MESSAGE_SIZE, "recip1,recip2",
                RAW_CONTENT, HEADER_VALS, CONTENT_TXT, CONTENT_HTML);

        doReturn(true).when(messageSummaryMapperSpy).getEnableRaw();

        MessageSummary actual = messageSummaryMapperSpy.toMessageSummary(MESSAGE_SPY);

        assertThat(actual).isEqualTo(expected);

    }

    @Test
    public void shouldMapToMessageSummaryWithoutRAW() throws Exception {

        MessageSummary expected = new MessageSummary(MESSAGE_ID, IDENTIFIER, SUBJECT, SENDER_MAIL,
                RECEIVED, SENDER_HOST, MESSAGE_SIZE, "recip1,recip2",
                null, HEADER_VALS, CONTENT_TXT, CONTENT_HTML);

        doReturn(false).when(messageSummaryMapperSpy).getEnableRaw();

        MessageSummary actual = messageSummaryMapperSpy.toMessageSummary(MESSAGE_SPY);

        assertThat(actual).isEqualTo(expected);


    }

}
