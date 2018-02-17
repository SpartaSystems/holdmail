#!/usr/bin/env bash

################################################################################
# Copyright 2018 Sparta Systems, Inc
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
# implied.
#
# See the License for the specific language governing permissions and
# limitations under the License.
################################################################################

# Run this to generate a new CHANGELOG file
#
# requires:
#   - https://github.com/skywinder/github-changelog-generator
#     (gem install github_changelog_generator)
#   - Env var CHANGELOG_GITHUB_TOKEN with a  personal access token with 'repo' access

CMD=github_changelog_generator

if [[ ! $(type -P "${CMD}") ]]
then
    echo "Install '${CMD}' at https://github.com/skywinder/github-changelog-generator"
    exit 1
fi

: "${CHANGELOG_GITHUB_TOKEN?See docs at https://github.com/skywinder/github-changelog-generator}"

github_changelog_generator \
    -u spartasystems \
    -p holdmail \
    -o ../CHANGELOG.md \
    --no-pull-requests \
    --since-tag 1.0.0 \
    --header-label "# HoldMail Changelog"







