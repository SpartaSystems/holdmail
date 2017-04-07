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

export default class Header extends React.Component {
    render() {
        return (
            <nav className="holdmail-nav navbar navbar-default navbar-static-top">
                <div className="container-fluid">
                    <div className="navbar-header">
                        <a href="#" className="navbar-left">
                            <img src="images/holdmail-header-logo.png" alt="HoldMail Logo" title="HoldMail Home"
                                 height="40"/>
                        </a>
                    </div>

                    <div id="navbar" className="navbar-collapse collapse">
                        <ul className="nav navbar-nav navbar-right">
                            <li>
                                <a href="https://github.com/spartasystems/holdmail" target="_blank" rel="noopener">
                                    <i className="holdmail-nav-link fa fa-github"/> GitHub</a>
                            </li>
                        </ul>
                    </div>

                </div>
            </nav>
        );
    }
}


