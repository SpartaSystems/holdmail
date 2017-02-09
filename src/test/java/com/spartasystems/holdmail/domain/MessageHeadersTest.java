/*******************************************************************************
 * Copyright 2016 - 2017 Sparta Systems, Inc
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

package com.spartasystems.holdmail.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static org.assertj.core.api.Assertions.assertThat;

public class MessageHeadersTest {

    @Test
    public void shouldSetEmptyMapOnConstructor() throws Exception {

        MessageHeaders headers = new MessageHeaders();
        assertThat(headers.asMap()).isEmpty();

    }

    @Test
    public void shouldSetMapOnMapCopyContructor() throws Exception {

        Map<String, String> otherMap = of("k1", "v1", "k2", "v2");

        MessageHeaders headers = new MessageHeaders(otherMap);
        assertThat(headers.asMap()).isEqualTo(otherMap);

    }

    @Test
    public void shouldAddValueOnPutOfNewKey() throws Exception {

        MessageHeaders headers = new MessageHeaders(of("name", "Mary"));
        headers.put("age", "10");

        assertThat(headers.asMap()).isEqualTo(of("name", "Mary", "age", "10"));

    }

    @Test
    public void shouldReplaceValueOnPutOfExistingKEy() throws Exception {

        MessageHeaders headers = new MessageHeaders(of("name", "Alex", "age", "15"));

        headers.put("age", "17");

        assertThat(headers.asMap()).isEqualTo(of("name", "Alex", "age", "17"));

    }

    @Test
    public void shouldGetValue() throws Exception {

        MessageHeaders headers = new MessageHeaders(of("name", "Alex", "age", "15"));

        assertThat(headers.get("flerp")).isNull();
        assertThat(headers.get("name")).isEqualTo("Alex");

    }

    @Test
    public void shouldHaveValidEqualsHashcode() throws Exception {

        EqualsVerifier.forClass(MessageHeaders.class)
                      .suppress(Warning.NONFINAL_FIELDS,
                              Warning.NULL_FIELDS,
                              Warning.STRICT_INHERITANCE)
                      .verify();

    }

    @Test
    public void shouldHaveToString() throws Exception{

        MessageHeaders headers = new MessageHeaders(of("name", "Alex", "age", "15"));

        assertThat(headers.toString()).isEqualTo("MessageHeaders[headerMap={name=Alex, age=15}]");

    }

}
