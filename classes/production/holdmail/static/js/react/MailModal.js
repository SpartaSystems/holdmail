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
import ModalForm from "./ModalForm";
import {Modal, ModalBody, ModalClose, ModalFooter, ModalHeader, ModalTitle} from "react-modal-bootstrap";
import {Tab, TabList, TabPanel, Tabs} from "react-tabs";
import {isEmailAddress} from "./Utils";
import {Alert} from "react-bs-notifier";

export default class MailModal extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            loading: true,
            error: null,
            messageMetaData: {},
            forwardButtonDisabled: 'disabled'
        };

    }

    getMessageHTMLURI() {
        const MESSAGES_ENDPOINT = '/rest/messages';
        return MESSAGES_ENDPOINT + '/' + this.props.message.messageId + '/html'
    }

    componentWillMount() {
        fetch(`http://localhost:8080/rest/messages/${this.props.message.messageId}`)
            .then(response => {
                response.json().then(json => {

                    this.setState({
                        loading: false,
                        messageMetaData: json
                    });
                })
            })
            .catch(err => {
                // Something went wrong. Save the error in state and re-render.
                this.setState({
                    loading: false,
                    error: err
                });
                return new Error("Fetching messages failed");
            });

    }

    forwardMail() {
        let messageId = this.props.message.messageId;
        const data = JSON.stringify({recipient: this.state.forwardText});
        console.log('this', this);
        fetch(`http://localhost:8080/rest/messages/${messageId}/forward`, {
            method: 'post',
            headers: {
                'Accept': 'application/json, text/plain, */*',
                'Content-Type': 'application/json'
            },
            body: data
        }).then((response) => {
            console.log('save json', response.ok);

            console.log('this', this);
            if (response.ok) {
                console.log('this', this);
                this.setStata({
                    responseOk: response.ok
                })
            }

            return response.text().then((text)=> {
                return text ? JSON.parse(text) : {}
            });
        }).then((result) => console.log('save json result', result)
        )
            .catch((error) => console.log('Request failed', error)
            );

    }

    handleChange(event) {
        const EMAIL_ADDRESS = event.target.value;
        let forwardButtonDisabled = 'disabled';
        if (isEmailAddress(EMAIL_ADDRESS)) {
            forwardButtonDisabled = ''
        }

        console.log('forwardButtonDisabled ', forwardButtonDisabled);
        this.setState({
            forwardText: EMAIL_ADDRESS,
            forwardButtonDisabled: forwardButtonDisabled
        });

        event.preventDefault();
    }

    render() {
        let tabList = [];
        let tabContent = [];

        if (this.state.messageMetaData) {
            if (this.state.messageMetaData.messageHasBodyHTML) {
                tabList.push(<Tab key="HTML Body" className="tab-link">HTML Body</Tab>);

                tabContent.push(<TabPanel key="HTML Body"><ModalForm message={this.props.message}/>
                    <iframe className="mail-summary-content mail-summary-content-html"
                            src={this.getMessageHTMLURI()}/>
                </TabPanel>);

            }
            if (this.state.messageMetaData.messageHasBodyText) {
                tabList.push(<Tab key="Text Body" className="tab-link">Text Body</Tab>);

                tabContent.push(<TabPanel key="Text Body">
                    <ModalForm message={this.props.message}/>
                    <div
                        className="mail-summary-content mail-summary-content-pre">{this.state.messageMetaData.messageBodyText}</div>
                </TabPanel>);
            }
        }

console.log('this.state.responseOk');
        let growl;
        if (this.state.responseOk) {
            growl = <Alert type="danger" headline="Error!">
                Holy cow, man!
            </Alert>
        }
        return (
            <Modal className="mail-details-modal"
                   size='modal-lg'
                   isOpen={this.props.isOpen}
                   onRequestHide={this.props.hideModal}>
                <ModalBody>
                    <ModalHeader>
                        <h3 className="modal-title">{this.props.message.subject}</h3>
                        {growl}
                    </ModalHeader>
                    <div className="modal-body">
                        <div className="mail-summary ng-isolate-scope">
                            <Tabs onSelect={this.handleSelect}
                                  selectedIndex={0}>
                                <TabList>
                                    {tabList}
                                    <Tab className="tab-link">Original Message</Tab>
                                </TabList>
                                {tabContent}
                                <TabPanel>
                                    <div
                                        className="mail-summary-content mail-summary-content-pre mail-summary-content-raw">{this.state.messageMetaData.messageRaw}</div>
                                </TabPanel>
                            </Tabs>
                        </div>
                    </div>
                </ModalBody>
                <ModalFooter>
                    <div className="row">
                        <div className="col-lg-6">
                            <form name="forwardForm"
                                  className="ng-pristine ng-valid-email ng-invalid ng-invalid-required">
                                <div className="input-group">
                                    <input id="forwardRecipientTxt" type="email"
                                           className="form-control ng-pristine ng-untouched ng-empty ng-valid-email ng-invalid ng-invalid-required"
                                           placeholder="Forward To: e.g. homer@simpson.com"
                                           required=""
                                           value={this.state.searchText}
                                           onChange={this.handleChange.bind(this)}/>
                                    <span className="input-group-btn">
                                                <button id="mainSearchBut" className="btn btn-primary" type="button"
                                                        onClick={this.forwardMail.bind(this)}
                                                        disabled={this.state.forwardButtonDisabled}>
                                                    Forward <span className="glyphicon glyphicon-forward"
                                                                  aria-hidden="true"/>
                                                </button>
                                            </span>
                                </div>
                            </form>
                        </div>
                        <div className="col-lg-6">
                            <button onClick={this.props.hideModal}
                                    id="mainSearchBut2"
                                    className="btn btn-default"
                                    type="button">
                                <span className="glyphicon glyphicon-remove" aria-hidden="true"/> Close
                            </button>
                        </div>
                    </div>
                </ModalFooter>
            </Modal>
        );
    }
}

MailModal
    .propTypes = {
    isOpen: React.PropTypes.bool,
    message: React.PropTypes.object,
    messageMetaData: React.PropTypes.object,
    hideModal: React.PropTypes.func,
    forwardButtonDisabled: React.PropTypes.string,
    responseOk: React.PropTypes.bool
};

MailModal.defaultProps = {
    forwardButtonDisabled: 'disabled',
    responseOk: true
};