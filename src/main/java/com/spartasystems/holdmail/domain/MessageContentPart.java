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

package com.spartasystems.holdmail.domain;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.james.mime4j.dom.field.FieldName;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MessageContentPart {

    private Map<String, String> headers = new HashMap<>();
    private byte[] content;

    public void setHeader(String header, String value) {
        this.headers.put(header, value);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getContentType() {
        return headers.get(FieldName.CONTENT_TYPE);
    }

    public boolean isHTML() {
        String type = getContentType();
        return type != null && type.startsWith("text/html");
    }

    public boolean isText() {
        String type = getContentType();
        return type != null && type.startsWith("text/plain");
    }

    public boolean isAttachment() {
        String disposition = headers.get(FieldName.CONTENT_DISPOSITION);
        return disposition != null && (disposition.equals("inline") || disposition.equals("attachment"));
    }

    public boolean hasContentId(String contentId) {

        String mailCID = headers.get(FieldName.CONTENT_ID);
        return !StringUtils.isBlank(mailCID) && mailCID.contains(contentId);

    }

    public void setContent(InputStream in) throws IOException {
        this.content = IOUtils.toByteArray(in);
    }

    public String getContentString() {
        return this.content == null ? null : new String(content, StandardCharsets.UTF_8);
    }

    public InputStream getContentStream() {
        return this.content == null ? null : new ByteArrayInputStream(content);
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MessageContentPart[");
        sb.append("headers=").append(headers);
        sb.append(", content=").append(content == null ? "null" : content.length + "b");
        sb.append(']');
        return sb.toString();
    }
}
