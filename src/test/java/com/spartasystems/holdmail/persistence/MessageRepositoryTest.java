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

package com.spartasystems.holdmail.persistence;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.spartasystems.holdmail.integration.BaseIntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.transaction.Transactional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@DatabaseSetup(type = DatabaseOperation.INSERT, value = "/messages.xml")
@DatabaseTearDown(type = DatabaseOperation.DELETE, value = "/messages.xml")
@DirtiesContext
@Transactional
public class MessageRepositoryTest extends BaseIntegrationTest{


    @Autowired
    private MessageRepository messageRepository;

    @Test
    public void shouldFindSixMessagesWithThisIsItTitle() throws Exception {
        Stream<MessageEntity> first150BySubjectDesc = messageRepository.findBySubject("This is it", new PageRequest(0,150));
        assertThat(first150BySubjectDesc.collect(Collectors.toList())).hasSize(6);
    }

    @Test
    public void shouldFind25MessagesWithSomeoneExampleComSenderEmail() throws Exception {
        Stream<MessageEntity> first150BySenderEmail = messageRepository.findBySenderEmail("someone@example.org",new PageRequest(0,150));

        assertThat(first150BySenderEmail.collect(Collectors.toList())).hasSize(25);

    }

    @Test
    public void shouldFindEmailsBySenderWithAlias() throws Exception {
        Stream<MessageEntity> oneEmailFound = messageRepository.findBySenderEmail("someone+alias@example.org",new PageRequest(0,150));
        assertThat(oneEmailFound.collect(Collectors.toList())).hasSize(1);
    }
}
