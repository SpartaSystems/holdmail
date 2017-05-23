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

package com.spartasystems.holdmail.mime;

import com.spartasystems.holdmail.domain.MessageContent;
import com.spartasystems.holdmail.domain.MessageContentPart;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.parser.AbstractContentHandler;
import org.apache.james.mime4j.stream.BodyDescriptor;
import org.apache.james.mime4j.stream.Field;

import java.io.IOException;
import java.io.InputStream;

class MessageContentExtractor extends AbstractContentHandler {

    private MessageContent     bodyParts;
    private MessageContentPart nextPotentialPart;

    public MessageContentExtractor() {
        bodyParts = new MessageContent();
        nextPotentialPart = null;
    }

    @Override
    public void startHeader() throws MimeException {
        nextPotentialPart = new MessageContentPart();
        nextPotentialPart.setSequence(bodyParts.size()+1);
    }

    @Override
    public void field(Field field) throws MimeException {

        // field may have been called after a message container header()
        if(nextPotentialPart != null) {
            nextPotentialPart.setHeader(field.getName(), field.getBody());
        }
    }


    @Override
    public void body(BodyDescriptor bd, InputStream is) throws MimeException, IOException {

        nextPotentialPart.setContent(is);
        bodyParts.addPart(nextPotentialPart);

    }

    protected MessageContentPart getNextPotentialPart() {
        return nextPotentialPart;
    }

    public MessageContent getParts() {
        return bodyParts;
    }
}
