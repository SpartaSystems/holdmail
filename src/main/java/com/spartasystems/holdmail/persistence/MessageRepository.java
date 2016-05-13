package com.spartasystems.holdmail.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface MessageRepository extends CrudRepository<MessageEntity, Long> {

    List<MessageEntity> findAllByOrderByReceivedDateDesc();

}
