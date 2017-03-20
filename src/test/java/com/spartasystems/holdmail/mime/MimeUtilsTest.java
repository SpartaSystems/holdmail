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
    public void shouldFindAllBodyParts() throws Exception {

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
    public void shouldRethrowMimeException() throws Exception {

        MimeException mimeException = new MimeException("mimeError");
        whenNew(MessageContentExtractor.class).withNoArguments()
                                              .thenThrow(mimeException);

        assertThatThrownBy(() -> MimeUtils.parseMessageContent(inputStreamMock))
                .isInstanceOf(HoldMailException.class)
                .hasMessage("Failed to parse body")
                .hasCause(mimeException);

    }

    @Test
    public void shouldRethrowIOException() throws Exception {

        IOException ioException = new IOException("ioError");
        whenNew(MessageContentExtractor.class).withNoArguments()
                                              .thenThrow(ioException);

        assertThatThrownBy(() -> MimeUtils.parseMessageContent(inputStreamMock))
                .isInstanceOf(HoldMailException.class)
                .hasMessage("Failed to parse body")
                .hasCause(ioException);

    }

}
