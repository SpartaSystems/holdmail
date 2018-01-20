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

package com.spartasystems.holdmail.integration;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.get;

public class BuildInfoIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private WebApplicationContext webAppCtx;

    @Before
    public void setUp() throws Exception {

        super.setUp();
        RestAssuredMockMvc.webAppContextSetup(webAppCtx);
    }

    @Test
    public void shouldConformToBuildInfoSchema() {

        get("/rest/buildInfo").then()
                              .assertThat()
                              .contentType(ContentType.JSON)
                              .statusCode(200)
                              .body(matchesJsonSchemaInClasspath("jsonschema/build-info.json"));

    }

}
