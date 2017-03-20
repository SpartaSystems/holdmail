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
import Moment from "moment";

export default class MessageItem extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        const formattedReceivedDate = this.formatReceivedDate(this.props.message.receivedDate);
        return (
            <tr onClick={this.props.handleClick}>
                <td className="no-wrap">{this.props.message.messageId}</td>
                <td className="no-wrap">{formattedReceivedDate}</td>
                <td className="no-wrap">{this.props.message.senderEmail}</td>
                <td className="no-wrap">{this.props.message.recipients}</td>
                <td className="no-wrap">{this.props.message.subject}</td>
            </tr>
        );
    }
    formatReceivedDate(receivedDate) {
        return Moment(receivedDate).format('MMM d, YYYY h:mm:ss A')
    }
}

