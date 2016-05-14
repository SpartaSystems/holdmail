package com.spartasystems.holdmail.rest.mime;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.parser.MimeStreamParser;
import org.apache.james.mime4j.stream.MimeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.spartasystems.holdmail.rest.mime.MessageMimeConverter.DeliveryMode.HTML;
import static com.spartasystems.holdmail.rest.mime.MessageMimeConverter.DeliveryMode.TEXT;

@Component
public class MessageMimeConverter {

    private static Logger logger = LoggerFactory.getLogger(MessageMimeConverter.class);

    public static final String PARAM_CID  = "cid";
    public static final String PARAM_MODE = "mode";

    public ResponseEntity convertContentType(InputStream in, String paramCid, String paramMode)
            throws Exception {

        Pair<String, byte[]> vals;

        if (StringUtils.isNotBlank(paramCid)) {

            logger.info("Parsing for content id '" + paramCid + "'");
            vals = parseContent(in, paramCid, null);
            return  ResponseEntity.ok()
                                  .header("Content-Type", vals.getLeft())
                                  .body(new InputStreamResource(new ByteArrayInputStream(vals.getRight())));
        }
        else {

            DeliveryMode mode = DeliveryMode.valueOf(paramMode, null);

            if(mode == null) {
                vals = parseContent(in, null, null);
                return  ResponseEntity.ok()
                                      .header("Content-Type", vals.getLeft())
                                      .body(new InputStreamResource(new ByteArrayInputStream(vals.getRight())));
            }

            switch (mode) {

                case HTML:
                    logger.info("serving text/html part");
                    vals = parseContent(in, null, HTML);
                    return  ResponseEntity.ok()
                                    .header("Content-Type", vals.getLeft())
                                    .body(new InputStreamResource(new ByteArrayInputStream(vals.getRight())));


                case TEXT:
                    logger.info("serving text/plain part");
                    vals = parseContent(in, null, TEXT);
                    return  ResponseEntity.ok()
                                          .header("Content-Type", vals.getLeft())
                                          .body(new InputStreamResource(new ByteArrayInputStream(vals.getRight())));

                case RAW_ATTACH:

                    logger.info("serving raw as application/octet-stream");

                    return ResponseEntity.ok()
                                         .header("Content-Disposition", "attachment;filename=\"mail.mht\"")
                                         .contentType(MediaType.APPLICATION_OCTET_STREAM)
                                         .body(new InputStreamResource(in));

                case RAW_RFC822:
                    logger.info("serving raw as message/rfc822");

                    return ResponseEntity.ok()
                                         .header("Content-Type", "message/rfc822")
                                         .body(new InputStreamResource(in));

                case RAW_TEXT:
                default:
                    logger.info("serving raw as text/plain");

                    return ResponseEntity.ok()
                                         .contentType(MediaType.TEXT_PLAIN)
                                         .body(new InputStreamResource(in));


            }

        }


    }

    private Pair<String, byte[]> parseContent(InputStream in, String cid, DeliveryMode mode) throws IOException, MimeException {

        MultiPartContentHandler handler = cid != null ? new MultiPartContentHandler(cid)
                                                        : new MultiPartContentHandler(mode);

        MimeStreamParser parser = new MimeStreamParser(new MimeConfig());
        parser.setContentHandler(handler);
        parser.parse(in);


        return handler.getParsedContent();
    }

    public enum DeliveryMode {
        RAW_RFC822,
        RAW_ATTACH,
        RAW_TEXT,
        TEXT,
        HTML;

        public static DeliveryMode valueOf(String mode, DeliveryMode defaultMode) {

            try {
                return valueOf(mode.toUpperCase());
            }
            catch (Exception e) {
                logger.warn("Unknown mode '" + mode + "', defaulting to: " + defaultMode);
                return defaultMode;
            }

        }
    }
}
