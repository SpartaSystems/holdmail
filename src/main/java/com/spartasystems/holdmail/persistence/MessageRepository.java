package com.spartasystems.holdmail.persistence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface MessageRepository extends CrudRepository<MessageEntity, Long> {

    List<MessageEntity> findAllByOrderByReceivedDateDesc();

    @Query("SELECT m FROM MessageEntity m join m.recipients r where r.recipientEmail = :recipientEmail order by  m.receivedDate desc")
    List<MessageEntity> findAllForRecipientOrderByReceivedDateDesc(@Param("recipientEmail") String recipientEmail);

}
