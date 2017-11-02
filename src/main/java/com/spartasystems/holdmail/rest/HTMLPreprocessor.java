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

import org.springframework.stereotype.Component;

@Component
public class HTMLPreprocessor {

    private String REST_FMT = "/rest/messages/%d/content/";

    public String preprocess(long messageId, String input) {

        if (input == null) {
            return null;
        }

        /* Right now, the only pre-processing of HTML we do is to replace any 'cid:IDENTIFIER' references
         * in the HTML with a REST URI that will serve the associated content for that IDENTIFIER. */
        return input.replaceAll("cid:", String.format(REST_FMT, messageId));
    }

}
