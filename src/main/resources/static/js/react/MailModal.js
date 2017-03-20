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
import {Modal, ModalHeader, ModalTitle, ModalClose, ModalBody, ModalFooter} from "react-modal-bootstrap";
import {Tab, Tabs, TabList, TabPanel} from "react-tabs";

export default class MailModal extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            loading: true,
            error: null,
            messageMetaData: {}
        };

    }

    getMessageHTMLURI() {
        const MESSAGES_ENDPOINT = '/rest/messages';
        return MESSAGES_ENDPOINT + '/' + this.props.message.messageId + '/html'
    }

    componentWillMount() {
        fetch(`http://localhost:8080/rest/messages/${this.props.message.messageId}`)
            .then(response => {
                console.log('this.props.message ', this.props.message);
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

        //TODO: finish real forward
        alert('message sent');
    }

    render() {
        let tabList = [];
        let tabContent = [];

        if (this.state.messageMetaData ) {
            if (this.state.messageMetaData.messageHasBodyHTML) {
                tabList.push(<Tab key="HTML Body" className="tab-link">HTML Body</Tab>);

                tabContent.push(<TabPanel key="HTML Body"><ModalForm message={this.props.message}/>
                    <iframe className="mail-summary-content mail-summary-content-html"
                            src={this.getMessageHTMLURI()}></iframe>
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

        return (
            <Modal className="mail-details-modal"
                   size='modal-lg'
                   isOpen={this.props.isOpen}
                   onRequestHide={this.props.hideModal}>
                <ModalBody>
                    <ModalHeader>
                        <h3 className="modal-title">{this.props.message.subject}</h3>
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
                                    <div className="mail-summary-content mail-summary-content-pre mail-summary-content-raw">{this.state.messageMetaData.messageRaw}</div>
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
                                           required=""/>
                                    <span className="input-group-btn">
                                                <button id="mainSearchBut" className="btn btn-primary" type="button"
                                                        onClick={this.forwardMail}
                                                        disabled="disabled">
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
};