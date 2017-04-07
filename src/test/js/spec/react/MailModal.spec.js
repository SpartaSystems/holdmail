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
import fetchMock from "fetch-mock";
import MailModal from "../../../../main/resources/static/js/react/MailModal";

describe('MailModal Component ', () => {
    let message;
    beforeEach(() => {
        message = {
            messageId: 1,
            senderEmail: 'foo@bar.com',
            recipients: 'test , me',
            receivedDate: 'I need to format the date'
        };
    });
    afterEach(()=>{
        fetchMock.restore();
    });
    test('MailModal constructor state defaults ', () => {
        fetchMock.mock('http://localhost:8080/rest/messages/1/forward', 200, JSON.stringify(message));

        const mailModal = new MailModal();
        expect(mailModal.state.loading).toEqual(true);
        expect(mailModal.state.messageMetaData).toEqual({});
        expect(mailModal.state.error).toEqual(null);

    });

    test('MailModal will render shallow', () => {
        fetchMock.mock('http://localhost:8080/rest/messages/1', 200, JSON.stringify(message));

        const mailModal = shallow(<MailModal isOpen={true} message={message} />);

        expect(mailModal.text().trim()).toEqual('<Modal />');
        expect(mailModal.unrendered.props.message).toBe(message);
        expect(mailModal.unrendered.props.isOpen).toBe(true);
        expect(mailModal.unrendered.props.forwardButtonDisabled).toBe('disabled');
        expect(mailModal.unrendered.props.alertVisible).toBe(false);



    });

    test('MailModal forwardMail post fetch', () => {
        fetchMock.mock('http://localhost:8080/rest/messages/1', 200, JSON.stringify(message));

        const forwardFunction = MailModal.prototype.forwardMail;
        MailModal.prototype.forwardMail = jest.fn();

        const mailModalComp = mount(<MailModal isOpen={true}
                                               message={message}
        />);
        const formControl = mailModalComp.find('#forwardRecipientTxt');

        formControl.simulate('change', {target: {value: 'foo3@bar.com'}});

        let searchButton = mailModalComp.find('#mainSearchBut');

        searchButton.simulate('click');

        expect(MailModal.prototype.forwardMail).toHaveBeenCalled();
        MailModal.prototype.forwardMail = forwardFunction;

    });

    test('MailModal forwardMail post fetch error', () => {

        let message2 = {
            messageId: 2,
            senderEmail: 'foo2@bar.com',
            recipients: 'test , me 2',
            receivedDate: 'I need to format the date 2'
        };
        fetchMock.get('http://localhost:8080/rest/messages/2', 200, message2);
        fetchMock.post('http://localhost:8080/rest/messages/2/forward', 500).catch(500);

        const mailModalComp = mount(<MailModal isOpen={true}
                                               message={message2}
        />);

        let searchButton = mailModalComp.find('#mainSearchBut');

        searchButton.simulate('click');


    });
});

