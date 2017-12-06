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
import router from '@/router'
import MessageDetail from '@/components/MessageDetail'
import messagesApi from '@/api/messages'
import message1 from '../../api-mocks/message-detail-1'
import message2 from '../../api-mocks/message-detail-2'

describe('MessageDetail.vue', () => {
  var comp
  var sandbox

  beforeEach(() => {
    sandbox = sinon.sandbox.create()
  })

  afterEach(() => {
    sandbox.restore()
  })

  describe('Lifecycle', () => {
    it('fetches message details on mount', () => {
      stubMessageDetailSuccess(message1)

      comp = getMountedComponent()

      expect(messagesApi.getMessageDetail.getCall(0).args[0]).to.equal(57)
    })
  })

  describe('Rendering', () => {
    it('displays the message subject in the modal header', (done) => {
      stubMessageDetailSuccess(message1)

      comp = getMountedComponent()

      Vue.nextTick(() => {
        expect(comp.$el.querySelector('.message-subject').textContent).to.equal('TEST')
        done()
      })
    })

    it('displays three tabs', (done) => {
      stubMessageDetailSuccess(message1)

      comp = getMountedComponent()

      Vue.nextTick(() => {
        var tabs = comp.$el.querySelectorAll('.tabs .nav-tabs .nav-item')

        expect(tabs[0].querySelector('a').textContent).to.equal('HTML Body')
        expect(tabs[1].querySelector('a').textContent).to.equal('Text Body')
        expect(tabs[2].querySelector('a').textContent).to.equal('Original Content')

        done()
      })
    })

    it('if no message body html, disables the tab', (done) => {
      stubMessageDetailSuccess(message1)

      comp = getMountedComponent()

      Vue.nextTick(() => {
        var tabs = comp.$el.querySelectorAll('.tabs .nav-tabs .nav-link')

        expect(tabs[0].classList.contains('disabled')).to.be.true

        done()
      })
    })

    it('if no message body text, disables the tab', (done) => {
      stubMessageDetailSuccess(message1)

      comp = getMountedComponent()

      Vue.nextTick(() => {
        var tabs = comp.$el.querySelectorAll('.tabs .nav-tabs .nav-link')

        expect(tabs[1].classList.contains('disabled')).to.be.true

        done()
      })
    })

    describe('tab selection', () => {
      var expectSelectedTab = function (msgHasHTML, msgHasText, expectedTabText, done) {
        stubMessageDetailSuccess(
          Object.assign({}, message1, {
            'messageHasBodyHTML': msgHasHTML,
            'messageHasBodyText': msgHasText
          }))

        comp = getMountedComponent()

        setTimeout(() => {
          var activeTab = comp.$el.querySelector('.nav-link.active')
          expect(activeTab.text).to.equal(expectedTabText)

          done()
        }, 0)
      }

      it('should select html tab if html and text present', (done) => {
        expectSelectedTab(true, true, 'HTML Body', done)
      })

      it('should select html tab if html but no text present', (done) => {
        expectSelectedTab(true, false, 'HTML Body', done)
      })

      it('should select text tab if text but no html present', (done) => {
        expectSelectedTab(false, true, 'Text Body', done)
      })

      it('should select original tab neither text nor html present', (done) => {
        expectSelectedTab(false, false, 'Original Content', done)
      })
    })
  })

  describe('Behavior', () => {
    describe('Forwarding', () => {
      it('can forward an email to another address', (done) => {
        stubMessageDetailSuccess(message2)
        var stub = stubForwardMessageSuccess()

        comp = getMountedComponent()
        comp.forwardRecipient = 'test@test.com'

        sandbox.stub(comp.$validator, 'validateAll').returnsPromise().resolves()

        comp.$nextTick(() => {
          var forwardInput = comp.$el.querySelector('input[name="forwardEmail"]')
          expect(forwardInput.value).to.equal('test@test.com')

          triggerEvent(comp, '#fwdButton', 'click')

          expect(stub.getCall(0).args).to.deep.equal([63, 'test@test.com'])

          setTimeout(function () {
            var alert = comp.$el.querySelector('#forward-alert')

            expect(alert.textContent.trim()).to.equal('Mail 63 successfully sent to test@test.com')

            stub.restore()
            done()
          }, 0)
        })
      })

      describe('Validation', () => {
        it('displays error message if not a valid email', (done) => {
          stubMessageDetailSuccess(message2)
          comp = getMountedComponent()
          comp.forwardRecipient = 'not valid email'

          sandbox.stub(comp.$validator, 'validateAll').returnsPromise().resolves()
          sandbox.stub(comp.errors, 'has').withArgs('forwardEmail').returns(true)
          sandbox.stub(comp.errors, 'first').withArgs('forwardEmail').returns('Not a valid email')

          comp.$nextTick(() => {
            triggerEvent(comp, '#fwdButton', 'click')

            comp.$nextTick(() => {
              var alert = comp.$el.querySelector('#forward-error')

              expect(alert.textContent.trim()).to.equal('Not a valid email')

              done()
            })
          })
        })
      })
    })
  })

  const getComponent = () => {
    Vue.use(Router)
    var Constructor = Vue.extend(MessageDetail)
    let comp = new Constructor({router})

    comp.$route.params.messageId = 57

    return comp
  }

  const getMountedComponent = () => {
    return getComponent().$mount()
  }

  function stubMessageDetailSuccess (details) {
    const d = {
      data: details
    }

    return sandbox.stub(messagesApi, 'getMessageDetail').returnsPromise().resolves(d)
  }

  function stubForwardMessageSuccess (messages) {
    return sandbox.stub(messagesApi, 'forwardMessage').returnsPromise().resolves()
  }
})
