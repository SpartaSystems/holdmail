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

