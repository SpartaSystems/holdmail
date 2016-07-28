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

import org.apache.commons.io.IOUtils;
import org.apache.james.mime4j.stream.Field;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class MimeBodyPartsExtractorTest {

    @Test
    public void shouldInitializeOnConstructor() throws Exception {

        MimeBodyPartsExtractor extractor = new MimeBodyPartsExtractor();

        assertThat(extractor.getParts()).isEqualTo(new MimeBodyParts());
        assertThat(extractor.getNextPotentialPart()).isNull();
    }

    @Test
    public void shouldStartHeader() throws Exception{

        MimeBodyPartsExtractor extractor = new MimeBodyPartsExtractor();
        assertThat(extractor.getNextPotentialPart()).isNull();

        extractor.startHeader();
        assertThat(extractor.getNextPotentialPart()).isEqualTo(new MimeBodyPart());

    }

    @Test
    public void shouldNotHandleFieldIfNoPart() throws Exception{

        MimeBodyPartsExtractor extractor = new MimeBodyPartsExtractor();

        extractor.field(mock(Field.class));
        assertThat(extractor.getNextPotentialPart()).isNull();

    }

    @Test
    public void shouldHandleField() throws Exception{

        Field fieldMock = mock(Field.class);
        when(fieldMock.getName()).thenReturn("fName");
        when(fieldMock.getBody()).thenReturn("fBody");

        MimeBodyPartsExtractor extractor = new MimeBodyPartsExtractor();

        extractor.startHeader(); // initialize a 'nextPotentialPart'
        extractor.field(fieldMock);

        MimeBodyPart nextPart = extractor.getNextPotentialPart();
        assertThat(nextPart.getHeaders().get("fName")).isEqualTo("fBody");

    }

    @Test
    public void shouldHandleBody() throws Exception {

        InputStream inputStreamMock = new ByteArrayInputStream("hello".getBytes());

        MimeBodyPartsExtractor extractor = new MimeBodyPartsExtractor();

        extractor.startHeader(); // initialize a 'nextPotentialPart'
        extractor.body(null, inputStreamMock);

        MimeBodyPart nextPart = extractor.getNextPotentialPart();
        assertThat(IOUtils.contentEquals(nextPart.getContentStream(), inputStreamMock));

        MimeBodyParts expectedParts = new MimeBodyParts();
        expectedParts.addBodyPart(nextPart);

        assertThat(extractor.getParts()).isEqualTo(expectedParts);
    }
}
