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

require('angular');
require('angular-route');
require('angular-growl-v2');
require('angular-ui-bootstrap');
require('ng-infinite-scroll');

(function () {
    'use strict';

    var HoldMailApp = angular.module('HoldMailApp', ['ui.bootstrap', 'angular-growl', 'infinite-scroll']);

    HoldMailApp.config([

        'growlProvider', function (growlProvider) {
            growlProvider.globalTimeToLive({success: 3000, error: -1, warning: 3000, info: 3000});
        }]);


}());
