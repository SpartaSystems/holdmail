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

import React from 'react';
import {shallow} from 'enzyme';
import HoldMail from '../../../../main/resources/static/js/react/HoldMail';

test('HoldMail constructor state defaults ', () =>{
    const holdMail = new HoldMail();
    expect(holdMail.state.error).toEqual(null);
    expect(holdMail.state.loading).toEqual(true);
    expect(holdMail.state.messages).toEqual([]);
    expect(holdMail.state.modalMessage).toEqual({});
    expect(holdMail.state.showModal).toEqual(false);

});

test('HoldMailView will render', () => {
    const holdMail = shallow(<HoldMail/>);
    expect(holdMail.text()).toEqual('<Header /><SearchBar /><MailView />');
});

