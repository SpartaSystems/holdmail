package com.spartasystems.holdmail.persistence;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.spartasystems.holdmail.BaseIntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/messages.xml")
@DirtiesContext
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
}