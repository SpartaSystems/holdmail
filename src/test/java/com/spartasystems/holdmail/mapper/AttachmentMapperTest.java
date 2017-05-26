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

package com.spartasystems.holdmail.mapper;

import com.spartasystems.holdmail.domain.MessageContentPart;
import com.spartasystems.holdmail.model.MessageAttachment;
import org.apache.james.mime4j.dom.field.FieldName;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class AttachmentMapperTest {

    private AttachmentMapper attachmentMapper;

    @Before
    public void setUp() throws Exception {
        attachmentMapper = new AttachmentMapper();
    }

    @Test
    public void shouldMapFromContentPart() throws Exception {

        MessageContentPart partSpy = spy(new MessageContentPart());
        partSpy.setHeader(FieldName.CONTENT_DISPOSITION, "my-disposition; a=b; c=d;");
        doReturn("my-file").when(partSpy).getAttachmentFilename();
        doReturn("my-mime").when(partSpy).getContentType();
        doReturn(333).when(partSpy).getSize();

        MessageAttachment expected = new MessageAttachment(0, "my-file", "my-mime", "my-disposition", 333);

        assertThat(attachmentMapper.fromMessageContentPart(partSpy)).isEqualTo(expected);

    }

}
