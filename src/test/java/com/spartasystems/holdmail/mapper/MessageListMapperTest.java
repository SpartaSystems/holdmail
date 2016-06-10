package com.spartasystems.holdmail.mapper;

import com.spartasystems.holdmail.model.MessageList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class MessageListMapperTest {

    @InjectMocks
    private MessageListMapper messageListMapper;

    @Test
    public void shouldMapToEmptyListWhenStreamIsEmpty() throws Exception {
        MessageList messageList = messageListMapper.toMessageList(Stream.empty());

        assertThat(messageList.getMessages()).isEmpty();
    }
    
}