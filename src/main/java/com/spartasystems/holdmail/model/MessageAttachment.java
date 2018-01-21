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

package com.spartasystems.holdmail.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class MessageAttachment {

    private final String attachmentId;
    private final String filename;
    private final String contentType;
    private final String disposition;
    private final String sha256Sum;
    private final int size;

    public MessageAttachment(String attachmentId, String filename, String contentType, String disposition, String sha256Sum, int size) {
        this.attachmentId = attachmentId;
        this.filename = filename;
        this.contentType = contentType;
        this.disposition = disposition;
        this.sha256Sum = sha256Sum;
        this.size = size;
    }

    public String getAttachmentId() {
        return attachmentId;
    }

    public String getFilename() {
        return filename;
    }

    public String getContentType() {
        return contentType;
    }

    public String getDisposition() {
        return disposition;
    }

    public String getSha256Sum() {
        return sha256Sum;
    }

    public int getSize() {
        return size;
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
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
