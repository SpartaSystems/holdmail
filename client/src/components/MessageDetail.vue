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

<template>
  <div id="message-detail">
    <b-alert id="forward-alert" :show="dismissCountDown" variant="success" @dismiss-count-down="countDownChanged">
      Mail {{ message.messageId }} successfully sent to <strong>{{ forwardRecipient }}</strong>
    </b-alert>
    <b-card header-tag="header" class="details-container">
      <div slot="header" class="p-1">
        <b-button id="fwdButton" class="pull-left" size="sm" variant="primary" onclick="history.back()">
          <span class="fa fa-arrow-left" aria-hidden="true"></span>
        </b-button>
        <h4 class="pl-2 pull-left message-subject">{{ message.subject }}</h4>
        <div class="btn-group pull-right">
          <b-button id="fwdButton" size="sm" variant="primary">
            Forward
            <span class="fa fa-forward" aria-hidden="true"></span>
          </b-button>
        </div>
      </div>
      <table class="table table-sm table-condensed addresses">
          <tr>
            <td class=""><strong>From:</strong></td>
            <td>{{message.senderEmail}}</td>
            <td class="text-right pr-2">{{ receivedDate | date('%b %-d, %Y %r') }}</td>
          </tr>
          <tr>
            <td><strong>To:</strong></td>
            <td colspan="2">{{message.recipients}}</td>
          </tr>
      </table>
      <b-tabs :no-fade="true" ref="tabs">
        <b-tab id="original-message" title="Original Message">
          <div class="mail-summary-content mail-summary-content-pre mail-summary-content-raw">{{ message.messageRaw }}</div>
        </b-tab>
        <b-tab id="html-body" title="HTML Body" :disabled="!message.messageHasBodyHTML">
            <!-- <mail-metadata :message="message"></mail-metadata> -->
            <iframe class="mail-summary-content mail-summary-content-html" :srcdoc="messageHTML"></iframe>
        </b-tab>
        <b-tab id="html-text" title="Text Body" :disabled="!message.messageHasBodyText">
            <!-- <mail-metadata :message="message"></mail-metadata> -->
            <div class="mail-summary-content mail-summary-content-pre mail-summary-content-raw">{{message.messageBodyText}}</div>
        </b-tab>
      </b-tabs>
    </b-card>
  </div>
</template>

<script>
import Vue from 'vue'
import VeeValidate from 'vee-validate'
import BootstrapVue from 'bootstrap-vue'
import messagesApi from '@/api/messages'

Vue.use(BootstrapVue)
Vue.use(VeeValidate)

export default {
  name: 'message-detail',
  data () {
    return {
      message: {},
      dismissCountDown: null,
      busyForwarding: false,
      forwardRecipient: '',
      errorContent: null
    }
  },
  mounted () {
    const messageId = this.$route.params.messageId

    messagesApi.getMessageDetail(messageId)
      .then((response) => {
        this.message = response.data
      })
      .catch(() => {
        console.log('Service failed to query message detail')
      })
  },
  watch: {
    message () {
      if (this.message.messageHasBodyHTML) {
        this.setTab(1)
      } else {
        this.setTab(0)
      }
    }
  },
  computed: {
    messageHTML () {
      if (this.message && this.message.messageHasBodyHTML) {
        var html = this.message.messageBodyHTML
        var el = document.createElement('html')

        el.innerHTML = html

        var links = el.getElementsByTagName('a')

        for (var i = 0; i < links.length; i++) {
          links[i].setAttribute('target', '_new')
        }

        return el.innerHTML
      } else {
        return ''
      }
    },
    receivedDate () {
      const receivedDate = this.message && this.message.receivedDate
      return receivedDate || 0
    }
  },
  methods: {

    forwardMail () {
      this.$validator.validateAll()

      if (this.errors.has('forwardEmail')) {
        this.errorContent = this.errors && this.errors.first('forwardEmail')
        this.$refs.valPopover._toolpop.show()
      } else {
        this.$refs.valPopover._toolpop.hide()

        this.busyForwarding = true

        messagesApi.forwardMessage(this.message.messageId, this.forwardRecipient)
          .then((response) => {
            this.busyForwarding = false
            this.dismissCountDown = 5
          })
          .catch(() => {
            console.log('Service failed to forward message to ' + this.forwardRecipient)
          })
      }
    },
    countDownChanged (dismissCountDown) {
      this.dismissCountDown = dismissCountDown
    },
    setTab (index) {
      this.$nextTick(() => {
        this.$refs.tabs.setTab(index, true)
      })
    }
  }
}
</script>

<style scoped lang="less">

.details-container {
  .card-header {
    padding: 0;
    color: #31708f;
    background-color: #d9edf7;
    border-color: #bce8f1;
  }
  .card-body {
    padding: 0;
  }
}

table.addresses {
  margin-bottom: 0;
}

.mail-summary {
    height: 500px;
    flex-direction: column;
}

.mail-summary-content {
    width: 100%;
    height: 330px;
    border: 0;
    padding: 5px;
}

.mail-summary-content-pre {
    background-color: #eeeeee;
    white-space: pre;
    font-family: monospace;
    overflow: auto;
}
</style>
