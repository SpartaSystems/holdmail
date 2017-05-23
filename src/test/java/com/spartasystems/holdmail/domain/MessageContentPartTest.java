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

package com.spartasystems.holdmail.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.apache.commons.io.IOUtils;
import org.apache.james.mime4j.dom.field.FieldName;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.io.ByteArrayInputStream;

import static com.google.common.collect.ImmutableMap.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class MessageContentPartTest {

    public static final String CONTENT_TYPE = "Content-Type";

    public static final String CONTENT_ID = "Content-ID";

    public static final String CONTENT_DISPOSITION = "Content-Disposition";

    // "Association of sub-ordinate officials of the head office management of the Danube steamboat electrical services"
    public static final String NON_ASCII_STR = "Donaudampfschiffahrtselektrizitätenhauptbetriebswerkbauunterbeamtengesellschaft";

    @Test
    public void shouldSetAndGetHeaders() throws Exception {

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
    public void shouldGetContentType() throws Exception {

        MessageContentPart messageContentPart = new MessageContentPart();
        assertThat(messageContentPart.getContentType()).isNull();

        messageContentPart.setHeader(CONTENT_TYPE, "magic-type");
        assertThat(messageContentPart.getContentType()).isEqualTo("magic-type");

    }

    @Test
    public void shouldGetIsHTML() throws Exception {

        MessageContentPart messageContentPart = new MessageContentPart();
        assertThat(messageContentPart.isHTML()).isFalse();

        messageContentPart.setHeader(CONTENT_TYPE, "text/plain");
        assertThat(messageContentPart.isHTML()).isFalse();

        messageContentPart.setHeader(CONTENT_TYPE, "text/html");
        assertThat(messageContentPart.isHTML()).isTrue();
    }

    @Test
    public void shouldGetIsText() throws Exception {

        MessageContentPart messageContentPart = new MessageContentPart();
        assertThat(messageContentPart.isText()).isFalse();

        messageContentPart.setHeader(CONTENT_TYPE, "text/html");
        assertThat(messageContentPart.isText()).isFalse();

        messageContentPart.setHeader(CONTENT_TYPE, "text/plain");
        assertThat(messageContentPart.isText()).isTrue();
    }

    @Test
    public void shouldHaveContentId() throws Exception {

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
    public void shouldSetContent() throws Exception {

        byte[] input = NON_ASCII_STR.getBytes();

        MessageContentPart messageContentPart = new MessageContentPart();

        messageContentPart.setContent(new ByteArrayInputStream(input));

        byte[] actual = Whitebox.getInternalState(messageContentPart, "content");
        assertThat(actual).isEqualTo(input);

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
    public void shouldGetSize() throws Exception {

        MessageContentPart part = new MessageContentPart();
        assertThat(part.getSize()).isEqualTo(0);

        byte[] bytes = NON_ASCII_STR.getBytes("UTF-8");
        part.setContent(new ByteArrayInputStream(bytes));
        assertThat(part.getSize()).isEqualTo(bytes.length);
    }

    @Test
    public void shouldReturnIsAttachmentTrueWhenDispositionIsInline() throws Exception {

        MessageContentPart messageContentPart = new MessageContentPart();
        messageContentPart.setHeader(CONTENT_DISPOSITION, "inline");

        assertThat(messageContentPart.isAttachment()).isTrue();
    }

    @Test
    public void shouldReturnIsAttachmentTrueWhenDispositionIsAttachment() throws Exception {

        MessageContentPart messageContentPart = new MessageContentPart();
        messageContentPart.setHeader(CONTENT_DISPOSITION, "attachment");

        assertThat(messageContentPart.isAttachment()).isTrue();
    }

    @Test
    public void shouldReturnIsAttachmentFalseWhenDispositionAnythingElse() throws Exception {

        // not set at all
        MessageContentPart messageContentPart = new MessageContentPart();
        messageContentPart.getHeaders().remove(CONTENT_DISPOSITION);
        assertThat(messageContentPart.isAttachment()).isFalse();

        // blank
        messageContentPart.setHeader(CONTENT_DISPOSITION, "");
        assertThat(messageContentPart.isAttachment()).isFalse();

        // garbage
        messageContentPart.setHeader(CONTENT_DISPOSITION, "poop");
        assertThat(messageContentPart.isAttachment()).isFalse();

        // case sensitive?
        messageContentPart.setHeader(CONTENT_DISPOSITION, "iNlINe");
        assertThat(messageContentPart.isAttachment()).isFalse();

        // valid, but doesn't start with
        messageContentPart.setHeader(CONTENT_DISPOSITION, "blah inline");
        assertThat(messageContentPart.isAttachment()).isFalse();

    }

    @Test
    public void shouldReturnNullAttachmentFilenameIfNotAttachment() throws Exception {

        MessageContentPart partSpy = spy(new MessageContentPart());
        doReturn(false).when(partSpy).isAttachment();

        assertThat(partSpy.getAttachmentFilename()).isNull();
    }

    @Test
    public void shouldReturnAttachmentFilenameFromDisposition() throws Exception {

        MessageContentPart part = new MessageContentPart();
        part.setHeader(FieldName.CONTENT_DISPOSITION, "inline; filename=myfile.pdf");
        part.setHeader(FieldName.CONTENT_TYPE, "text/blah; name=should-no-use-this.pdf");

        assertThat(part.getAttachmentFilename()).isEqualTo("myfile.pdf");

    }

    @Test
    public void shouldReturnAttachmentFilenameFromContentTypeFallback() throws Exception {

        MessageContentPart part = new MessageContentPart();
        part.setHeader(FieldName.CONTENT_DISPOSITION, "inline; a=b;"); // no 'filename' param
        part.setHeader(FieldName.CONTENT_TYPE, "text/blah; name=ctype-fallback.pdf");

        assertThat(part.getAttachmentFilename()).isEqualTo("ctype-fallback.pdf");

    }

    @Test
    public void shouldReturnNullAttachmentFilenameFallback() throws Exception {

        MessageContentPart part = new MessageContentPart();
        part.setHeader(FieldName.CONTENT_DISPOSITION, "inline; a=b;"); // no 'filename' param
        part.setHeader(FieldName.CONTENT_TYPE, "text/blah; c=d;"); // no 'name' param

        assertThat(part.getAttachmentFilename()).isNull();
    }

    @Test
    public void shouldHaveToString() throws Exception {

        MessageContentPart messageContentPart = new MessageContentPart();
        messageContentPart.setSequence(9);

        assertThat(messageContentPart.toString()).isEqualTo("MessageContentPart[headers={}, sequence=9, content=null]");

        messageContentPart.setContent(new ByteArrayInputStream("hello".getBytes()));
        messageContentPart.setHeader("k", "v");

        String hVal = messageContentPart.getHeaders().get("k").toString();

        assertThat(messageContentPart.toString()).isEqualTo("MessageContentPart[headers={k=" + hVal
                + "}, sequence=9, content=5 bytes]");
    }

    @Test
    public void shouldHaveValidEqualsHashcode() throws Exception {

        EqualsVerifier.forClass(MessageContentPart.class)
                      .suppress(Warning.NONFINAL_FIELDS,
                              Warning.NULL_FIELDS,
                              Warning.STRICT_INHERITANCE)
                      .verify();

    }

}
