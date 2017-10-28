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
  <div id="mailCriteriaAndResults" class="col-lg-12">
    <div id="mailCriteria" class="mt-4">
        <form>
            <div class="input-group">
                <span class="input-group-addon text-white bg-primary">Recipient Email</span>

                <input id="mainSearchTxt" type="text" class="form-control"
                       placeholder="Enter full email address: e.g. homer@simpson.com"
                       v-model="recipientEmail"
                       @keyup.enter="clearAndFetchMessages" />

                <span class="input-group-btn">
                    <button id="mainSearchBut" class="btn btn-success" type="button"
                            @click="clearAndFetchMessages">
                        <span class="glyphicon glyphicon-search" aria-hidden="true"></span> Search!
                    </button>
                </span>
            </div>
        </form>
    </div>

    <div id="mailResults">
      <table class="table table-sm table-striped table-bordered table-hover mt-2"
             v-infinite-scroll="fetchMessages"
             infinite-scroll-disabled="busy"
             infinite-scroll-distance="10">
        <thead>
        <tr>
            <th width="2%">ID</th>
            <th width="8%">Received</th>
            <th width="15%">From</th>
            <th width="25%">Recipients</th>
            <th width="50%">Subject</th>
        </tr>
        </thead>
        <tbody>
          <tr v-for="item in items" @click="rowClick(item)">
            <td class="item-id" nowrap>{{ item.messageId }}</td>
            <td class="item-received-date" nowrap>{{ item.receivedDate | date('%b %-d, %Y %r') }}</td>
            <td class="item-sender-mail" nowrap>{{ item.senderEmail }}</td>
            <td class="item-recipients">{{ item.recipients }}</td>
            <td class="item-subject">{{ item.subject }}</td>
          </tr>
        </tbody>
      </table>

      <div class="progress" v-show="busy">
        <div class="progress-bar" role="progressbar" aria-valuenow="70"
             aria-valuemin="0" aria-valuemax="100" style="width:100%">
            Fetching...
        </div>
      </div>

      <div class="card bg-faded text-center" v-show="showEmptyMessagesPane">
        <div class="card-block">
          <h3 class="card-title">No Messages</h3>
          <p class="card-text">No messages found for the current criteria - Try sending some to the mail server!</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Vue from 'vue'
import infiniteScroll from 'vue-infinite-scroll'
import filters from 'vue-filter'
import messagesApi from '@/api/messages'

Vue.use(infiniteScroll)
Vue.use(filters)

export default {
  name: 'message-table',
  data () {
    return {
      items: [],
      busy: false,
      noMorePages: false,
      page: 0,
      size: 40,
      recipientEmail: '',
      selectedMail: {}
    }
  },
  mounted () {
    this.clearAndFetchMessages()
  },
  computed: {
    showEmptyMessagesPane () {
      return !this.busy && this.items.length < 1
    }
  },
  methods: {
    clearAndFetchMessages () {
      this.items = []
      this.page = 0
      this.noMorePages = false

      this.fetchMessages()
    },
    fetchMessages () {
      if (this.busy || this.noMorePages) {
        return
      }

      this.busy = true

      messagesApi.getMessageList(this.size, this.page, this.recipientEmail)
        .then((response) => {
          const messages = response.data.messages

          this.items = this.items.concat(messages)
          this.noMorePages = messages.length < 1
          this.busy = false
          this.page++
        })
        .catch(() => {
          this.busy = false
          console.log('Service failed to query message list')
        })
    },
    rowClick (selectedMail) {
      this.$router.push({ name: 'MessageDetail', params: { messageId: selectedMail.messageId } })
    }
  }
}
</script>

<style>
#mailResults {
  font-size: 14px;
}

#mailResults .table-hover tbody tr:hover td, .table-hover tbody tr:hover th {
    background-color: #efefff;
    cursor: pointer;
}
</style>

