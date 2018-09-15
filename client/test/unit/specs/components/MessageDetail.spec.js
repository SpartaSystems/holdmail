/*******************************************************************************
 * Copyright 2017 - 2018 Sparta Systems, Inc
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
import moment from 'moment'
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

    it('displays the mail delivery information', (done) => {
      stubMessageDetailSuccess(message1)
      comp = getMountedComponent()

      Vue.nextTick(() => {
        let dateInLocalFMT = moment(1490035247552).format('MMM D, YYYY hh:mm:ss A')
        expect(comp.$el.querySelector('.message-sender').textContent).to.equal('holdmail@spartasystems.com')
        expect(comp.$el.querySelector('.message-recipients').textContent).to.equal('test@test.com')
        expect(comp.$el.querySelector('.message-received-date').textContent).to.equal(dateInLocalFMT)
        done()
      })
    })

    it('displays attachments row with links when attachments are present', (done) => {
      stubMessageDetailSuccess(message1)
      comp = getMountedComponent()

      Vue.nextTick(() => {
        let attachRow = comp.$el.querySelector('.message-attach-row')
        expect(attachRow.style.display).to.equal('')

        let items = attachRow.querySelectorAll('.attach-item')
        expect(items.length).to.equal(2)
        expect(items[0].querySelector('.attach-link-text').textContent).to.equal('att-CN-你好世界.png')
        expect(items[0].querySelector('.attach-size').textContent).to.equal('(1.25KB)')
        expect(items[0].querySelector('.attach-link').href).to.contain('/rest/messages/57/att/5')

        expect(items[1].querySelector('.attach-link-text').textContent).to.equal('att-JP-ありがとうございます.png')
        expect(items[1].querySelector('.attach-size').textContent).to.equal('(1.22KB)')
        expect(items[1].querySelector('.attach-link').href).to.contain('/rest/messages/57/att/10')
        done()
      })
    })

    it('does not display attachments row when no attachments are present', (done) => {
      // message2 doesn't have attachments
      // (attachments from the API with "disposition: inline" aren't shown as linked attachments in the UI)
      stubMessageDetailSuccess(message2)
      comp = getMountedComponent()

      Vue.nextTick(() => {
        expect(comp.$el.querySelector('.message-attach-row').style.display).to.equal('none')
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

    describe('XSS', () => {
      it('all iframes are sandboxed', (done) => {
        stubMessageDetailSuccess(message1)

        comp = getMountedComponent()

        Vue.nextTick(() => {
          const sandboxedIFrames = Array.from(comp.$el.querySelectorAll('iframe'))
            .filter(iframe => iframe.hasAttribute('sandbox'))

          // currently 2 iframes on the message detail view
          expect(sandboxedIFrames).to.have.length(2)

          done()
        })
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

  describe('Computed Properties', () => {
    describe('messageRawEndpoint', () => {
      it('returns empty string if message detail not fetched yet', () => {
        comp = getMountedComponent()

        expect(comp.messageRawEndpoint).to.equal('')
      })

      it('returns correct url path', () => {
        stubMessageDetailSuccess(message1)

        comp = getMountedComponent()

        expect(comp.messageRawEndpoint).to.equal('/rest/messages/57/raw')
      })
    })

    describe('attachmentList', () => {
      it('return empty list if no attachments', () => {
        // message two has no attachments, of which 1 is inline
        stubMessageDetailSuccess(message2)
        comp = getMountedComponent()
        expect(comp.attachmentList).to.have.lengthOf(0)
      })

      it('filters out inline attachments', () => {
        // message one has three attachments, of which 1 is inline
        stubMessageDetailSuccess(message1)
        comp = getMountedComponent()
        expect(comp.attachmentList).to.have.lengthOf(2)
      })

      it('delivers existing attribs while adding downloadURI', () => {
        // message one has three attachments, of which 1 is inline
        stubMessageDetailSuccess(message1)
        comp = getMountedComponent()
        expect(comp.attachmentList[0]).to.include({
          // let's not test all, just sanity check a couple
          'attachmentId': '5',
          'filename': 'att-CN-你好世界.png',
          // see that the additional attrib is added
          'downloadURI': '/rest/messages/57/att/5'
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
