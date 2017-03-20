import React from 'react';
import {shallow} from 'enzyme';
import Header from '../../../../main/resources/static/js/react/Header';

test('Header constructor state defaults ', () =>{
    const header = new Header();
    expect(header.state).toEqual(undefined);
});

test('Header will render', () => {
    const header = shallow(<Header/>);
    expect(header.text().trim()).toEqual('GitHub');
});

