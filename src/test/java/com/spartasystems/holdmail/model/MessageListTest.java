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

package com.spartasystems.holdmail.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class MessageListTest {

    public static final List<MessageListItem> TWO_ITEM_LIST =
            asList(mock(MessageListItem.class), mock(MessageListItem.class));

    @Test
    public void shouldInitWithItems() throws Exception {

        MessageList messageList = new MessageList(TWO_ITEM_LIST);

        assertThat(messageList.getMessages()).isEqualTo(TWO_ITEM_LIST);

    }

    @Test
    public void shouldSetAndGetMessages() throws Exception {

        MessageList messageList = new MessageList(TWO_ITEM_LIST);

        List<MessageListItem> replacementList = singletonList(mock(MessageListItem.class));
        messageList.setMessages(replacementList);

        assertThat(messageList.getMessages()).isEqualTo(replacementList);

    }

    @Test
    public void shouldHaveValidEqualsHashcode() throws Exception {

        EqualsVerifier.forClass(MessageList.class)
                      .suppress(Warning.NONFINAL_FIELDS,
                              Warning.STRICT_INHERITANCE)
                      .verify();

    }
}
