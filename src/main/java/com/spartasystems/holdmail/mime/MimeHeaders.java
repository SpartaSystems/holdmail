package com.spartasystems.holdmail.mime;

import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashMap;
import java.util.Map;

public class MimeHeaders {

    private Map<String, String> headerMap;

    public MimeHeaders() {
        headerMap = new HashMap<>();
    }

    public MimeHeaders(Map<String, String> otherMap) {
        headerMap = new HashMap<>(otherMap);
    }

    @JsonValue
    public Map<String, String> asMap() {
        return new HashMap<>(headerMap);
    }

    public boolean containsHeader(String key) {
        return headerMap.containsKey(key);
    }

    public void put(String key, String value) {
        headerMap.put(key, value);
    }

    public String get(String key) {
        return headerMap.get(key);
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
