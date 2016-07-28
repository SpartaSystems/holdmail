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

package com.spartasystems.holdmail.exception;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HoldMailExceptionTest {

    @Test
    public void shouldSetMessage() throws Exception{

        final String MESSAGE = "the message";
        final Throwable CAUSE = new Throwable();

        HoldMailException exception = new HoldMailException(MESSAGE, CAUSE);

        assertThat(exception.getMessage()).isEqualTo(MESSAGE);
        assertThat(exception.getCause()).isEqualTo(CAUSE);

    }

}
