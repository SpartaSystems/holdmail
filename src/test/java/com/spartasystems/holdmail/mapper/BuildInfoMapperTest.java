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

package com.spartasystems.holdmail.mapper;

import com.spartasystems.holdmail.model.BuildInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.info.BuildProperties;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BuildInfoMapperTest {

    public static final Date   BUILD_TIME    = new Date();
    public static final String BUILD_VERSION = "myVersion";

    @Mock
    private BuildProperties buildPropertiesMock;

    @InjectMocks
    private BuildInfoMapper buildInfoMapper;

    @Test
    public void shouldMapFromProperties() {

        when(buildPropertiesMock.getVersion()).thenReturn(BUILD_VERSION);
        when(buildPropertiesMock.getTime()).thenReturn(BUILD_TIME);

        BuildInfo expected = new BuildInfo(BUILD_VERSION, BUILD_TIME);

        assertThat(buildInfoMapper.fromProperties(buildPropertiesMock))
                .isEqualTo(expected);

    }

}
