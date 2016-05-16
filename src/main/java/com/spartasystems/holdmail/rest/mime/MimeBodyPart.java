package com.spartasystems.holdmail.rest.mime;

import org.apache.commons.io.IOUtils;

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

    public void addHeader(String header, String value) {
        this.headers.put(header, value);
    }

    public boolean isHTML() {
        String type = headers.get("Content-Type");
        return type != null && type.startsWith("text/html");
    }

    public boolean isText() {
        String type = headers.get("Content-Type");
        return type != null && type.startsWith("text/plain");
    }

    public void setContent(InputStream in) throws IOException {
        this.content = IOUtils.toByteArray(in);
    }

    public String getContentString() {
        return this.content == null ? null : new String(content, StandardCharsets.UTF_8);
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
