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
import SearchBar from '../../../../main/resources/static/js/react/SearchBar';

test('SearchBar constructor state defaults ', () =>{
    const searchBar = new SearchBar();
    expect(searchBar.state).toEqual(undefined);
});

test('SearchBar will render', () => {
    const searchBar = shallow(<SearchBar/>);
    expect(searchBar.text()).toEqual('Recipient Email Search!');
});

test('SearchBar will render', () => {
    const searchBar = shallow(<SearchBar/>);
    expect(searchBar.text()).toEqual('Recipient Email Search!');
});

