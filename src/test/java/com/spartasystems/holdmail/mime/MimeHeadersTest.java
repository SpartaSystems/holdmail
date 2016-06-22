package com.spartasystems.holdmail.mime;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class MimeHeadersTest {

    @Test
    public void shouldHaveValidEqualsHashcode() throws Exception {

        EqualsVerifier.forClass(MimeHeaders.class)
                      .suppress(Warning.NONFINAL_FIELDS, Warning.NULL_FIELDS, Warning.STRICT_INHERITANCE)
                      .verify();

    }

}
