import React from 'react';
import {shallow} from 'enzyme';
import SearchBar from '../../../../main/resources/static/js/react/SearchBar';

test('SearchBar constructor state defaults ', () =>{
    const searchBar = new SearchBar();
    expect(searchBar.state).toEqual(undefined);
});

test('SearchBar will render', () => {
    const searchBar = shallow(<SearchBar/>);
    expect(searchBar.text()).toEqual('Recipient Email Search!');
});

