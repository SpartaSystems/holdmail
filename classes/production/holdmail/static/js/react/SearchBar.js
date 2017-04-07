/*******************************************************************************
 * Copyright 2017 Sparta Systems, Inc
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

export default class SearchBar extends React.Component {
    constructor(props){
        super(props);

        this.state = {
            searchText:'',
            loading: true,
            error: null
        };
    }

    handleChange(event){
        this.setState({
            searchText: event.target.value
        });
        event.preventDefault();
    }

    render() {
        return (
            <div id="mailCriteria" className="mail-criteria">
                <div className="input-group">
                    <div className="input-group-btn">
                        <button id="single-button" type="button" className="btn btn-primary">
                            Recipient Email
                        </button>
                    </div>

                    <input id="mainSearchTxt" type="text" className="form-control"
                           placeholder="Enter full email address: e.g. homer@simpson.com"
                           value={this.state.searchText} onChange={this.handleChange.bind(this)} />

                    <span className="input-group-btn">
                            <button id="mainSearchBut" className="btn btn-success" type="button"
                                    onClick={this.props.clearAndFetchMessages.bind(this, this.state.searchText)}>
                                <span className="glyphicon glyphicon-search" aria-hidden="true"/> Search!
                            </button>
                        </span>
                </div>
            </div>
        );
    }
}

