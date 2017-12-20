/*******************************************************************************
 * Copyright 2017 Sparta Systems, Inc
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

import static com.spartasystems.holdmail.domain.HeaderValueParam.parse;
import static org.assertj.core.api.Assertions.assertThat;

public class HeaderValueParamTest {

    @Test
    public void shouldHandleNonRFC2231() {

        assertThat(parse("")).isEqualTo(new HeaderValueParam("", "", 0, null, null));
        assertThat(parse("nonkeyval")).isEqualTo(new HeaderValueParam("nonkeyval", "", 0, null, null));
        assertThat(parse("key=val")).isEqualTo(new HeaderValueParam("key", "val", 0, null, null));
    }

    @Test
    public void shouldHandleRFC2231Position() {

        assertThat(parse("key*=val")).isEqualTo(new HeaderValueParam("key", "val", 0, null, null));
        assertThat(parse("key*1=val")).isEqualTo(new HeaderValueParam("key", "val", 1, null, null));
        assertThat(parse("key*5*=val")).isEqualTo(new HeaderValueParam("key", "val", 5, null, null));
    }

    @Test
    public void shouldHandleRFC2231CharsetLang() {

        assertThat(parse("key*=''val")).isEqualTo(new HeaderValueParam("key", "val", 0, "", ""));
        assertThat(parse("key*=utf-8''val")).isEqualTo(new HeaderValueParam("key", "val", 0, "utf-8", ""));
        assertThat(parse("key*='de'val")).isEqualTo(new HeaderValueParam("key", "val", 0, "", "de"));
        assertThat(parse("key*=utf-8'de'val")).isEqualTo(new HeaderValueParam("key", "val", 0, "utf-8", "de"));

        // same 4 tests again, only in combination with position
        assertThat(parse("key*2*=''val")).isEqualTo(new HeaderValueParam("key", "val", 2, "", ""));
        assertThat(parse("key*2*=utf-8''val")).isEqualTo(new HeaderValueParam("key", "val", 2, "utf-8", ""));
        assertThat(parse("key*2*='de'val")).isEqualTo(new HeaderValueParam("key", "val", 2, "", "de"));
        assertThat(parse("key*2*=utf-8'de'val")).isEqualTo(new HeaderValueParam("key", "val", 2, "utf-8", "de"));
    }

    @Test
    public void shouldIgnoreRFC2231CharsetLangIfNotAsteriskSuffixed() {

        assertThat(parse("key=utf-8'de'val")).isEqualTo(new HeaderValueParam("key", "utf-8'de'val", 0, null, null));
        assertThat(parse("key*2=utf-8'de'val")).isEqualTo(new HeaderValueParam("key", "utf-8'de'val", 2, null, null));
    }

    @Test
    public void shouldHaveValidEqualsHashcode() {

        EqualsVerifier.forClass(HeaderValueParam.class)
                .suppress(Warning.STRICT_INHERITANCE)
                .verify();

    }

}