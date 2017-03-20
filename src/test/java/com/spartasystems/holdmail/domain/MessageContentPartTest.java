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
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.io.ByteArrayInputStream;

import static com.google.common.collect.ImmutableMap.of;
import static org.assertj.core.api.Assertions.assertThat;

public class MessageContentPartTest {

    public static final String CONTENT_TYPE = "Content-Type";

    public static final String CONTENT_ID = "Content-ID";

    // "Association of sub-ordinate officials of the head office management of the Danube steamboat electrical services"
    public static final String NON_ASCII_STR = "Donaudampfschiffahrtselektrizitätenhauptbetriebswerkbauunterbeamtengesellschaft";

    @Test
    public void shouldSetAndGetHeaders() throws Exception {

        MessageContentPart messageContentPart = new MessageContentPart();
        assertThat(messageContentPart.getHeaders()).isEmpty();

        messageContentPart.setHeader("a", "aval");
        assertThat(messageContentPart.getHeaders()).isEqualTo(of("a", "aval"));

        messageContentPart.setHeader("b", "bval");
        assertThat(messageContentPart.getHeaders()).isEqualTo(of("a", "aval", "b", "bval"));

        messageContentPart.setHeader("b", "newBVal");
        assertThat(messageContentPart.getHeaders()).isEqualTo(of("a", "aval", "b", "newBVal"));
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
    public void shouldHaveToString() throws Exception {

        MessageContentPart messageContentPart = new MessageContentPart();
        assertThat(messageContentPart.toString()).isEqualTo("BodyPart[headers={}, content=null]");

        messageContentPart.setContent(new ByteArrayInputStream("hello".getBytes()));
        messageContentPart.setHeader("k", "v");

        assertThat(messageContentPart.toString()).isEqualTo("BodyPart[headers={k=v}, content=5b]");
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
