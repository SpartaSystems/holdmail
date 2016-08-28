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
require('angular-mocks/ngMock');

require('../../../main/resources/static/js/HoldmailApp');
require('../../../main/resources/static/js/MessageListController');

describe('MessageListcontroller Tests', function () {

    var ctrl, scope;

    beforeEach(angular.mock.module('HoldMailApp'));

    beforeEach(inject(function($rootScope, $controller) {
        scope = $rootScope.$new();
        ctrl = $controller('MessageListController', {$scope: scope});

    }));

    it('should have expected functions', function() {
        expect(ctrl.clearAndFetchMessages).toBeDefined();
        expect(ctrl.fetchMessages).toBeDefined();
        expect(ctrl.hasNoResults).toBeDefined();
        expect(ctrl.rowClick).toBeDefined();
    });

    it('should be initialized correctly', function() {

        // TODO: this is going to fail because the mocking
        // above is resulting in every function in the
        // controller being invoked

        expect(ctrl.items).toEqual([]);
        expect(ctrl.busy).toBeFalsy();
        expect(ctrl.noMorePages).toBeFalsy();
        expect(ctrl.page).toEqual(0);
        expect(ctrl.size).toEqual(40);
    })

})
;

