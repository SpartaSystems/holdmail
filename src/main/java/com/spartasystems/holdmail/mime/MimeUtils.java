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
import com.spartasystems.holdmail.exception.HoldMailException;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.parser.MimeStreamParser;

import java.io.IOException;
import java.io.InputStream;

public class MimeUtils {

    private MimeUtils() {
    }

    public static MessageContent parseMessageContent(InputStream in) {

        try {
            MessageContentExtractor bodyPartExtractor = new MessageContentExtractor();

            MimeStreamParser parser = new MimeStreamParser();
            parser.setContentDecoding(true);
            parser.setContentHandler(bodyPartExtractor);
            parser.parse(in);
            return bodyPartExtractor.getParts();
        } catch (MimeException | IOException e) {
            throw new HoldMailException("Failed to parse body", e);
        }

    }

}

