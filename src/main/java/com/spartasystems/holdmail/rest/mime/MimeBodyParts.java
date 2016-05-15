package com.spartasystems.holdmail.rest.mime;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MimeBodyParts {

    private List<MimeBodyPart> bodyPartList;

    public MimeBodyParts() {
        this.bodyPartList = new ArrayList<>();
    }

    public void addBodyPart(MimeBodyPart bodyPart) {
        this.bodyPartList.add(bodyPart);
    }

    public Optional<MimeBodyPart> findFirstHTMLBody() {
        return bodyPartList.stream().filter(MimeBodyPart::isHTML).findFirst();
    }

    public Optional<MimeBodyPart> findFirstTextBody() {
        return bodyPartList.stream().filter(MimeBodyPart::isText).findFirst();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}



