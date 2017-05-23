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

import com.google.common.collect.ImmutableMap;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HeaderValueTest {

    public static final String SINGLE_PARAM       = "my-value; param1=thing.gif";
    public static final String MULTI_PARAM        = "my-value; param1=\"0\"; param2=\"quoted\"; param3=unquoted";
    public static final String MULTI_PARAM_SPACES = "my-value;   param1=\"0\"  ;   param2=\"quoted\"  ;  param3=unquoted  ";
    public static final String NO_PARAMS          = "my-value";

    @Test
    public void shouldHaveValidEqualsHashCode() throws Exception {

        EqualsVerifier.forClass(HeaderValue.class)
                      .suppress(Warning.STRICT_INHERITANCE)
                      .verify();
    }

    @Test
    public void shouldHandleBlank() throws Exception {

        HeaderValue blank = new HeaderValue("");
        assertThat(blank.getValue()).isEqualTo("");
        assertThat(blank.getParams()).isEmpty();
    }

    @Test
    public void shouldMapNoParams() throws Exception {

        HeaderValue singleParam = new HeaderValue(NO_PARAMS);
        assertThat(singleParam.getValue()).isEqualTo("my-value");
        assertThat(singleParam.getParams()).isEmpty();

    }

    @Test
    public void shouldMapSingleParam() throws Exception {

        HeaderValue singleParam = new HeaderValue(SINGLE_PARAM);
        assertThat(singleParam.getValue()).isEqualTo("my-value");
        assertThat(singleParam.getParams()).isEqualTo(ImmutableMap.of("param1", "thing.gif"));
    }

    @Test
    public void shouldMatchMultiParam() throws Exception {

        HeaderValue multiParam = new HeaderValue(MULTI_PARAM);
        assertThat(multiParam.getValue()).isEqualTo("my-value");
        assertThat(multiParam.getParams()).isEqualTo(
                ImmutableMap.of("param1", "0", "param2", "quoted", "param3", "unquoted"));

    }

    @Test
    public void shouldMatchMultiParamWithSpacePadding() throws Exception {

        HeaderValue multiParam = new HeaderValue(MULTI_PARAM_SPACES);
        assertThat(multiParam.getValue()).isEqualTo("my-value");
        assertThat(multiParam.getParams()).isEqualTo(
                ImmutableMap.of("param1", "0", "param2", "quoted", "param3", "unquoted"));

    }

    @Test
    public void shouldHaveValue() throws Exception{

        HeaderValue headerValue = new HeaderValue(SINGLE_PARAM);
        assertThat(headerValue.hasValue("my-value")).isTrue();
        assertThat(headerValue.hasValue("some-other-value")).isFalse();

    }

    @Test
    public void shouldGetParamValue() throws Exception{

        HeaderValue multiParam = new HeaderValue(MULTI_PARAM_SPACES);
        assertThat(multiParam.getParamValue("param1")).isEqualTo("0");
        assertThat(multiParam.getParamValue("param2")).isEqualTo("quoted");
        assertThat(multiParam.getParamValue("param3")).isEqualTo("unquoted");

        assertThat(multiParam.getParamValue("unknown-param")).isNull();


    }

}
