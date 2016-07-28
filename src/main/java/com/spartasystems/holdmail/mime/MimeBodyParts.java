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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

public class MimeBodyParts {

    private List<MimeBodyPart> bodyPartList;

    public MimeBodyParts() {
        this.bodyPartList = new ArrayList<>();
    }

    public void addBodyPart(MimeBodyPart bodyPart) {
        this.bodyPartList.add(bodyPart);
    }

    public MimeBodyPart findFirstHTMLBody() {
        return bodyPartList.stream().filter(MimeBodyPart::isHTML).findFirst().orElse(null);
    }

    public MimeBodyPart findFirstTextBody() {
        return bodyPartList.stream().filter(MimeBodyPart::isText).findFirst().orElse(null);
    }

    public MimeBodyPart findByContentId(String contentId) {
        return bodyPartList.stream().filter(b -> b.hasContentId(contentId)).findFirst().orElse(null);
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



