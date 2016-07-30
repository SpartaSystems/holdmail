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

package com.spartasystems.holdmail.model;

import com.google.common.collect.ImmutableMap;
import com.spartasystems.holdmail.mime.MimeBodyPart;
import com.spartasystems.holdmail.mime.MimeBodyParts;
import com.spartasystems.holdmail.mime.MimeHeaders;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MessageSummaryTest {

    public static final int ID = 123;
    public static final String IDENTIFIER = "mId";
    public static final String SUBJECT = "mSubj";
    public static final String SENDER = "mSender";
    public static final Date RECIEVED = new Date();
    public static final String SENDERHOST = "mHost";
    public static final int SIZE = 10000;
    public static final String RECIPIENTS = "mRecips";
    public static final String RAW = "mRAW";
    public static final MimeHeaders HEADERS = new MimeHeaders(ImmutableMap.of("k", "v"));
    public static final String CONTENT_ID = "derpId";

    @Test
    public void shouldSetValuesOnConstruct() throws Exception{

        MessageSummary summary = new MessageSummary(ID, IDENTIFIER, SUBJECT, SENDER,
                RECIEVED, SENDERHOST, SIZE, RECIPIENTS, RAW,
                HEADERS, null);

        assertThat(summary.getMessageId()).isEqualTo(ID);
        assertThat(summary.getIdentifier()).isEqualTo(IDENTIFIER);
        assertThat(summary.getSubject()).isEqualTo(SUBJECT);
        assertThat(summary.getSenderEmail()).isEqualTo(SENDER);
        assertThat(summary.getReceivedDate()).isEqualTo(RECIEVED);
        assertThat(summary.getSenderHost()).isEqualTo(SENDERHOST);
        assertThat(summary.getMessageSize()).isEqualTo(SIZE);
        assertThat(summary.getRecipients()).isEqualTo(RECIPIENTS);
        assertThat(summary.getMessageRaw()).isEqualTo(RAW);
        assertThat(summary.getMessageHeaders()).isEqualTo(HEADERS);

    }

    @Test
    public void shouldReturnNoHTMLWhenMessageBodyHTMLNotPresent() throws Exception{

        MimeBodyParts parts = mock(MimeBodyParts.class);
        when(parts.findFirstHTMLBody()).thenReturn(null);

        MessageSummary summary = new MessageSummary(ID, IDENTIFIER, SUBJECT, SENDER,
                RECIEVED, SENDERHOST, SIZE, RECIPIENTS, RAW,
                HEADERS, parts);

        assertThat(summary.getMessageHasBodyHTML()).isFalse();
        assertThat(summary.getMessageBodyHTML()).isNull();

    }

    @Test
    public void shouldReturnHTMLWhenMessageBodyHTMLPresent() throws Exception{

        MimeBodyPart html = mock(MimeBodyPart.class);
        when(html.getContentString()).thenReturn("html content");

        MimeBodyParts parts = mock(MimeBodyParts.class);
        when(parts.findFirstHTMLBody()).thenReturn(html);

        MessageSummary summary = new MessageSummary(ID, IDENTIFIER, SUBJECT, SENDER,
                RECIEVED, SENDERHOST, SIZE, RECIPIENTS, RAW,
                HEADERS, parts);

        assertThat(summary.getMessageHasBodyHTML()).isTrue();
        assertThat(summary.getMessageBodyHTML()).isEqualTo("html content");

    }


    @Test
    public void shouldReturnNoTextWhenMessageBodyTextNotPresent() throws Exception{

        MimeBodyParts parts = mock(MimeBodyParts.class);
        when(parts.findFirstTextBody()).thenReturn(null);

        MessageSummary summary = new MessageSummary(ID, IDENTIFIER, SUBJECT, SENDER,
                RECIEVED, SENDERHOST, SIZE, RECIPIENTS, RAW,
                HEADERS, parts);

        assertThat(summary.getMessageHasBodyText()).isFalse();
        assertThat(summary.getMessageBodyText()).isNull();

    }

    @Test
    public void shouldReturnTextWhenMessageBodyTextPresent() throws Exception{

        MimeBodyPart text = mock(MimeBodyPart.class);
        when(text.getContentString()).thenReturn("text content");

        MimeBodyParts parts = mock(MimeBodyParts.class);
        when(parts.findFirstTextBody()).thenReturn(text);

        MessageSummary summary = new MessageSummary(ID, IDENTIFIER, SUBJECT, SENDER,
                RECIEVED, SENDERHOST, SIZE, RECIPIENTS, RAW,
                HEADERS, parts);

        assertThat(summary.getMessageHasBodyText()).isTrue();
        assertThat(summary.getMessageBodyText()).isEqualTo("text content");

    }

    @Test
    public void shouldGetMessageContentById() throws Exception{

        MimeBodyPart expected = mock(MimeBodyPart.class);

        MimeBodyParts parts = mock(MimeBodyParts.class);
        when(parts.findByContentId(CONTENT_ID)).thenReturn(expected);

        MessageSummary summary = new MessageSummary(ID, IDENTIFIER, SUBJECT, SENDER,
                RECIEVED, SENDERHOST, SIZE, RECIPIENTS, RAW,
                HEADERS, parts);

        assertThat(summary.getMessageContentById(CONTENT_ID)).isEqualTo(expected);
    }

    @Test
    public void shouldHaveValidEqualsHashcode() throws Exception {

        EqualsVerifier.forClass(MessageSummary.class)
                      .suppress(Warning.NONFINAL_FIELDS,
                              Warning.STRICT_INHERITANCE)
                      .verify();

    }
}
