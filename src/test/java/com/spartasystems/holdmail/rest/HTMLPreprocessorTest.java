/*******************************************************************************
 * Copyright 2016 Sparta Systems, Inc
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

package com.spartasystems.holdmail.rest;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HTMLPreprocessorTest {

    @Test
    public void shouldReplaceCIDWithRestPath() throws Exception{

        HTMLPreprocessor preParser = new HTMLPreprocessor();

        assertThat(preParser.preprocess(10, "")).isEqualTo("");
        assertThat(preParser.preprocess(20, "aaaaaaa")).isEqualTo("aaaaaaa");

        assertThat(preParser.preprocess(30, "cid:aaaaaaa"))
                            .isEqualTo("/rest/messages/30/content/aaaaaaa");

        assertThat(preParser.preprocess(40, "aaaaaaacid:"))
                            .isEqualTo("aaaaaaa/rest/messages/40/content/");

        assertThat(preParser.preprocess(50, "aaaaaaacid:aaaaaaa"))
                            .isEqualTo("aaaaaaa/rest/messages/50/content/aaaaaaa");


    }

    @Test
    public void shouldPassThroughNulls() throws Exception {

        HTMLPreprocessor preParser = new HTMLPreprocessor();

        assertThat(preParser.preprocess(1234, null)).isNull();

    }
}
