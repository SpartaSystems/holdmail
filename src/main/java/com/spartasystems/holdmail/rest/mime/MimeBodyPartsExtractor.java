package com.spartasystems.holdmail.rest.mime;

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
            nextPotentialPart.addHeader(field.getName(), field.getBody());
        }
    }


    @Override
    public void body(BodyDescriptor bd, InputStream is) throws MimeException, IOException {

        nextPotentialPart.addContent(is);
        foundBodyParts.addBodyPart(nextPotentialPart);

    }

    public MimeBodyParts getParts() {
        return foundBodyParts;
    }
}
