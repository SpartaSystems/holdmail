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
import org.apache.commons.io.IOUtils;
import org.apache.james.mime4j.dom.field.FieldName;
import org.junit.Test;

import java.io.ByteArrayInputStream;

import static com.google.common.collect.ImmutableMap.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class MessageContentPartTest {

    private static final String CONTENT_TYPE = "Content-Type";

    private static final String CONTENT_ID = "Content-ID";

    private static final String CONTENT_DISPOSITION = "Content-Disposition";

    // "Association of sub-ordinate officials of the head office management of the Danube steamboat electrical services"
    private static final String NON_ASCII_STR = "Donaudampfschiffahrtselektrizitätenhauptbetriebswerkbauunterbeamtengesellschaft";
    private static final String NON_ASCII_STR_SHA256 = "29fa17ffd1ca2b8d34c4e3556b0cd537a194fcf53ef9633b8796c02aa2d01281";
    private static final int NON_ASCII_STR_LENGTH = 80;

    @Test
    public void shouldSetAndGetHeaders() {

        MessageContentPart messageContentPart = new MessageContentPart();
        assertThat(messageContentPart.getHeaders()).isEmpty();

        messageContentPart.setHeader("a", "aval");
        assertThat(messageContentPart.getHeaders()).isEqualTo(of("a", new HeaderValue("aval")));

        messageContentPart.setHeader("b", "bval");
        assertThat(messageContentPart.getHeaders()).isEqualTo(of("a", new HeaderValue("aval"),
                "b", new HeaderValue("bval")));

        messageContentPart.setHeader("b", "newBVal");
        assertThat(messageContentPart.getHeaders())
                .isEqualTo(of("a", new HeaderValue("aval"), "b", new HeaderValue("newBVal")));
    }

    @Test
    public void shouldGetContentType() {

        MessageContentPart messageContentPart = new MessageContentPart();
        assertThat(messageContentPart.getContentType()).isNull();

        messageContentPart.setHeader(CONTENT_TYPE, "magic-type");
        assertThat(messageContentPart.getContentType()).isEqualTo("magic-type");

    }

    @Test
    public void shouldGetIsHTML() {

        MessageContentPart messageContentPart = new MessageContentPart();
        assertThat(messageContentPart.isHTML()).isFalse();

        messageContentPart.setHeader(CONTENT_TYPE, "text/plain");
        assertThat(messageContentPart.isHTML()).isFalse();

        messageContentPart.setHeader(CONTENT_TYPE, "text/html");
        assertThat(messageContentPart.isHTML()).isTrue();
    }

    @Test
    public void shouldGetIsText() {

        MessageContentPart messageContentPart = new MessageContentPart();
        assertThat(messageContentPart.isText()).isFalse();

        messageContentPart.setHeader(CONTENT_TYPE, "text/html");
        assertThat(messageContentPart.isText()).isFalse();

        messageContentPart.setHeader(CONTENT_TYPE, "text/plain");
        assertThat(messageContentPart.isText()).isTrue();
    }

    @Test
    public void shouldHaveContentId() {

        MessageContentPart messageContentPart = new MessageContentPart();
        assertThat(messageContentPart.hasContentId("thing")).isFalse();

        messageContentPart.setHeader(CONTENT_ID, "");
        assertThat(messageContentPart.hasContentId("thing")).isFalse();

        messageContentPart.setHeader(CONTENT_ID, "gosh darnit bobby");

        assertThat(messageContentPart.hasContentId("propane")).isFalse();
        assertThat(messageContentPart.hasContentId("gosh")).isTrue();
        assertThat(messageContentPart.hasContentId("darnit")).isTrue();
        assertThat(messageContentPart.hasContentId("bobby")).isTrue();

    }

    @Test
    public void shouldGetContentString() throws Exception {

        MessageContentPart messageContentPart = new MessageContentPart();
        assertThat(messageContentPart.getContentString()).isNull();

        messageContentPart.setContent(new ByteArrayInputStream(NON_ASCII_STR.getBytes("UTF-8")));
        assertThat(messageContentPart.getContentString()).isEqualTo(NON_ASCII_STR);
    }

    @Test
    public void shouldGetContentStream() throws Exception {

        MessageContentPart messageContentPart = new MessageContentPart();
        assertThat(messageContentPart.getContentStream()).isNull();

        byte[] bytes = NON_ASCII_STR.getBytes("UTF-8");
        messageContentPart.setContent(new ByteArrayInputStream(bytes));
        assertThat(IOUtils.contentEquals(messageContentPart.getContentStream(), new ByteArrayInputStream(bytes)));

    }

    @Test
    public void shouldGetSHA256Sum() throws Exception {

        MessageContentPart messageContentPart = new MessageContentPart();
        assertThat(messageContentPart.getSHA256Sum()).isNull();

        messageContentPart.setContent(new ByteArrayInputStream(NON_ASCII_STR.getBytes("UTF-8")));
        assertThat(messageContentPart.getSHA256Sum()).isEqualTo(NON_ASCII_STR_SHA256);

    }

    @Test
    public void shouldGetSize() throws Exception {

        MessageContentPart part = new MessageContentPart();
        assertThat(part.getSize()).isEqualTo(0);

        byte[] bytes = NON_ASCII_STR.getBytes("UTF-8");
        part.setContent(new ByteArrayInputStream(bytes));
        assertThat(part.getSize()).isEqualTo(NON_ASCII_STR_LENGTH);
    }

    @Test
    public void shouldReturnIsAttachmentWhenDispositionIsInline() {

        MessageContentPart messageContentPart = new MessageContentPart();
        messageContentPart.setHeader(CONTENT_DISPOSITION, "inline");

        assertThat(messageContentPart.hasAttachmentDisposition(true)).isTrue();
        assertThat(messageContentPart.hasAttachmentDisposition(false)).isFalse();
    }

    @Test
    public void shouldReturnIsAttachmentWhenDispositionIsAttachment() {

        MessageContentPart messageContentPart = new MessageContentPart();
        messageContentPart.setHeader(CONTENT_DISPOSITION, "attachment");

        assertThat(messageContentPart.hasAttachmentDisposition(true)).isTrue();
        assertThat(messageContentPart.hasAttachmentDisposition(false)).isTrue();
    }

    @Test
    public void shouldReturnIsAttachmentFalseWhenDispositionAnythingElse() {

        // not set at all
        MessageContentPart messageContentPart = new MessageContentPart();
        messageContentPart.getHeaders().remove(CONTENT_DISPOSITION);
        assertThat(messageContentPart.hasAttachmentDisposition(true)).isFalse();

        // blank
        messageContentPart.setHeader(CONTENT_DISPOSITION, "");
        assertThat(messageContentPart.hasAttachmentDisposition(true)).isFalse();

        // garbage
        messageContentPart.setHeader(CONTENT_DISPOSITION, "poop");
        assertThat(messageContentPart.hasAttachmentDisposition(true)).isFalse();

        // case sensitive?
        messageContentPart.setHeader(CONTENT_DISPOSITION, "iNlINe");
        assertThat(messageContentPart.hasAttachmentDisposition(true)).isFalse();

        // valid, but doesn't start with
        messageContentPart.setHeader(CONTENT_DISPOSITION, "blah inline");
        assertThat(messageContentPart.hasAttachmentDisposition(true)).isFalse();

    }

    @Test
    public void shouldReturnNullAttachmentFilenameIfNotAttachment() {

        MessageContentPart partSpy = spy(new MessageContentPart());
        doReturn(false).when(partSpy).hasAttachmentDisposition(true);

        assertThat(partSpy.getAttachmentFilename()).isNull();
    }

    @Test
    public void shouldReturnAttachmentFilenameFromDisposition() {

        MessageContentPart part = new MessageContentPart();
        part.setHeader(FieldName.CONTENT_DISPOSITION, "inline; filename=myfile.pdf");
        part.setHeader(FieldName.CONTENT_TYPE, "text/blah; name=should-no-use-this.pdf");

        assertThat(part.getAttachmentFilename()).isEqualTo("myfile.pdf");

    }

    @Test
    public void shouldReturnAttachmentFilenameFromContentTypeFallback() {

        MessageContentPart part = new MessageContentPart();
        part.setHeader(FieldName.CONTENT_DISPOSITION, "inline; a=b;"); // no 'filename' param
        part.setHeader(FieldName.CONTENT_TYPE, "text/blah; name=ctype-fallback.pdf");

        assertThat(part.getAttachmentFilename()).isEqualTo("ctype-fallback.pdf");

    }

    @Test
    public void shouldReturnNullAttachmentFilenameFallback() {

        MessageContentPart part = new MessageContentPart();
        part.setHeader(FieldName.CONTENT_DISPOSITION, "inline; a=b;"); // no 'filename' param
        part.setHeader(FieldName.CONTENT_TYPE, "text/blah; c=d;"); // no 'name' param

        assertThat(part.getAttachmentFilename()).isNull();
    }

    @Test
    public void shouldHaveValidEqualsHashcode() {

        EqualsVerifier.forClass(MessageContentPart.class)
                .suppress(Warning.NONFINAL_FIELDS,
                        Warning.NULL_FIELDS,
                        Warning.STRICT_INHERITANCE)
                .verify();

    }

}
