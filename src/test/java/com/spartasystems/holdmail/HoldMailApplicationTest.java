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

package com.spartasystems.holdmail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.SpringApplication;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SpringApplication.class)
public class HoldMailApplicationTest {

    @Test
    public void shouldInvokeSpringApp() {

        String[] args = {"a", "b", "c"};

        PowerMockito.mockStatic(SpringApplication.class);
        PowerMockito.when(SpringApplication.run(HoldMailApplication.class, args)).thenReturn(null);

        HoldMailApplication.main(args);

        // https://github.com/jayway/powermock/wiki/MockitoUsage#how-to-verify-exact-number-of-calls
        PowerMockito.verifyStatic(Mockito.times(1));
        SpringApplication.run(HoldMailApplication.class, args);

    }

}
