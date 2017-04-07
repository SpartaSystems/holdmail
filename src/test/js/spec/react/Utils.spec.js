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

import {formatMediumDate, isEmailAddress} from "../../../../main/resources/static/js/react/Utils";

describe('Utils', () => {
    test('formatMediumDate ', () => {
        let mediumDate = formatMediumDate(new Date("Tue, 04 Apr 2017 20:30:13"));
        expect(mediumDate).toBe('Apr 2, 2017 8:30:13 PM');
    });
    describe('isEmailAddress', () => {
        test('should return true if the email address is valid', () => {
            let mediumDate = isEmailAddress('test@test.com');
            expect(mediumDate).toBe(true);
        });
        test('should return false if the email address is not valid', () => {
            let mediumDate = isEmailAddress('test bad email address');
            expect(mediumDate).toBe(false);
        });
    });
});