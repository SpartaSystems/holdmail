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
import org.apache.commons.lang3.StringUtils;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.parser.MimeStreamParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MimeUtils {

    private static final Logger logger = LoggerFactory.getLogger(MimeUtils.class);

    private MimeUtils() {
    }

    /**
     * Parse the provided inputstream into the message domain
     * @param rawContentStream The inputstream to the message RAW content
     * @return A {@link MessageContent} object representing the parsed content, or
     *          a {@link HoldMailException} if parsing failed.
     */
    public static MessageContent parseMessageContent(final InputStream rawContentStream) {

        try {
            MessageContentExtractor bodyPartExtractor = new MessageContentExtractor();

            MimeStreamParser parser = new MimeStreamParser();
            parser.setContentDecoding(true);
            parser.setContentHandler(bodyPartExtractor);
            parser.parse(rawContentStream);
            return bodyPartExtractor.getParts();
        } catch (MimeException | IOException e) {
            throw new HoldMailException("Failed to parse body", e);
        }

    }

    /**
     * Perform a {@link URLDecoder#decode(String, String)} but provide some null safety
     * @param value The value to decode
     * @param encoding The enconding
     * @return The decoded string, or the original string if either the value or encoding were invalid.
     */
    public static String safeURLDecode(final String value, final String encoding) {

        if(StringUtils.isBlank(value)){
            logger.warn("No value was provided for decoding");
            return value;
        }

        if(StringUtils.isBlank(encoding)){
            logger.warn("No encoding name was provided for decoding, returning original value");
            return value;
        }

        try {
            return URLDecoder.decode(value, encoding);
        } catch (UnsupportedEncodingException e) {
            logger.error("Couldn't decode value with encoding '" + encoding
                    + "': " + e.getMessage() + ", value was:" + value);
            return value;
        }
    }

    /**
     * Strip any leading/trailing spaces from the string.  Spaces inside the quotes will be maintained.
     * <ul>
     *     <li>null --&gt; null</li>
     *     <li>"" --&gt; ""</li>
     *     <li>"  blah  " -&gt; "blah"</li>
     *     <li>" \"blah\" " -&gt; "blah"</li>
     *     <li>" \" blah \" " -&gt; " blah "</li>
     * </ul>
     * @param input the input string
     * @return The trimmed string
     */
    public static String trimQuotes(final String input) {

        if(StringUtils.isBlank(input)){
            return input;
        }

        final Pattern TRIM_PARAM_REGEX = Pattern.compile("^\\s*\"?(?<value>.*?)\"?\\s*$");

        Matcher matcher = TRIM_PARAM_REGEX.matcher(input);
        if (matcher.matches()) {
            return matcher.group("value");
        } else {
            return input;
        }
    }

}

