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

package com.spartasystems.holdmail.model;

import com.google.common.collect.ImmutableMap;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class MessageSummaryTest {

    public static final int                 ID         = 123;
    public static final String              IDENTIFIER = "mId";
    public static final String              SUBJECT    = "mSubj";
    public static final String              SENDER     = "mSender";
    public static final Date                RECIEVED   = new Date();
    public static final String              SENDERHOST = "mHost";
    public static final int                 SIZE       = 10000;
    public static final String              RECIPIENTS = "mRecips";
    public static final Map<String, String> HEADERS    = ImmutableMap.of("k", "v");
    public static final String              RAW        = "mRAW";
    public static final String              HTML       = "<SomeHTML>";
    public static final String              TEXT       = "someTEXT";
    public static final List<MessageAttachment>  ATTACHMENTS = asList(
                                            mock(MessageAttachment.class), mock(MessageAttachment.class));

    @Test
    public void shouldSetValuesOnConstruct() throws Exception {

        MessageSummary summary = buildSummary(HTML, TEXT);

        assertThat(summary.getMessageId()).isEqualTo(ID);
        assertThat(summary.getIdentifier()).isEqualTo(IDENTIFIER);
        assertThat(summary.getSubject()).isEqualTo(SUBJECT);
        assertThat(summary.getSenderEmail()).isEqualTo(SENDER);
        assertThat(summary.getReceivedDate()).isEqualTo(RECIEVED);
        assertThat(summary.getSenderHost()).isEqualTo(SENDERHOST);
        assertThat(summary.getMessageSize()).isEqualTo(SIZE);
        assertThat(summary.getRecipients()).isEqualTo(RECIPIENTS);
        assertThat(summary.getMessageHeaders()).isEqualTo(HEADERS);
        assertThat(summary.getAttachments()).isEqualTo(ATTACHMENTS);

    }

    @Test
    public void shouldIndicateWhenMessageHasHTML() throws Exception{

        assertThat(buildSummary(HTML, TEXT).getMessageHasBodyHTML()).isTrue();
        assertThat(buildSummary(null, TEXT).getMessageHasBodyHTML()).isFalse();
        assertThat(buildSummary("", TEXT).getMessageHasBodyHTML()).isFalse();
    }

    @Test
    public void shouldIndicateWhenMessageHasText() throws Exception{

        assertThat(buildSummary(HTML, TEXT).getMessageHasBodyText()).isTrue();
        assertThat(buildSummary(HTML, null).getMessageHasBodyText()).isFalse();
        assertThat(buildSummary(HTML, "").getMessageHasBodyText()).isFalse();
    }

    private MessageSummary buildSummary(String html, String text) {
        return new MessageSummary(ID, IDENTIFIER, SUBJECT, SENDER,
                RECIEVED, SENDERHOST, SIZE, RECIPIENTS,
                HEADERS, text, html, ATTACHMENTS);
    }

    @Test
    public void shouldHaveValidEqualsHashcode() throws Exception {

        EqualsVerifier.forClass(MessageSummary.class)
                      .suppress(Warning.NONFINAL_FIELDS,
                              Warning.STRICT_INHERITANCE)
                      .verify();

    }
}
