package com.spartasystems.holdmail.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class MessageAttachmentTest {

    @Test
    public void shouldHaveValidEqualsHashcode() throws Exception{

        EqualsVerifier.forClass(MessageAttachment.class)
                      .suppress(Warning.STRICT_INHERITANCE)
                      .verify();
    }

}
