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

export default class NoMessage extends React.Component {
    render() {
        return (
            <tr>
                <div className="well well-lg">
                    <h3 className="text-center">No Messages</h3>
                    <p className="text-center">No messages found for the current criteria - Try sending some to the mail
                        server!</p>
                </div>
            </tr>
        );
    }
}


