import React from 'react';
import {shallow} from 'enzyme';
import Loading from '../../../../main/resources/static/js/react/Loading';

test('SearchBar constructor state defaults ', () =>{
    const loading = new Loading();
    expect(loading.state).toEqual(undefined);
});

test('SearchBar will render', () => {
    const loading = shallow(<Loading/>);
    expect(loading.text()).toEqual('Fetching...');
});

