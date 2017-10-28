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

/**
 * Triggers event on a selector within a Vue component
 *
 * @param  {Vue}    vm       The Vue component instance
 * @param  {String} selector Selector to trigger the event on
 * @param  {String} event    Name of the event
 * @param  {String} keyCode  Optional. Key code to trigger for keyboard event
 */
window.triggerEvent = function triggerEvent (vm, selector, event, keyCode) {
  var e = document.createEvent('HTMLEvents')
  e.initEvent(event, true, true)
  if (keyCode) { e.keyCode = keyCode }
  vm.$el.querySelector(selector).dispatchEvent(e)
}
