import React from "react";
import {shallow, mount} from "enzyme";
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

