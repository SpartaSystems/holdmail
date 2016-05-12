package com.spartasystems.holdmail.rest;

import com.spartasystems.holdmail.model.MessageModel;
import com.spartasystems.holdmail.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/rest", produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageController {

    @Autowired
    private MessageService messageService;

    @RequestMapping("/messages")
    public List<MessageModel> listMessages() {

        return messageService.loadAll();
    }


}
