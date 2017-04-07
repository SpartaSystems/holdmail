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
import Header from "./Header";
import MailView from "./MailView";
import SearchBar from "./SearchBar";

export default class HoldMail extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            messages: [],
            messageMetaData: {},
            modalMessage: {},
            showModal: false,
            loading: true,
            error: null
        };
    }
    clearAndFetchMessages(searchText) {
        this.setState({
            loading:true
        });
        let recipient = '';
        if(searchText) {
            recipient = `&recipient=${searchText}`;
        }

        fetch(`http://localhost:8080/rest/messages?size=${this.props.size}&page=${this.props.page}${recipient}`)
            .then(response => {
                response.json().then(json => {
                    this.setState({
                        loading:false,
                        messages: json.messages
                    });
                })
            })
            .catch(err => {
                // Something went wrong. Save the error in state and re-render.
                this.setState({
                    loading:true,
                    error: err
                });
                return new Error("Fetching messages failed");
            });

    }
    componentWillMount() {
        this.clearAndFetchMessages('');
    }



    render() {
        return (
            <div>
                <Header/>
                <div className="container">
                    <div className="row">
                        <div id="mailCriteriaAndResults" className="col-lg-12">
                            <SearchBar size={this.props.size} page={this.props.page}  clearAndFetchMessages={this.clearAndFetchMessages.bind(this)}  />
                            <MailView size={this.props.size} page={this.props.page} messages={this.state.messages} loading={this.state.loading}/>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}


HoldMail.defaultProps = {
    page: 0,
    size: 40
};

