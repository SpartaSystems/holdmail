package com.spartasystems.holdmail.persistence;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class MessageEntityTest {

    @Test
    public void shouldVerifyEqualsContract() {
        EqualsVerifier.forClass(MessageEntity.class)
                .suppress(Warning.NONFINAL_FIELDS, Warning.NULL_FIELDS, Warning.STRICT_INHERITANCE)
                .verify();
    }

}