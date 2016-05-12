package com.spartasystems.holdmail.service;

import com.spartasystems.holdmail.MessageMapper;
import com.spartasystems.holdmail.model.MessageModel;
import com.spartasystems.holdmail.persistence.MessageEntity;
import com.spartasystems.holdmail.persistence.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageMapper messageMapper;

    public MessageModel saveIncomingMessage(MessageModel message) {

        MessageEntity entity = messageMapper.toEntity(message);

        entity = messageRepository.save(entity);

        return messageMapper.fromEntity(entity);
    }

    // TODO: perhaps more approrpiate not to load the whole entity (read: body) for a list
    public List<MessageModel> loadAll() {

        List<MessageEntity> allEntities = messageRepository.findAll();

        return allEntities.stream()
                          .map(e -> messageMapper.fromEntity(e))
                          .collect(Collectors.toList());

    }

}
