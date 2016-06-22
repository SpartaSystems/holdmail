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
