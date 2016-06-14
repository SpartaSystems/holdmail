package com.spartasystems.holdmail.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest({UUID.class})
public class MessageTest {

    private static final String HEXCHAR = "[0-9a-fA-F]";
    private static final String UUID_PATT = String.format("^%s{8}\\-%s{4}\\-%s{4}\\-%s{4}\\-%s{12}$",
            HEXCHAR, HEXCHAR, HEXCHAR, HEXCHAR, HEXCHAR);

    @Test
    public void shouldSetIdentifierOnConstructor() throws Exception {

        Message message = new Message("derpId");
        assertThat(message.getIdentifier()).isEqualTo("derpId");

    }

    @Test
    public void shouldSetRandomIdentifierOnDefaultConstructor() {

        List<String> seen = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Message message = new Message();
            assertThat(seen).doesNotContain(message.getIdentifier());
            assertThat(message.getIdentifier()).matches(UUID_PATT);
            seen.add(message.getIdentifier());
        }

    }

    @Test
    public void shouldSetIdentifier() throws Exception {

        Message message = new Message("someId");
        message.setIdentifier("newMsgId");
        assertThat(message.getIdentifier()).isEqualTo("newMsgId");

    }

    @Test
    public void shouldSetRandomIdentifierOnBlankIdentifier() {

        List<String> seen = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Message message = new Message("test");
            message.setIdentifier("");
            assertThat(seen).doesNotContain(message.getIdentifier());
            assertThat(message.getIdentifier()).matches(UUID_PATT);
            seen.add(message.getIdentifier());
        }

    }

    @Test
    public void shouldVerifyEqualContract() {
        EqualsVerifier.forClass(Message.class)
                .suppress(Warning.NONFINAL_FIELDS, Warning.NULL_FIELDS, Warning.STRICT_INHERITANCE)
                .verify();
    }

}
