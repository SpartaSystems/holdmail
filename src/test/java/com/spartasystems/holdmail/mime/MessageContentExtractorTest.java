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

package com.spartasystems.holdmail.mime;

import com.spartasystems.holdmail.domain.HeaderValue;
import com.spartasystems.holdmail.domain.MessageContent;
import com.spartasystems.holdmail.domain.MessageContentPart;
import org.apache.commons.io.IOUtils;
import org.apache.james.mime4j.stream.BodyDescriptor;
import org.apache.james.mime4j.stream.Field;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class MessageContentExtractorTest {

    @Test
    public void shouldInitializeOnConstructor() {

        MessageContentExtractor extractor = new MessageContentExtractor();

        assertThat(extractor.getParts()).isEqualTo(new MessageContent());
        assertThat(extractor.getNextPotentialPart()).isNull();
    }

    @Test
    public void shouldStartHeader() {

        MessageContentExtractor extractor = new MessageContentExtractor();
        assertThat(extractor.getNextPotentialPart()).isNull();

        extractor.startHeader();
        MessageContentPart expected = new MessageContentPart();
        expected.setSequence(1);
        assertThat(extractor.getNextPotentialPart()).isEqualTo(expected);

    }

    @Test
    public void shouldNotHandleFieldIfNoPart() {

        MessageContentExtractor extractor = new MessageContentExtractor();

        extractor.field(mock(Field.class));
        assertThat(extractor.getNextPotentialPart()).isNull();

    }

    @Test
    public void shouldHandleField() {

        Field fieldMock = mock(Field.class);
        when(fieldMock.getName()).thenReturn("fName");
        when(fieldMock.getBody()).thenReturn("fBody");

        MessageContentExtractor extractor = new MessageContentExtractor();

        extractor.startHeader(); // initialize a 'nextPotentialPart'
        extractor.field(fieldMock);

        MessageContentPart nextPart = extractor.getNextPotentialPart();
        assertThat(nextPart.getHeaders().get("fName")).isEqualTo(new HeaderValue("fBody"));

    }

    @Test
    public void shouldHandleBody() throws Exception {

        InputStream inputStreamMock = new ByteArrayInputStream("hello".getBytes());

        MessageContentExtractor extractor = new MessageContentExtractor();

        extractor.startHeader(); // initialize a 'nextPotentialPart'
        extractor.body(null, inputStreamMock);

        MessageContentPart nextPart = extractor.getNextPotentialPart();
        assertThat(IOUtils.contentEquals(nextPart.getContentStream(), inputStreamMock));

        MessageContent expectedParts = new MessageContent();
        expectedParts.addPart(nextPart);

        assertThat(extractor.getParts()).isEqualTo(expectedParts);
    }

    @Test
    public void shouldSetSequenceWithEachNewPart() throws Exception{

        BodyDescriptor bodyMock = mock(BodyDescriptor.class);
        ByteArrayInputStream streamMock = new ByteArrayInputStream(new byte[] {});

        MessageContentExtractor extractor = new MessageContentExtractor();

        extractor.startHeader();
        extractor.body(bodyMock, streamMock);
        assertThat(getCurrentSequence(extractor)).containsExactly(1);

        extractor.startHeader();
        extractor.body(bodyMock, streamMock);
        assertThat(getCurrentSequence(extractor)).containsExactly(1, 2);

        extractor.startHeader();
        extractor.body(bodyMock, streamMock);
        assertThat(getCurrentSequence(extractor)).containsExactly(1, 2, 3);
    }

    private List<Integer> getCurrentSequence(MessageContentExtractor extractor) {
        return extractor.getParts().getParts()
                        .stream()
                        .map(MessageContentPart::getSequence)
                        .collect(Collectors.toList());
    }
}
