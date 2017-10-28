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

package com.spartasystems.holdmail.rest;

import com.spartasystems.holdmail.mapper.BuildInfoMapper;
import com.spartasystems.holdmail.model.BuildInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rest/buildInfo", produces = MediaType.APPLICATION_JSON_VALUE)
public class BuildInfoController {

    @Autowired
    private BuildProperties buildProperties;

    @Autowired
    private BuildInfoMapper buildInfoMapper;

    @RequestMapping()
    public BuildInfo getBuildInfo() {

        return buildInfoMapper.fromProperties(buildProperties);
    }

}
