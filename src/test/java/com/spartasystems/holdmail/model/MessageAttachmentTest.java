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

package com.spartasystems.holdmail.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageAttachmentTest {

    @Test
    public void shouldHaveValidEqualsHashcode() {

        EqualsVerifier.forClass(MessageAttachment.class)
                      .suppress(Warning.STRICT_INHERITANCE)
                      .verify();
    }

    @Test
    public void shouldHaveValidConstructor() {

        MessageAttachment attach = new MessageAttachment("myAttachId", "myfile", "mytype", "mydispos", "mySHA256", 555);
        assertThat(attach.getAttachmentId()).isEqualTo("myAttachId");
        assertThat(attach.getFilename()).isEqualTo("myfile");
        assertThat(attach.getContentType()).isEqualTo("mytype");
        assertThat(attach.getDisposition()).isEqualTo("mydispos");
        assertThat(attach.getSha256Sum()).isEqualTo("mySHA256");
        assertThat(attach.getSize()).isEqualTo(555);

    }

}
