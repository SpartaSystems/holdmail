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

package com.spartasystems.holdmail.mime;

import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.parser.AbstractContentHandler;
import org.apache.james.mime4j.stream.BodyDescriptor;
import org.apache.james.mime4j.stream.Field;

import java.io.IOException;
import java.io.InputStream;

class MimeBodyPartsExtractor extends AbstractContentHandler {

    private MimeBodyParts foundBodyParts;
    private MimeBodyPart  nextPotentialPart;

    public MimeBodyPartsExtractor() {
        foundBodyParts = new MimeBodyParts();
        nextPotentialPart = null;
    }

    @Override
    public void startHeader() throws MimeException {
        nextPotentialPart = new MimeBodyPart();
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
        foundBodyParts.addBodyPart(nextPotentialPart);

    }

    public MimeBodyParts getParts() {
        return foundBodyParts;
    }
}
