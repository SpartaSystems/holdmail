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
import {shallow, mount} from "enzyme";
import HoldMail from "../../../../main/resources/static/js/react/HoldMail";
import fetchMock from "fetch-mock";

describe('HoldMail Component ', () => {

    beforeEach(() => {
        let messages;
        beforeEach(() => {
            messages = [{
                messageId: 1,
                senderEmail: 'foo1@bar.com',
                recipients: 'test , me',
                receivedDate: 'I need to format the date'
            },
                {
                    messageId: 2,
                    senderEmail: 'foo@bar2.com',
                    recipients: 'test , me2',
                    receivedDate: 'I need to format the date 2'
                },
                {
                    messageId: 3,
                    senderEmail: 'foo3@bar.com',
                    recipients: 'test , me 3',
                    receivedDate: 'I need to format the date 3'
                }];

            const baseUrl = 'http://localhost:8080/rest/messages?size=40&page=0';
            fetchMock.mock(baseUrl, 200, JSON.stringify(messages));
            fetchMock.mock(`${baseUrl}&recipient=foo3@bar.com`, 200, JSON.stringify(messages[2]));
        });
    });

    test('HoldMail constructor state defaults ', () => {
        const holdMail = new HoldMail();
        expect(holdMail.state.error).toEqual(null);
        expect(holdMail.state.loading).toEqual(true);
        expect(holdMail.state.messages).toEqual([]);
        expect(holdMail.state.modalMessage).toEqual({});
        expect(holdMail.state.showModal).toEqual(false);

    });


    test('HoldMail should render', () => {
        const holdMail = shallow(<HoldMail/>);
        expect(holdMail.text()).toEqual('<Header /><SearchBar /><MailView />');
    });

    test('HoldMail clearAndFetchMessages should return all messages',()=>{
        const holdMail = mount(<HoldMail size={40} page={0} />);

        let formControl = holdMail.find('#mainSearchTxt');

        expect(holdMail.text().trim()).toEqual('GitHubRecipient Email Search!Fetching...');
        formControl.simulate('change', {target: {value: 'foo3@bar.com'}});

        let button = holdMail.find('#mainSearchBut');
        button.simulate('click');

    });



});