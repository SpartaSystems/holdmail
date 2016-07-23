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

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MimeBodyPart {

    Map<String, String> headers = new HashMap<>();
    byte[] content;

    public MimeBodyPart() {
    }

    public void setHeader(String header, String value) {
        this.headers.put(header, value);
    }

    public void setContent(InputStream in) throws IOException {
        this.content = IOUtils.toByteArray(in);
    }

    public String getContentType() {
        return headers.get("Content-Type");
    }

    public boolean isHTML() {
        String type = getContentType();
        return type != null && type.startsWith("text/html");
    }

    public boolean isText() {
        String type = getContentType();
        return type != null && type.startsWith("text/plain");
    }

    public boolean hasContentId(String contentId) {

        String mailCID = headers.get("Content-ID");
        return !StringUtils.isBlank(mailCID) && mailCID.contains(contentId);

    }

    public String getContentString() {
        return this.content == null ? null : new String(content, StandardCharsets.UTF_8);
    }

    public InputStream getContentStream() {
        return this.content == null ? null : new ByteArrayInputStream(content);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BodyPart{");
        sb.append("headers=").append(headers);
        sb.append(", content=").append(content == null ? "null" : content.length + "b");
        sb.append('}');
        return sb.toString();
    }
}
