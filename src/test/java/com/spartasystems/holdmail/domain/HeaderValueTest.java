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

    private static final String SINGLE_PARAM = "my-value; param1=thing.gif";
    private static final String MULTI_PARAM = "my-value; param1=\"0\"; param2=\"quoted\"; param3=unquoted";
    private static final String MULTI_PARAM_SPACES = "my-value;   param1=\"0\"  ;   param2=\"quoted\"  ;  param3=unquoted  ";
    private static final String NO_PARAMS = "my-value";

    @Test
    public void shouldHaveValidEqualsHashCode() {

        EqualsVerifier.forClass(HeaderValue.class)
                .suppress(Warning.STRICT_INHERITANCE)
                .verify();
    }

    @Test
    public void shouldHandleBlank() {

        HeaderValue blank = new HeaderValue("");
        assertThat(blank.getValue()).isEqualTo("");
        assertThat(blank.getParams()).isEmpty();
    }

    @Test
    public void shouldMapNoParams() {

        HeaderValue singleParam = new HeaderValue(NO_PARAMS);
        assertThat(singleParam.getValue()).isEqualTo("my-value");
        assertThat(singleParam.getParams()).isEmpty();

    }

    @Test
    public void shouldMapSingleParam() {

        HeaderValue singleParam = new HeaderValue(SINGLE_PARAM);
        assertThat(singleParam.getValue()).isEqualTo("my-value");
        assertThat(singleParam.getParams()).isEqualTo(ImmutableMap.of("param1", "thing.gif"));
    }

    @Test
    public void shouldMatchMultiParam() {

        HeaderValue multiParam = new HeaderValue(MULTI_PARAM);
        assertThat(multiParam.getValue()).isEqualTo("my-value");
        assertThat(multiParam.getParams()).isEqualTo(
                ImmutableMap.of("param1", "0", "param2", "quoted", "param3", "unquoted"));

    }

    @Test
    public void shouldMatchMultiParamWithSpacePadding() {

        HeaderValue multiParam = new HeaderValue(MULTI_PARAM_SPACES);
        assertThat(multiParam.getValue()).isEqualTo("my-value");
        assertThat(multiParam.getParams()).isEqualTo(
                ImmutableMap.of("param1", "0", "param2", "quoted", "param3", "unquoted"));

    }

    @Test
    public void shouldHaveValue() {

        HeaderValue headerValue = new HeaderValue(SINGLE_PARAM);
        assertThat(headerValue.hasValue("my-value")).isTrue();
        assertThat(headerValue.hasValue("some-other-value")).isFalse();

    }

    @Test
    public void shouldGetParamValue() {

        HeaderValue multiParam = new HeaderValue(MULTI_PARAM_SPACES);
        assertThat(multiParam.getParamValue("param1")).isEqualTo("0");
        assertThat(multiParam.getParamValue("param2")).isEqualTo("quoted");
        assertThat(multiParam.getParamValue("param3")).isEqualTo("unquoted");

        assertThat(multiParam.getParamValue("unknown-param")).isNull();


    }

    @Test
    public void shouldDecodeRFC2231_MultiParam_NoEncoding() {

        String in = "Content-Disposition: attachment;\n" +
                " filename*2=[LAST];\n" +
                " filename*0=[FIRST];\n" +
                " filename*1=[MIDDLE]\n";

        assertThat(new HeaderValue(in).getParamValue("filename"))
                .isEqualTo("[FIRST][MIDDLE][LAST]");
    }

    @Test
    public void shouldDecodeRFC2231_MultiParam_UTF8() {

        String in = "Content-Disposition: attachment;\n" +
                " filename*0*=utf-8''%E7%A7%81%E3%81%AE%E3%83%9B%E3%83%90%E3%83%BC%E3%82%AF;\n" +
                " filename*1*=%E3%83%A9%E3%83%95%E3%83%88%E3%81%AF%E9%B0%BB%E3%81%A7%E3%81;\n" +
                " filename*2*=%84%E3%81%A3%E3%81%B1%E3%81%84%E3%81%A7%E3%81%99\n";

        assertThat(new HeaderValue(in).getParamValue("filename"))
                .isEqualTo("私のホバークラフトは鰻でいっぱいです");
    }

    @Test
    public void shouldDecodeRFC2231_MultiParam_NonUTF8() {

        String in = "Content-Disposition: attachment;\n" +
                " filename*0*=gb2312''%CE%D2%B5%C4%C6%F8%B5%E6%B4%AC%D7%B0%C2%FA%C1;" +
                " filename*1*=%CB%F7%AD%D3%E3";

        assertThat(new HeaderValue(in).getParamValue("filename"))
                .isEqualTo("我的气垫船装满了鳝鱼");
    }

    @Test
    public void shouldDecodeRFC2231_SingleParam() {

        String in = "Content-Disposition: attachment;\n" +
                " filename*=gb2312''%CE%D2%B5%C4%C6%F8%B5%E6%B4%AC%D7%B0%C2%FA%C1%CB%F7%AD%D3%E3";

        assertThat(new HeaderValue(in).getParamValue("filename"))
                .isEqualTo("我的气垫船装满了鳝鱼");
    }

    @Test
    public void shouldDecodeRFC2231_SingleParam_IgnoreEncoding() {

        // lack of a trailing asterisk means don't parse for encoding info
        String in = "Content-Disposition: attachment;\n" +
                " filename=gb2312''%CE%D2%B5%C4%C6%F8%B5%E6%B4%AC%D7%B0%C2%FA%C1%CB%F7%AD%D3%E3";

        assertThat(new HeaderValue(in).getParamValue("filename"))
                .isEqualTo("gb2312''%CE%D2%B5%C4%C6%F8%B5%E6%B4%AC%D7%B0%C2%FA%C1%CB%F7%AD%D3%E3");
    }

    @Test
    public void shouldDecodeRFC2231_MultiParam_IgnoreEncoding() {

        // lack of a trailing asterisk means don't parse for encoding info
        String in = "Content-Disposition: attachment;\n" +
                " filename*0=gb2312''%CE%D2%B5%C4%C6%F8%B5%E6%B4%AC%D7%B0%C2%FA%C1;" +
                " filename*1=%CB%F7%AD%D3%E3";

        assertThat(new HeaderValue(in).getParamValue("filename"))
                .isEqualTo("gb2312''%CE%D2%B5%C4%C6%F8%B5%E6%B4%AC%D7%B0%C2%FA%C1%CB%F7%AD%D3%E3");
    }


}
