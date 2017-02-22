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

import com.spartasystems.holdmail.mime.MimeUtils;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MimeUtils.class)
public class MessageTest {

    @Captor
    private ArgumentCaptor<InputStream> streamCaptor;

    private static final String HEXCHAR     = "[0-9a-fA-F]";
    private static final String UUID_PATT   = String.format("^%s{8}\\-%s{4}\\-%s{4}\\-%s{4}\\-%s{12}$",
            HEXCHAR, HEXCHAR, HEXCHAR, HEXCHAR, HEXCHAR);
    public static final  String RAW_CONTENT = "someRawContent";

    @Before
    public void setUp() throws Exception {
        mockStatic(MimeUtils.class);
    }

    @Test
    public void shouldSetIdentifierOnConstructor() throws Exception {

        Message message = buildMessage("derpId", "");
        assertThat(message.getIdentifier()).isEqualTo("derpId");

    }

    @Test
    public void shouldSetRandomIdentifierOnBlankIdentifier() {

        List<String> seen = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Message message = buildMessage(null, "");
            assertThat(seen).doesNotContain(message.getIdentifier());
            assertThat(message.getIdentifier()).matches(UUID_PATT);
            seen.add(message.getIdentifier());
        }

    }

    @Test
    public void shouldParseContent() throws Exception {

        MessageContent expected = mock(MessageContent.class);
        when(MimeUtils.parseMessageContent(streamCaptor.capture())).thenReturn(expected);

        Message message = buildMessage("someId", RAW_CONTENT);

        assertThat(message.getContent()).isEqualTo(expected);

        assertThat(IOUtils.toString(streamCaptor.getValue(), UTF_8)).isEqualTo(RAW_CONTENT);

    }

    private Message buildMessage(String identifier, String rawMessage) {
        return new Message(0, identifier, "derpSubject", "derp@localhost.com",
                new Date(), "localhost", 4, rawMessage, emptyList(), new MessageHeaders());
    }

    @Test
    public void shouldVerifyEqualContract() {
        EqualsVerifier.forClass(Message.class)
                      .suppress(Warning.NONFINAL_FIELDS, Warning.NULL_FIELDS, Warning.STRICT_INHERITANCE)
                      .verify();
    }

}
