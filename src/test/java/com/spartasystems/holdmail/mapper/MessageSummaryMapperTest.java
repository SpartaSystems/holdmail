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

package com.spartasystems.holdmail.mapper;

import com.spartasystems.holdmail.domain.Message;
import com.spartasystems.holdmail.mime.MimeBodyParser;
import com.spartasystems.holdmail.mime.MimeBodyParts;
import com.spartasystems.holdmail.mime.MimeHeaders;
import com.spartasystems.holdmail.model.MessageSummary;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.omg.CORBA.portable.InputStream;
import org.powermock.reflect.Whitebox;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.ImmutableMap.of;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MessageSummaryMapperTest {

    public static final long         MESSAGE_ID   = 1233;
    public static final String       IDENTIFIER   = "mesgId";
    public static final String       SUBJECT      = "msgSubj";
    public static final String       SENDER_MAIL  = "msgSenderMail";
    public static final Date         RECEIVED     = new Date();
    public static final String       SENDER_HOST  = "msgSenderHost";
    public static final int          MESSAGE_SIZE = 4534;
    public static final String       RAW_CONTENT  = "rawText";
    public static final List<String> RECIPIENTS   = asList("recip1", "recip2");
    public static final MimeHeaders  HEADERS      = new MimeHeaders(of("k1", "v1"));

    @Mock
    private MimeBodyParser mimeBodyParserMock;

    @InjectMocks
    private MessageSummaryMapper messageSummaryMapper;

    @Test
    public void shouldMapToMessageSummary() throws Exception {

        MimeBodyParts expectedBodyParts = mock(MimeBodyParts.class);

        ArgumentCaptor<InputStream> streamCaptor = ArgumentCaptor.forClass(InputStream.class);
        when(mimeBodyParserMock.findAllBodyParts(streamCaptor.capture())).thenReturn(expectedBodyParts);

        Message message = new Message(MESSAGE_ID, IDENTIFIER, SUBJECT, SENDER_MAIL,
                RECEIVED, SENDER_HOST, MESSAGE_SIZE,
                RAW_CONTENT, RECIPIENTS, HEADERS);

        MessageSummary summary = messageSummaryMapper.toMessageSummary(message);
        assertThat(summary.getMessageId()).isEqualTo(MESSAGE_ID);
        assertThat(summary.getIdentifier()).isEqualTo(IDENTIFIER);
        assertThat(summary.getSubject()).isEqualTo(SUBJECT);
        assertThat(summary.getSenderEmail()).isEqualTo(SENDER_MAIL);
        assertThat(summary.getReceivedDate()).isEqualTo(RECEIVED);
        assertThat(summary.getSenderHost()).isEqualTo(SENDER_HOST);
        assertThat(summary.getMessageSize()).isEqualTo(MESSAGE_SIZE);
        assertThat(summary.getRecipients()).isEqualTo("recip1,recip2");
        assertThat(summary.getMessageRaw()).isEqualTo(RAW_CONTENT);
        assertThat(summary.getMessageHeaders()).isEqualTo(HEADERS);

        assertThat(IOUtils.contentEquals(streamCaptor.getValue(), IOUtils.toInputStream(RAW_CONTENT, UTF_8)));

        MimeBodyParts actualParts = Whitebox.getInternalState(summary, "mimeBodyParts");
        assertThat(actualParts).isEqualTo(expectedBodyParts);

    }

}
