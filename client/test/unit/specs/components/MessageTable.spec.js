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

import Vue from 'vue'
import Router from 'vue-router'
import messagesApi from '@/api/messages'
import MessageTable from '@/components/MessageTable'
import router from '@/router'

import msgs from '../../api-mocks/message-list'

describe('MessageTable.vue', () => {
  var comp
  var sandbox
  const getComponent = generateComponentMounter(MessageTable)

  beforeEach(() => {
    sandbox = sinon.sandbox.create()
  })

  afterEach(() => {
    sandbox.restore()
  })

  describe('Lifecycle', () => {
    it('fetches messages on mount', () => {
      const stub = sandbox.stub(MessageTable.methods, 'clearAndFetchMessages')

      getComponent()

      expect(stub.calledOnce).to.be.ok
    })
  })

  describe('Rendering', () => {
    it('if no messages, display empty messages pane', (done) => {
      var stub = stubMessageListSuccess(msgs)

      comp = getComponent()

      setTimeout(function () {
        expect(comp.$el.querySelector('.card').style.display).to.equal('none')

        stub.restore()

        stub = stubMessageListSuccess([])

        comp.clearAndFetchMessages()

        setTimeout(function () {
          var card = comp.$el.querySelector('.card')
          expect(card.style.display).to.equal('')
          expect(card.querySelector('.card-title').textContent).to.equal('No Messages')
          expect(card.querySelector('.card-text').textContent).to.equal('No messages found for the current criteria - Try sending some to the mail server!')

          stub.restore()

          done()
        }, 0)
      }, 0)
    })

    it('renders list of messages', (done) => {
      var stub = stubMessageListSuccess(msgs)

      comp = getComponent()

      setTimeout(function () {
        var messages = comp.$el.querySelectorAll('#mailResults table tbody tr')

        expect(messages.length).to.equal(2)
        expect(messages[0].querySelector('.item-id').textContent).to.equal('57')
        expect(messages[0].querySelector('.item-received-date').textContent).to.equal('Mar 20, 2017 02:40:47 PM')
        expect(messages[0].querySelector('.item-sender-mail').textContent).to.equal('holdmail@spartasystems.com')
        expect(messages[0].querySelector('.item-recipients').textContent).to.equal('test@test.com')
        expect(messages[0].querySelector('.item-subject').textContent).to.equal('TEST')
        expect(messages[1].querySelector('.item-id').textContent).to.equal('58')
        expect(messages[1].querySelector('.item-received-date').textContent).to.equal('Mar 20, 2017 02:40:47 PM')
        expect(messages[1].querySelector('.item-sender-mail').textContent).to.equal('holdmail@spartasystems.com')
        expect(messages[1].querySelector('.item-recipients').textContent).to.equal('test@test.com')
        expect(messages[1].querySelector('.item-subject').textContent).to.equal('TEST')

        stub.restore()

        done()
      }, 0)
    })

    it('displays progress bar while busy fetching messages', (done) => {
      deleteModuleCtorCache()

      // stub out mounted() function it calls MessageTable.clearAndFetchMessages()
      // and messes with the timing of this test (would fire clearAndFetchMessages() twice,
      // making the MessageTable.busy true at the time of test)
      sandbox.stub(MessageTable, 'mounted')
      stubMessageList()

      comp = getComponent()

      expect(comp.$el.querySelector('.progress').style.display).to.equal('none')

      comp.clearAndFetchMessages()

      comp.$nextTick(() => {
        expect(comp.busy).to.be.true
        expect(comp.$el.querySelector('.progress').style.display).to.equal('')

        deleteModuleCtorCache()

        done()
      })
    })
  })

  describe('Behavior', () => {
    it('prevents simultaneous fetches of message list', () => {
      var stub = stubMessageList()

      comp = getComponent() // clearAndFetchMessages() called during mount lifecycle phase

      expect(comp.busy).to.be.true // component is already busy now

      comp.clearAndFetchMessages() // attempt a simulatenous fetch

      expect(stub.callCount).to.equal(1) // message list fetch only called once
    })

    it('row click displays message details', (done) => {
      stubMessageListSuccess(msgs)

      Vue.use(Router)
      var Constructor = Vue.extend(MessageTable)

      comp = new Constructor({router}).$mount()

      sandbox.stub(comp.$router, 'push')

      setTimeout(function () {
        triggerEvent(comp, '#mailResults table tbody tr:first-child', 'click')

        expect(comp.$router.push.getCall(0).args[0])
        .to.deep.equal({ name: 'MessageDetail', params: { messageId: 57 } })

        done()
      }, 0)
    })

    describe('Searching', () => {
      it('search is initiated on enter key of search field', () => {
        stubMessageListSuccess(msgs)

        comp = getComponent()

        var clearFetchStub = sandbox.stub(comp, 'clearAndFetchMessages')

        triggerEvent(comp, '#mainSearchTxt', 'keyup', 13)

        expect(clearFetchStub.calledOnce).to.be.ok
      })

      it('search is initiated on clicking search button', (done) => {
        stubMessageListSuccess(msgs)

        comp = getComponent()

        var clearFetchStub = sandbox.stub(comp, 'clearAndFetchMessages')

        comp.$nextTick(function () {
          triggerEvent(comp, '#mainSearchBut', 'click')

          expect(clearFetchStub.callCount).to.equal(1)

          done()
        })
      })

      it('can filter search results by email', (done) => {
        var stub = stubMessageListSuccess(msgs)

        deleteModuleCtorCache()

        sandbox.stub(MessageTable, 'mounted')

        comp = getComponent()

        comp.recipientEmail = 'test@test.com'

        comp.$nextTick(() => {
          expect(comp.$el.querySelector('#mainSearchTxt').value).to.equal('test@test.com')

          triggerEvent(comp, '#mainSearchBut', 'click')

          setTimeout(function () {
            expect(stub.calledWith(40, 0, 'test@test.com')).to.be.true

            deleteModuleCtorCache()

            done()
          }, 0)
        })
      })
    })
  })

  // because Vue.js caches their constructors, one needs to remove
  // the cached constructor when stubbing Module functions (similar to prototypes)
  // so that the next test does not inherit the cached module that has a stubbed
  // function
  function deleteModuleCtorCache () {
    delete MessageTable._Ctor
  }

  function stubMessageList () {
    return sandbox.stub(messagesApi, 'getMessageList').returns(new Promise(() => {}))
  }

  function stubMessageListSuccess (messages) {
    const d = {
      data: { messages }
    }
    const resolved = new Promise((resolve, reject) => resolve(d))

    return sandbox.stub(messagesApi, 'getMessageList')
      .returns(resolved)
  }
})
