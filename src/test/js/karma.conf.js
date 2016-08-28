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

'use strict';

module.exports = function (karma) {
    karma.set({

        basePath: '../../../',

        frameworks: ['browserify', 'jasmine-jquery', 'jasmine'],

        files: [
            { pattern: 'src/test/js/spec/**/*Spec.js', watched: false, included: true, served: true }
        ],

        singleRun: false,

        reporters: ['progress'],

        preprocessors: {
            'src/test/js/spec/**/*Spec.js': ['browserify']
        },

        logLevel: karma.LOG_INFO,

        browsers: ['PhantomJS'],

        browserNoActivityTimeout: 30000,

        browserify: {
            debug: true
        }


    });
};
