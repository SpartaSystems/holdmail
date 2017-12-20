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

import com.spartasystems.holdmail.domain.MessageContent;
import com.spartasystems.holdmail.exception.HoldMailException;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.parser.MimeStreamParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.omg.CORBA.portable.InputStream;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ MimeUtils.class })
public class MimeUtilsTest {

    @Mock
    private InputStream inputStreamMock;

    @Mock
    private MimeStreamParser mimeStreamParserMock;

    @Mock
    private MessageContentExtractor messageContentExtractorMock;

    @Test
    public void shouldFindAllBodyPartsOnParse() throws Exception {

        MessageContent expectedParts = mock(MessageContent.class);
        whenNew(MimeStreamParser.class).withAnyArguments().thenReturn(mimeStreamParserMock);
        whenNew(MessageContentExtractor.class).withNoArguments().thenReturn(messageContentExtractorMock);
        when(messageContentExtractorMock.getParts()).thenReturn(expectedParts);

        MessageContent actualParts = MimeUtils.parseMessageContent(inputStreamMock);

        InOrder inOrder = inOrder(mimeStreamParserMock);
        inOrder.verify(mimeStreamParserMock).setContentDecoding(true);
        inOrder.verify(mimeStreamParserMock).setContentHandler(messageContentExtractorMock);
        inOrder.verify(mimeStreamParserMock).parse(inputStreamMock);
        assertThat(actualParts).isEqualTo(expectedParts);
    }

    @Test
    public void shouldRethrowMimeExceptionOnParse() throws Exception {

        MimeException mimeException = new MimeException("mimeError");
        whenNew(MessageContentExtractor.class).withNoArguments()
                                              .thenThrow(mimeException);

        assertThatThrownBy(() -> MimeUtils.parseMessageContent(inputStreamMock))
                .isInstanceOf(HoldMailException.class)
                .hasMessage("Failed to parse body")
                .hasCause(mimeException);

    }

    @Test
    public void shouldRethrowIOExceptionOnParse() throws Exception {

        IOException ioException = new IOException("ioError");
        whenNew(MessageContentExtractor.class).withNoArguments()
                                              .thenThrow(ioException);

        assertThatThrownBy(() -> MimeUtils.parseMessageContent(inputStreamMock))
                .isInstanceOf(HoldMailException.class)
                .hasMessage("Failed to parse body")
                .hasCause(ioException);

    }

    @Test
    public void shouldTrimAndUnquote() {

        assertThat(MimeUtils.trimQuotes(null)).isEqualTo(null);
        assertThat(MimeUtils.trimQuotes("")).isEqualTo("");
        assertThat(MimeUtils.trimQuotes("blah")).isEqualTo("blah");
        assertThat(MimeUtils.trimQuotes(" \"blah\" ")).isEqualTo("blah");
        assertThat(MimeUtils.trimQuotes(" \" blah \" ")).isEqualTo(" blah ");
        assertThat(MimeUtils.trimQuotes(" \" blah blah \" ")).isEqualTo(" blah blah ");

    }

    @Test
    public void shouldSafeDecode() {

        assertThat(MimeUtils.safeURLDecode(null, "enc")).isEqualTo(null);
        assertThat(MimeUtils.safeURLDecode("", "enc")).isEqualTo("");
        assertThat(MimeUtils.safeURLDecode("val", null)).isEqualTo("val");
        assertThat(MimeUtils.safeURLDecode("val", "")).isEqualTo("val");

        String helloWorldChinese = "你好世界";
        String helloWorldChineseEncodedUTF8 = "%E4%BD%A0%E5%A5%BD%E4%B8%96%E7%95%8C";

        assertThat(MimeUtils.safeURLDecode(helloWorldChineseEncodedUTF8, "BADENCONDING"))
                .isEqualTo(helloWorldChineseEncodedUTF8);

        assertThat(MimeUtils.safeURLDecode(helloWorldChineseEncodedUTF8, "UTF-8"))
                .isEqualTo(helloWorldChinese);


    }

}
