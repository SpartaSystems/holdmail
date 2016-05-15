package com.spartasystems.holdmail.rest.mime;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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

    public void addContent(InputStream in) throws IOException {
        this.content = IOUtils.toByteArray(in);
    }

    // TODO: split into parser
    public ResponseEntity toResponseEntity() {
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        headers.forEach(builder::header);
        return builder.body(new InputStreamResource(new ByteArrayInputStream(content)));
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
