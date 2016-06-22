package com.spartasystems.holdmail.mime;

import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.parser.MimeStreamParser;
import org.apache.james.mime4j.stream.MimeConfig;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class MimeBodyParser {

    public MimeBodyParts findAllBodyParts(InputStream in) throws IOException, MimeException {

        MimeBodyPartsExtractor bodyPartExtractor = new MimeBodyPartsExtractor();

        MimeStreamParser parser = new MimeStreamParser(new MimeConfig());
        parser.setContentDecoding(true);
        parser.setContentHandler(bodyPartExtractor);
        parser.parse(in);

        return bodyPartExtractor.getParts();
    }

}

