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
import MessageItem from "./MessageItem";
import Loading from "./Loading";
import MailModal from "./MailModal";

export default class MailView extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            messages: [],
            messageMetaData: {},
            showModal: false,
            loading: true,
            error: null
        };

        this.handleHideModal = this.handleHideModal.bind(this);

    }

    handleShowModal(message) {

        this.setState({
            showModal: true,
            modalMessage: message
        });
    }

    handleHideModal() {
        this.setState({
            showModal: false
        });
    }

    render() {
        if (this.props.loading) {
            return (
                <Loading/>
            );
        }

        const messageList = this.props.messages.sort((messageA, messageB) => messageA.messageId - messageB.messageId)
            .map(message => {
                return (
                    <MessageItem key={message.messageId}
                                 message={message}
                                 handleClick={this.handleShowModal.bind(this, message)}/>
                );
            });

        let modalElement = null;
        if (this.state.showModal) {
            modalElement = <MailModal isOpen={this.state.showModal}
                                      message={this.state.modalMessage}
                                      hideModal={this.handleHideModal}/>;
        }
        return (<div id="mailResults">
                <table className="table table-condensed table-striped table-bordered table-hover">
                    <thead>
                    <tr>
                        <th width="2%">ID</th>
                        <th width="8%">Received</th>
                        <th width="15%">From</th>
                        <th width="25%">Recipients</th>
                        <th width="50%">Subject</th>
                    </tr>
                    </thead>
                    <tbody>
                    {messageList}
                    </tbody>
                </table>
                {modalElement}
            </div>
        );
    }
}

MailView.defaultProps = {
    page: 0,
    size: 40
};


