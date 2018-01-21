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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.h2.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MessageContent {

    private List<MessageContentPart> parts;

    public MessageContent() {
        this.parts = new ArrayList<>();
    }

    public void addPart(MessageContentPart part) {
        this.parts.add(part);
    }

    public List<MessageContentPart> getParts() {
        return parts;
    }

    public String findFirstHTMLPart() {
        return parts.stream()
                    .filter(MessageContentPart::isHTML)
                    .map(MessageContentPart::getContentString)
                    .findFirst()
                    .orElse(null);
    }

    public String findFirstTextPart() {
        return parts.stream()
                    .filter(MessageContentPart::isText)
                    .map(MessageContentPart::getContentString)
                    .findFirst()
                    .orElse(null);
    }

    public List<MessageContentPart> findAttachmentParts() {
        return parts.stream()
                    .filter(MessageContentPart::isAttachment)
                    .collect(Collectors.toList());
    }

    public MessageContentPart findByContentId(String contentId) {
        return parts.stream().filter(b -> b.hasContentId(contentId)).findFirst().orElse(null);
    }

    public MessageContentPart findBySequenceId(int seqId) {
        return parts.stream().filter(b -> b.getSequence() == seqId).findFirst().orElse(null);
    }

    public int size() {
        return parts.size();
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



