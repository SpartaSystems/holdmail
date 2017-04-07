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

import React from "react";
import {mount, shallow} from "enzyme";
import MailView from "../../../../main/resources/static/js/react/MailView";

describe('MailView Component ', () => {
    jest.mock('../../../../main/resources/static/js/react/Utils'); // this happens automatically with automocking
    const utils = require('../../../../main/resources/static/js/react/Utils');

    const currDate = new Date();

    utils.formatMediumDate(() => currDate);

    test('MailView constructor state defaults ', () => {
        const modalForm = new MailView();
        expect(modalForm.state.error).toEqual(null);
        expect(modalForm.state.loading).toEqual(true);
        expect(modalForm.state.messageMetaData).toEqual({});
        expect(modalForm.state.messages).toEqual([]);
        expect(modalForm.state.showModal).toEqual(false);

    });
    test('')

});