package com.spartasystems.holdmail.rest;

import com.spartasystems.holdmail.model.MessageForwardCommand;
import com.spartasystems.holdmail.service.MessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class MessageControllerTest {

    @Mock
    private MessageService messageServiceMock;

    @InjectMocks
    private MessageController messageController;

    @Test
    public void shouldCallMessageServiceToForwardMail() throws Exception {

        ResponseEntity response = messageController.fowardMail(345, new MessageForwardCommand("some@guy.com"));

        Mockito.verify(messageServiceMock).forwardMessage(345, "some@guy.com");
        assertThat(response).isEqualTo(ResponseEntity.accepted().build());


    }

}