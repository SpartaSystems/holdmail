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

