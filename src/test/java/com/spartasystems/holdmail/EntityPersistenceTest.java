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

package com.spartasystems.holdmail;

import com.google.common.collect.ImmutableSet;
import com.spartasystems.holdmail.persistence.MessageEntity;
import com.spartasystems.holdmail.persistence.MessageHeaderEntity;
import com.spartasystems.holdmail.persistence.MessageRecipientEntity;
import com.spartasystems.holdmail.persistence.MessageRepository;
import org.hibernate.AssertionFailure;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.transaction.Transactional;
import java.util.Date;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
//@TransactionConfiguration(defaultRollback = false)
public class EntityPersistenceTest extends BaseIntegrationTest {

    private static final String MESSAGE_ID = "derpID";
    private static final int    MESSAGE_SIZE  = 1234;
    private static final String MESSAGE_BODY = "herp derp this is the text body";
    private static final String SENDER_EMAIL  = "sender@domain.com";
    private static final String SENDER_HOST   = "senderHostTest";
    private static final String SUBJECT       = "this is the subject";
    private static final Date   RECEIVED_DATE = new Date();

    @Autowired
    private MessageRepository messageRepository;

    @Test
    public void shouldSaveEmail() throws Exception {

        MessageEntity entity = buildBasicEntity();

        Long savedEntityId = messageRepository.save(entity).getMessageId();
        assertThat(savedEntityId).isGreaterThan(0);

        MessageEntity loaded = messageRepository.findOne(savedEntityId);
        assertThat(loaded).isNotNull();
        assertThat(loaded.getIdentifier()).isEqualTo(MESSAGE_ID);
        assertThat(loaded.getMessageSize()).isEqualTo(MESSAGE_SIZE);
        assertThat(loaded.getMessageBody()).isEqualTo(MESSAGE_BODY);
        assertThat(loaded.getReceivedDate()).hasSameTimeAs(RECEIVED_DATE);
        assertThat(loaded.getSenderEmail()).isEqualTo(SENDER_EMAIL);
        assertThat(loaded.getSenderHost()).isEqualTo(SENDER_HOST);
        assertThat(loaded.getSubject()).isEqualTo(SUBJECT);

    }

    @Test
    public void shouldSaveHeaders() throws Exception {

        MessageEntity entity = buildBasicEntity();

        String h1Name = "header1";
        String h2Name = "header2";
        String h1Val = "header1Val";
        String h2Val = "header2Val";

        entity.setHeaders(ImmutableSet.of(
                new MessageHeaderEntity(h1Name, h1Val),
                new MessageHeaderEntity(h2Name, h2Val)));

        long savedEntityId = messageRepository.save(entity).getMessageId();
        MessageEntity loaded = messageRepository.findOne(savedEntityId);

        assertThat(loaded.getHeaders()).hasSize(2);
        assertHeaderPresent(entity, h1Name, h1Val);
        assertHeaderPresent(entity, h2Name, h2Val);

    }

    @Test
    public void shouldSaveRecipients() throws Exception {

        MessageEntity entity = buildBasicEntity();

        String email1 = "herp@somedomain.com";
        String email2 = "derp@somedomaincom";

        entity.setRecipients(ImmutableSet.of(
                new MessageRecipientEntity(email1),
                new MessageRecipientEntity(email2)));


        long savedEntityId = messageRepository.save(entity).getMessageId();
        MessageEntity loaded = messageRepository.findOne(savedEntityId);

        assertThat(loaded.getRecipients()).hasSize(2);
        asList(email1, email2).forEach(expected ->
            loaded.getRecipients()
                  .stream()
                  .filter(recip -> expected.equals(recip.getRecipientEmail()))
        .findFirst().orElseThrow(() -> new AssertionFailure("couldn't find email: " + email1)));

    }

    private void assertHeaderPresent(MessageEntity entity, String expectedName, String expectedValue) {

        MessageHeaderEntity foundHeader = entity.getHeaders().stream()
                                                .filter(h -> h.getHeaderName().equals(expectedName))
                                                .findFirst()
                                                .orElseThrow(() -> new AssertionError(
                                                        "Couldn't find header: " + expectedName));
        assertThat(foundHeader.getHeaderValue()).as("Value mismatch, header: " + expectedName).isEqualTo(expectedValue);
    }

    private MessageEntity buildBasicEntity() {
        MessageEntity entity = new MessageEntity();
        entity.setIdentifier(MESSAGE_ID);
        entity.setMessageSize(MESSAGE_SIZE);
        entity.setMessageBody(MESSAGE_BODY);
        entity.setReceivedDate(RECEIVED_DATE);
        entity.setSenderEmail(SENDER_EMAIL);
        entity.setSenderHost(SENDER_HOST);
        entity.setSubject(SUBJECT);
        return entity;
    }
}
