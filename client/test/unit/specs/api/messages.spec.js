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

import messagesApi from '@/api/messages'
import axios from 'axios'

describe('messages api', () => {
  it('getMessageList()', () => {
    var stub = sinon.stub(axios, 'get').returns('mock promise')

    var mock = messagesApi.getMessageList(40, 1, 'test@test.com')

    expect(mock).to.equal('mock promise')
    expect(stub.calledWith('/rest/messages'), {size: 40, page: 1, recipient: 'test@test.com'}).to.be.true

    stub.restore()
  })

  it('getMessageDetail()', () => {
    var stub = sinon.stub(axios, 'get').returns('mock promise')

    var mock = messagesApi.getMessageDetail(63)

    expect(mock).to.equal('mock promise')
    expect(stub.calledWith('/rest/messages/63')).to.be.true

    stub.restore()
  })

  it('forwardMessage()', () => {
    var stub = sinon.stub(axios, 'post').returns('mock promise')

    var mock = messagesApi.forwardMessage(63, 'test@test.com')

    expect(mock).to.equal('mock promise')
    expect(stub.calledWith('/rest/messages/63/forward'), {recipient: 'test@test.com'})

    stub.restore()
  })
})
