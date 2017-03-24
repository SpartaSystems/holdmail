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
import MailModal from '../../../../main/resources/static/js/react/MailModal';

test('MailModal constructor state defaults ', () =>{
    debugger;
    let message = {
        messageId:1,
        senderEmail: 'foo@bar.com',
        recipients: 'test , me',
        receivedDate: 'I need to format the date'
    };

    const mailModal = new MailModal({message:message});
    expect(mailModal.state.loading).toEqual(true);
    expect(mailModal.state.messageMetaData).toEqual({});
    expect(mailModal.state.error).toEqual(null);

});

test('MailModal will render', () => {
    const mailModal = shallow(<MailModal/>);
    expect(mailModal.text().trim()).toEqual('GitHub');
});

