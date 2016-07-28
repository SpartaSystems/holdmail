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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MimeBodyPartsTest {

    @Mock
    private MimeBodyPart mimeBodyPartMock1;

    @Mock
    private MimeBodyPart mimeBodyPartMock2;

    @Mock
    private MimeBodyPart mimeBodyPartMock3;

    @Mock
    private MimeBodyPart mimeBodyPartMock4;

    @Test
    public void shouldHaveNoPartsOnConstruction() throws Exception {

        MimeBodyParts mimeBodyParts = new MimeBodyParts();

        List<MimeBodyPart> parts = Whitebox.getInternalState(mimeBodyParts, "bodyPartList");
        assertThat(parts).isEmpty();

    }

    @Test
    public void shouldAddParts() throws Exception {

        MimeBodyParts mimeBodyParts = new MimeBodyParts();
        mimeBodyParts.addBodyPart(mimeBodyPartMock1);
        mimeBodyParts.addBodyPart(mimeBodyPartMock2);

        List<MimeBodyPart> parts = Whitebox.getInternalState(mimeBodyParts, "bodyPartList");
        assertThat(parts).isEqualTo(asList(mimeBodyPartMock1, mimeBodyPartMock2));
    }

    @Test
    public void shouldNotFindFirstHTMLBody() throws Exception {

        MimeBodyParts mimeBodyParts = buildMimeBodyPartsWith4Parts();
        assertThat(mimeBodyParts.findFirstHTMLBody()).isNull();
    }

    @Test
    public void shouldFindFirstHTMLBody() throws Exception {

        when(mimeBodyPartMock2.isHTML()).thenReturn(true);

        MimeBodyParts mimeBodyParts = buildMimeBodyPartsWith4Parts();

        assertThat(mimeBodyParts.findFirstHTMLBody()).isEqualTo(mimeBodyPartMock2);

    }

    @Test
    public void shouldNotFindFirstTextBody() throws Exception {

        MimeBodyParts mimeBodyParts = buildMimeBodyPartsWith4Parts();
        assertThat(mimeBodyParts.findFirstTextBody()).isNull();
    }

    @Test
    public void shouldFindFirstTextBody() throws Exception {

        when(mimeBodyPartMock4.isText()).thenReturn(true);

        MimeBodyParts mimeBodyParts = buildMimeBodyPartsWith4Parts();

        assertThat(mimeBodyParts.findFirstTextBody()).isEqualTo(mimeBodyPartMock4);

    }

    @Test
    public void shouldNotFindByContentId() throws Exception {

        MimeBodyParts mimeBodyParts = buildMimeBodyPartsWith4Parts();
        assertThat(mimeBodyParts.findByContentId("blah")).isNull();
    }

    @Test
    public void shouldFindByContentId() throws Exception {

        when(mimeBodyPartMock3.hasContentId("herp derp")).thenReturn(true);

        MimeBodyParts mimeBodyParts = buildMimeBodyPartsWith4Parts();

        assertThat(mimeBodyParts.findByContentId("herp derp")).isEqualTo(mimeBodyPartMock3);

    }

    @Test
    public void shouldHaveToString() throws Exception {

        when(mimeBodyPartMock1.toString()).thenReturn("mbp1");
        when(mimeBodyPartMock2.toString()).thenReturn("mbp2");
        when(mimeBodyPartMock3.toString()).thenReturn("mbp3");
        when(mimeBodyPartMock4.toString()).thenReturn("mbp4");

        MimeBodyParts mimeBodyParts = buildMimeBodyPartsWith4Parts();

        assertThat(mimeBodyParts.toString()).isEqualTo("MimeBodyParts[bodyPartList=[mbp1, mbp2, mbp3, mbp4]]");

    }

    @Test
    public void shouldHaveValidEqualsHashcode() throws Exception {

        EqualsVerifier.forClass(MimeBodyParts.class)
                      .suppress(Warning.NONFINAL_FIELDS,
                              Warning.STRICT_INHERITANCE)
                      .verify();

    }

    private MimeBodyParts buildMimeBodyPartsWith4Parts() {

        MimeBodyParts mimeBodyParts = new MimeBodyParts();
        mimeBodyParts.addBodyPart(mimeBodyPartMock1);
        mimeBodyParts.addBodyPart(mimeBodyPartMock2);
        mimeBodyParts.addBodyPart(mimeBodyPartMock3);
        mimeBodyParts.addBodyPart(mimeBodyPartMock4);
        return mimeBodyParts;

    }
}
