/*******************************************************************************
 * Copyright 2016 - 2017 Sparta Systems, Inc
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

package com.spartasystems.holdmail.rest;

import com.spartasystems.holdmail.domain.Message;
import com.spartasystems.holdmail.domain.MessageContentPart;
import com.spartasystems.holdmail.mapper.MessageSummaryMapper;
import com.spartasystems.holdmail.model.MessageForwardCommand;
import com.spartasystems.holdmail.model.MessageList;
import com.spartasystems.holdmail.model.MessageSummary;
import com.spartasystems.holdmail.service.MessageService;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.http.MediaType.TEXT_PLAIN;

@RestController
@RequestMapping(value = "/rest/messages", produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageSummaryMapper messageSummaryMapper;

    @Autowired
    private HTMLPreprocessor htmlPreprocessor;

    @RequestMapping()
    public MessageList getMessages(
            @RequestParam(name = "recipient", required = false) @Email String recipientEmail,
            Pageable pageRequest) {

        return messageService.findMessages(recipientEmail, pageRequest);
    }

    @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteMessageContent(@PathVariable("messageId") long messageId) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{messageId}")
    public ResponseEntity getMessageContent(@PathVariable("messageId") long messageId) {

        MessageSummary summary = loadMessageSummary(messageId);
        return ResponseEntity.ok().body(summary);
    }

    @RequestMapping(value = "/{messageId}/html")
    public ResponseEntity getMessageContentHTML(@PathVariable("messageId") long messageId) {

        MessageSummary summary = loadMessageSummary(messageId);
        String htmlSubstituted = htmlPreprocessor.preprocess(messageId, summary.getMessageBodyHTML());
        return serveContent(htmlSubstituted, TEXT_HTML);
    }

    @RequestMapping(value = "/{messageId}/text")
    public ResponseEntity getMessageContentTEXT(@PathVariable("messageId") long messageId) {

        MessageSummary summary = loadMessageSummary(messageId);
        return serveContent(summary.getMessageBodyText(), TEXT_PLAIN);
    }

    @RequestMapping(value = "/{messageId}/raw")
    public ResponseEntity getMessageContentRAW(@PathVariable("messageId") long messageId) {

        Message message = messageService.getMessage(messageId);
        return serveContent(message.getRawMessage(), TEXT_PLAIN);
    }

    @RequestMapping(value = "/{messageId}/content/{contentId}")
    public ResponseEntity getMessageContentByPartId(@PathVariable("messageId") long messageId,
            @PathVariable("contentId") String contentId) {

        Message message = messageService.getMessage(messageId);

        MessageContentPart content = message.getContent().findByContentId(contentId);

        return ResponseEntity.ok()
                             .header("Content-Type", content.getContentType())
                             .body(new InputStreamResource(content.getContentStream()));
    }

    @RequestMapping(value = "/{messageId}/att/{attId}")
    public ResponseEntity getMessageContentByAttachmentId(@PathVariable("messageId") long messageId,
            @PathVariable("attId") int attId) {

        Message message = messageService.getMessage(messageId);

        MessageContentPart content = message.getContent().findBySequenceId(attId);

        String disposition = "attachment;";

        if(StringUtils.isNotBlank(content.getAttachmentFilename())) {
            disposition += " filename=\"" + content.getAttachmentFilename() + "\";";
        }

        return ResponseEntity.ok()
                             .header("Content-Type", content.getContentType())
                             .header("Content-Disposition", disposition)
                             .body(new InputStreamResource(content.getContentStream()));
    }

    @RequestMapping(value = "/{messageId}/forward", method = RequestMethod.POST)
    public ResponseEntity fowardMail(@PathVariable("messageId") long messageId,
            @Valid @RequestBody MessageForwardCommand forwardCommand)  {

        messageService.forwardMessage(messageId, forwardCommand.getRecipient());

        return ResponseEntity.accepted().build();
    }

    // -------------------------- utility ------------------------------------

    protected MessageSummary loadMessageSummary(long messageId) {
        Message message = messageService.getMessage(messageId);
        return messageSummaryMapper.toMessageSummary(message);
    }

    protected ResponseEntity serveContent(Object data, MediaType mediaType) {

        if (data == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().contentType(mediaType).body(data);
    }

}
