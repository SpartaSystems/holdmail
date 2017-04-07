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
import {shallow} from "enzyme";

import  MessageItem from "../../../../main/resources/static/js/react/MessageItem";

describe(' MessageItem Component ', () => {
    const currDate = new Date();

    test('SearchBar constructor state defaults ', () =>{
        const loading = new MessageItem();
        expect(loading.state).toEqual(undefined);
    });

    test('SearchBar will render', () => {
        jest.mock('../../../../main/resources/static/js/react/Utils'); // this happens automatically with automocking
        const utils = require('../../../../main/resources/static/js/react/Utils');



        utils.formatMediumDate(() => currDate);
        const message = {
            messageId: 1,
            senderEmail: 'foo@bar.com',
            recipients: ' test me',
            receivedDate: currDate
        };

        const loading = shallow(<MessageItem  key={message.messageId}
                                              message={message}/>);

        const values = loading.text().split(' ');

        let hours = currDate.getHours();
        hours = hours % 12;
        hours = hours ? hours : 12; // the hour '0' should be '12'

        expect(values[0]).toBe('1Apr');
        expect(values[1].trim()).toBe('5,');
        expect(values[2]).toEqual(currDate.getFullYear().toString());
        expect(values[3]).toEqual(`${hours}:${ (currDate.getMinutes()<10?'0':'') + currDate.getMinutes()}:${ (currDate.getSeconds()<10?'0':'') + currDate.getSeconds() }`);
        expect(values[4]).toBe('PMfoo@bar.com');
        expect(values[5]).toBe('test');
        expect(values[6]).toBe('me');
    });


});
