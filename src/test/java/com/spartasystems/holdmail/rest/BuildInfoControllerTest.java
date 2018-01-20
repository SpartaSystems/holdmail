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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.info.BuildProperties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BuildInfoControllerTest {

    @Mock
    private BuildProperties buildPropertiesMock;

    @Mock
    private BuildInfoMapper buildInfoMapperMock;

    @InjectMocks
    private BuildInfoController buildInfoController;

    @Test
    public void shouldGetBuildInfo() {

        BuildInfo expectedInfo = mock(BuildInfo.class);

        when(buildInfoMapperMock.fromProperties(buildPropertiesMock)).thenReturn(expectedInfo);

        assertThat(buildInfoController.getBuildInfo()).isEqualTo(expectedInfo);
    }

}
