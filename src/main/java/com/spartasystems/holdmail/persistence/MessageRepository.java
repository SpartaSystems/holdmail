package com.spartasystems.holdmail.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
public interface MessageRepository extends CrudRepository<MessageEntity, Long> {

    List<MessageEntity> findAllByOrderByReceivedDateDesc();

    @Query("SELECT m FROM MessageEntity m join m.recipients r where r.recipientEmail = :recipientEmail order by  m.receivedDate desc")
    List<MessageEntity> findAllForRecipientOrderByReceivedDateDesc(@Param("recipientEmail") String recipientEmail);

    Stream<MessageEntity> findBySubject(String subject, Pageable pageable);

}
