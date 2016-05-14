package com.spartasystems.holdmail.rest.mime;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.parser.AbstractContentHandler;
import org.apache.james.mime4j.stream.BodyDescriptor;
import org.apache.james.mime4j.stream.Field;

import java.io.IOException;
import java.io.InputStream;

public class MultiPartContentHandler extends AbstractContentHandler {

    private final MessageMimeConverter.DeliveryMode desiredMode;
    private final String                            desiredCid;

    private String contentId;
    private String contentType;
    private String stringContent = null;
    private byte[] binaryContent = null;

    public MultiPartContentHandler(MessageMimeConverter.DeliveryMode desiredMode) {

        this.desiredMode = desiredMode;
        this.desiredCid = null;
    }

    public MultiPartContentHandler(String desiredCid) {

        this.desiredMode = null;
        this.desiredCid = "<" + desiredCid + ">";
    }

    @Override
    public void startHeader() throws MimeException {
        contentType = null;
        contentId = null;
    }

    @Override
    public void field(Field f) throws MimeException {

        if ("Content-Type".equals(f.getName())) {
            contentType = f.getBody();
        }

        if ("Content-ID".equals(f.getName())) {
            contentId = f.getBody();
        }
    }

    @Override
    public void body(BodyDescriptor bodyDescriptor, InputStream inputStream) throws IOException {

        if (desiredCid != null && contentId != null && contentId.contains(desiredCid)) {
            binaryContent = IOUtils.toByteArray(inputStream);
        }
        else {
            stringContent = IOUtils.toString(inputStream);
        }

    }

    @Override
    public void endMessage() throws MimeException {

        try {
            if (desiredCid == null) {

                if (MessageMimeConverter.DeliveryMode.HTML.equals(desiredMode)) {

                    if (stringContent == null) {
                        throw new MimeException("Expected, but did not find HTML content");
                    }
                    else {

                        String linksFixed = stringContent
                                .replaceAll("\"cid:", "\"?" + MessageMimeConverter.PARAM_CID + "=");

                        contentType = "text/html";
                        binaryContent = linksFixed.getBytes("UTF-8");

                    }
                }
                else {

                    // assuming text
                    if (stringContent == null) {
                        throw new MimeException("Expected, but did not find text content");
                    }
                    else {
                        contentType = "text/plain";
                        binaryContent = stringContent.getBytes("UTF-8");
                    }

                }

            }

        }
        catch (IOException e) {
            throw new MimeException("Couldn't write output ", e);
        }

    }

    public Pair<String, byte[]> getParsedContent() {
        return Pair.of(contentType, binaryContent);
    }

}
