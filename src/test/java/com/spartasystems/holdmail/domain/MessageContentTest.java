/*******************************************************************************
 * Copyright 2016 - 2018 Sparta Systems, Inc
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

package com.spartasystems.holdmail.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MessageContentTest {

    private static final String PART_1_CONTENT = "p1Content";
    private static final String PART_2_CONTENT = "p2Content";
    private static final String PART_3_CONTENT = "p3Content";
    private static final String PART_4_CONTENT = "p4Content";

    @Mock
    private MessageContentPart part1Mock;

    @Mock
    private MessageContentPart part2Mock;

    @Mock
    private MessageContentPart part3Mock;

    @Mock
    private MessageContentPart part4Mock;

    @Before
    public void setUp() {

        when(part1Mock.getContentString()).thenReturn(PART_1_CONTENT);
        when(part2Mock.getContentString()).thenReturn(PART_2_CONTENT);
        when(part3Mock.getContentString()).thenReturn(PART_3_CONTENT);
        when(part4Mock.getContentString()).thenReturn(PART_4_CONTENT);

    }

    @Test
    public void shouldHaveNoPartsOnConstruction() {

        MessageContent messageContent = new MessageContent();

        assertThat(messageContent.getParts()).isEmpty();

    }

    @Test
    public void shouldAddParts() {

        MessageContent messageContent = new MessageContent();
        messageContent.addPart(part1Mock);
        messageContent.addPart(part2Mock);

        assertThat(messageContent.getParts()).isEqualTo(asList(part1Mock, part2Mock));
    }

    @Test
    public void shouldNotFindFirstHTMLBody() {

        MessageContent messageContent = buildMimeBodyPartsWith4Parts();
        assertThat(messageContent.findFirstHTMLPart()).isNull();
    }

    @Test
    public void shouldFindFirstHTMLBody() {

        when(part2Mock.isHTML()).thenReturn(true);

        MessageContent messageContent = buildMimeBodyPartsWith4Parts();

        assertThat(messageContent.findFirstHTMLPart()).isEqualTo(PART_2_CONTENT);

    }

    @Test
    public void shouldNotFindFirstTextBody() {

        MessageContent messageContent = buildMimeBodyPartsWith4Parts();
        assertThat(messageContent.findFirstTextPart()).isNull();
    }

    @Test
    public void shouldFindFirstTextBody() {

        when(part4Mock.isText()).thenReturn(true);

        MessageContent messageContent = buildMimeBodyPartsWith4Parts();

        assertThat(messageContent.findFirstTextPart()).isEqualTo(PART_4_CONTENT);

    }

    @Test
    public void shouldFindAllAttachmentsWithDispositionTrue() {

        when(part1Mock.hasAttachmentDisposition(true)).thenReturn(false);
        when(part2Mock.hasAttachmentDisposition(true)).thenReturn(true);
        when(part3Mock.hasAttachmentDisposition(true)).thenReturn(false);
        when(part4Mock.hasAttachmentDisposition(true)).thenReturn(true);

        MessageContent messageContent = buildMimeBodyPartsWith4Parts();

        assertThat(messageContent.findAttachmentParts(true)).containsExactly(part2Mock, part4Mock);

    }

    @Test
    public void shouldFindAllAttachmentsWithDispositionFalse() {

        when(part1Mock.hasAttachmentDisposition(false)).thenReturn(false);
        when(part2Mock.hasAttachmentDisposition(false)).thenReturn(false);
        when(part3Mock.hasAttachmentDisposition(false)).thenReturn(true);
        when(part4Mock.hasAttachmentDisposition(false)).thenReturn(true);

        MessageContent messageContent = buildMimeBodyPartsWith4Parts();

        assertThat(messageContent.findAttachmentParts(false)).containsExactly(part3Mock, part4Mock);

    }

    @Test
    public void shouldNotFindByContentId() {

        MessageContent messageContent = buildMimeBodyPartsWith4Parts();
        assertThat(messageContent.findByContentId("blah")).isNull();
    }

    @Test
    public void shouldFindByContentId() {

        when(part3Mock.hasContentId("herp derp")).thenReturn(true);

        MessageContent messageContent = buildMimeBodyPartsWith4Parts();

        assertThat(messageContent.findByContentId("herp derp")).isEqualTo(part3Mock);

    }

    @Test
    public void shouldNotFindBySequenceId() {

        MessageContent messageContent = buildMimeBodyPartsWith4Parts();
        assertThat(messageContent.findBySequenceId(555)).isNull();
    }

    @Test
    public void shouldFindBySequenceId() {

        when(part4Mock.getSequence()).thenReturn(54);

        MessageContent messageContent = buildMimeBodyPartsWith4Parts();

        assertThat(messageContent.findBySequenceId(54)).isEqualTo(part4Mock);

    }

    @Test
    public void shouldHaveToString() {

        when(part1Mock.toString()).thenReturn("mbp1");
        when(part2Mock.toString()).thenReturn("mbp2");
        when(part3Mock.toString()).thenReturn("mbp3");
        when(part4Mock.toString()).thenReturn("mbp4");

        MessageContent messageContent = buildMimeBodyPartsWith4Parts();

        assertThat(messageContent.toString()).isEqualTo("MessageContent[parts=[mbp1, mbp2, mbp3, mbp4]]");

    }

    @Test
    public void shouldGetSize() {

        MessageContent parts0 = new MessageContent();
        assertThat(parts0.size()).isEqualTo(0);

        MessageContent parts2 = new MessageContent();
        parts2.addPart(part1Mock);
        parts2.addPart(part4Mock);
        assertThat(parts2.size()).isEqualTo(2);

        MessageContent parts4 = buildMimeBodyPartsWith4Parts();
        assertThat(parts4.size()).isEqualTo(4);
    }

    @Test
    public void shouldHaveValidEqualsHashcode() {

        EqualsVerifier.forClass(MessageContent.class)
                      .suppress(Warning.NONFINAL_FIELDS,
                              Warning.STRICT_INHERITANCE)
                      .verify();

    }

    private MessageContent buildMimeBodyPartsWith4Parts() {

        MessageContent messageContent = new MessageContent();
        messageContent.addPart(part1Mock);
        messageContent.addPart(part2Mock);
        messageContent.addPart(part3Mock);
        messageContent.addPart(part4Mock);
        return messageContent;

    }
}
