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

package com.spartasystems.holdmail.mime;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.io.ByteArrayInputStream;

import static com.google.common.collect.ImmutableMap.of;
import static org.assertj.core.api.Assertions.assertThat;

public class MimeBodyPartTest {

    public static final String CONTENT_TYPE = "Content-Type";

    public static final String CONTENT_ID = "Content-ID";

    // "Association of sub-ordinate officials of the head office management of the Danube steamboat electrical services"
    public static final String NON_ASCII_STR = "Donaudampfschiffahrtselektrizitätenhauptbetriebswerkbauunterbeamtengesellschaft";

    @Test
    public void shouldSetAndGetHeaders() throws Exception {

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        assertThat(mimeBodyPart.getHeaders()).isEmpty();

        mimeBodyPart.setHeader("a", "aval");
        assertThat(mimeBodyPart.getHeaders()).isEqualTo(of("a", "aval"));

        mimeBodyPart.setHeader("b", "bval");
        assertThat(mimeBodyPart.getHeaders()).isEqualTo(of("a", "aval", "b", "bval"));

        mimeBodyPart.setHeader("b", "newBVal");
        assertThat(mimeBodyPart.getHeaders()).isEqualTo(of("a", "aval", "b", "newBVal"));
    }

    @Test
    public void shouldGetContentType() throws Exception {

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        assertThat(mimeBodyPart.getContentType()).isNull();

        mimeBodyPart.setHeader(CONTENT_TYPE, "magic-type");
        assertThat(mimeBodyPart.getContentType()).isEqualTo("magic-type");

    }

    @Test
    public void shouldGetIsHTML() throws Exception {

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        assertThat(mimeBodyPart.isHTML()).isFalse();

        mimeBodyPart.setHeader(CONTENT_TYPE, "text/plain");
        assertThat(mimeBodyPart.isHTML()).isFalse();

        mimeBodyPart.setHeader(CONTENT_TYPE, "text/html");
        assertThat(mimeBodyPart.isHTML()).isTrue();
    }

    @Test
    public void shouldGetIsText() throws Exception {

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        assertThat(mimeBodyPart.isText()).isFalse();

        mimeBodyPart.setHeader(CONTENT_TYPE, "text/html");
        assertThat(mimeBodyPart.isText()).isFalse();

        mimeBodyPart.setHeader(CONTENT_TYPE, "text/plain");
        assertThat(mimeBodyPart.isText()).isTrue();
    }

    @Test
    public void shouldHaveContentId() throws Exception {

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        assertThat(mimeBodyPart.hasContentId("thing")).isFalse();

        mimeBodyPart.setHeader(CONTENT_ID, "");
        assertThat(mimeBodyPart.hasContentId("thing")).isFalse();

        mimeBodyPart.setHeader(CONTENT_ID, "gosh darnit bobby");

        assertThat(mimeBodyPart.hasContentId("propane")).isFalse();
        assertThat(mimeBodyPart.hasContentId("gosh")).isTrue();
        assertThat(mimeBodyPart.hasContentId("darnit")).isTrue();
        assertThat(mimeBodyPart.hasContentId("bobby")).isTrue();

    }

    @Test
    public void shouldSetContent() throws Exception {

        byte[] input = NON_ASCII_STR.getBytes();

        MimeBodyPart mimeBodyPart = new MimeBodyPart();

        mimeBodyPart.setContent(new ByteArrayInputStream(input));

        byte[] actual = Whitebox.getInternalState(mimeBodyPart, "content");
        assertThat(actual).isEqualTo(input);

    }

    @Test
    public void shouldGetContentString() throws Exception {

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        assertThat(mimeBodyPart.getContentString()).isNull();

        mimeBodyPart.setContent(new ByteArrayInputStream(NON_ASCII_STR.getBytes("UTF-8")));
        assertThat(mimeBodyPart.getContentString()).isEqualTo(NON_ASCII_STR);
    }

    @Test
    public void shouldGetContentStream() throws Exception {

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        assertThat(mimeBodyPart.getContentStream()).isNull();

        byte[] bytes = NON_ASCII_STR.getBytes("UTF-8");
        mimeBodyPart.setContent(new ByteArrayInputStream(bytes));
        assertThat(IOUtils.contentEquals(mimeBodyPart.getContentStream(), new ByteArrayInputStream(bytes)));

    }

    @Test
    public void shouldHaveToString() throws Exception {

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        assertThat(mimeBodyPart.toString()).isEqualTo("BodyPart[headers={}, content=null]");

        mimeBodyPart.setContent(new ByteArrayInputStream("hello".getBytes()));
        mimeBodyPart.setHeader("k", "v");

        assertThat(mimeBodyPart.toString()).isEqualTo("BodyPart[headers={k=v}, content=5b]");
    }

    @Test
    public void shouldHaveValidEqualsHashcode() throws Exception {

        EqualsVerifier.forClass(MimeBodyPart.class)
                      .suppress(Warning.NONFINAL_FIELDS,
                                Warning.NULL_FIELDS,
                                Warning.STRICT_INHERITANCE)
                      .verify();

    }



}
