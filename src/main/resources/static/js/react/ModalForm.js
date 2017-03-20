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

export default class ModalForm extends React.Component {
    constructor(props) {
        super(props);

    }

    render() {
        return (
            <form className="form-horizontal">
                <div className="form-group">
                    <label className="col-sm-2 control-label">From</label>
                    <div className="col-sm-10">
                        <p className="form-control-static">{this.props.message.senderEmail}</p>
                    </div>
                    <label className="col-sm-2 control-label">Recipient(s)</label>
                    <div className="col-sm-10">
                        <p className="form-control-static">{this.props.message.recipients}</p>
                    </div>
                    <label className="col-sm-2 control-label">Sent</label>
                    <div className="col-sm-10">
                        <p className="form-control-static">{this.props.message.receivedDate/* | date:'medium'*/}</p>
                    </div>
                </div>
            </form>
        );
    }
}

