/*******************************************************************************
 * Copyright 2016 - 2018 Sparta Systems, Inc
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

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.james.mime4j.dom.field.FieldName;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

public class MessageContentPart {

    private Map<String, HeaderValue> headers = new HashMap<>();
    private byte[] content;
    private String sha256Sum;
    private int sequence;

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getSHA256Sum() {
        return sha256Sum;
    }

    public void setHeader(String header, String value) {
        this.headers.put(header, new HeaderValue(value));
    }

    public Map<String, HeaderValue> getHeaders() {
        return headers;
    }

    public String getContentType() {

        HeaderValue contentHeader = headers.get(FieldName.CONTENT_TYPE);
        return contentHeader == null ? null : contentHeader.getValue();
    }

    public boolean isHTML() {
        String type = getContentType();
        return type != null && type.equals("text/html");
    }

    public boolean isText() {
        String type = getContentType();
        return type != null && type.equals("text/plain");
    }

    public boolean isAttachment() {
        HeaderValue disposition = headers.get(FieldName.CONTENT_DISPOSITION);
        return disposition != null && (disposition.hasValue("inline") || disposition.hasValue("attachment"));
    }

    public String getAttachmentFilename() {

        if (!isAttachment()) {
            return null;
        }

        HeaderValue disposition = headers.get(FieldName.CONTENT_DISPOSITION);
        HeaderValue contentType = headers.get(FieldName.CONTENT_TYPE);

        String disposFilename = disposition == null ? null : disposition.getParamValue("filename");
        String ctypeFilename = contentType == null ? null : contentType.getParamValue("name");

        /* we _could_ just generate a default filename, but then we'd have to figure out the
         * extension for the media type. Better to not even attempt to do that, and let smarter
         * browsers do a better job of it than we could */
        return ObjectUtils.firstNonNull(disposFilename, ctypeFilename);
    }

    public boolean hasContentId(String contentId) {

        HeaderValue mailCID = headers.get(FieldName.CONTENT_ID);
        return mailCID != null && mailCID.getValue().contains(contentId);

    }

    public void setContent(InputStream in) throws IOException {
        this.content = IOUtils.toByteArray(in);
        this.sha256Sum = DigestUtils.sha256Hex(this.content);
    }

    public String getContentString() {
        return this.content == null ? null : new String(content, StandardCharsets.UTF_8);
    }

    public InputStream getContentStream() {
        return this.content == null ? null : new ByteArrayInputStream(content);
    }

    public int getSize() {
        return this.content == null ? 0 : content.length;
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
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("sequence", sequence)
                .append("headers", headers)
                .append("content", content)
                .append("sha256Sum", sha256Sum)
                .toString();
    }
}
