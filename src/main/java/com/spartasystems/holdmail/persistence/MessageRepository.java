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

    Stream<MessageEntity> findBySenderEmail(String senderEmail, Pageable pageable);

}
