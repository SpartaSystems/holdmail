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
import ModalForm from "../../../../main/resources/static/js/react/ModalForm";


describe('<ModalForm>', ()=>{
let message;
    beforeEach(()=>{

        message = {
            senderEmail: 'foo@bar.com',
            recipients: 'test , me',
            receivedDate: 'I need to format the date'
        };
    });
    test('ModalForm constructor state defaults ', () => {
        const modalForm = new ModalForm();
        expect(modalForm.state).toEqual(undefined);
    });


    test('ModalForm will render value', ()=>{

        const expected = Array.from(
            shallow(<ModalForm message={message}/>)
                .find('.form-control-static')
        );

        expect(expected.length).toBe(3);
        expect(expected[0].props.children).toEqual(message.senderEmail);
        expect(expected[1].props.children).toEqual(message.recipients);
        expect(expected[2].props.children).toEqual(message.receivedDate);

    });

});

