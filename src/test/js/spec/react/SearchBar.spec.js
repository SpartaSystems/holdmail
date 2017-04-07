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
import {mount} from "enzyme";
import SearchBar from "../../../../main/resources/static/js/react/SearchBar";
describe('SearchBar Component ', () => {
    test('SearchBar constructor state defaults ', () => {
        const searchBar = new SearchBar();
        expect(searchBar.state).toEqual({"error": null, "loading": true, "searchText": ""});
    });

    test('SearchBar will render', () => {

        const clearAndFetchMessages = jest.fn();

        const searchBar = mount(<SearchBar size={0} page={40} clearAndFetchMessages={clearAndFetchMessages}/>);
        let formControl = searchBar.find('#mainSearchTxt');

        expect(searchBar.text()).toEqual('Recipient Email Search!');
        formControl.simulate('change', {target: {value: 'test@test.com'}});

        let button = searchBar.find('#mainSearchBut');
        button.simulate('click');

        expect(clearAndFetchMessages).toHaveBeenCalled();
    });

});