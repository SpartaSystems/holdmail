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

import axios from 'axios'

const MESSAGES_ENDPOINT = '/rest/messages'

export default {
  getMessageList (size, page, recipientEmail) {
    let params = { size, page }

    if (recipientEmail) {
      params.recipient = recipientEmail
    }

    return axios.get(MESSAGES_ENDPOINT, { params })
  },
  getMessageDetail (messageId) {
    const url = `${MESSAGES_ENDPOINT}/${messageId}`

    return axios.get(url)
  },
  getMessageRAWEndpoint (messageId) {
    return `${MESSAGES_ENDPOINT}/${messageId}/raw`
  },
  forwardMessage (messageId, recipientEmail) {
    const url = `${MESSAGES_ENDPOINT}/${messageId}/forward`

    return axios.post(url, {recipient: recipientEmail})
  }
}
