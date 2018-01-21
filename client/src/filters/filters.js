/*******************************************************************************
 * Copyright 2018 Sparta Systems, Inc
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

export default {
  /**
   * Scale and format the given number of bytes into the appropriate unit
   * @param bytes The number of bytes (>= 0)
   * @returns {string} A prettified description of the number of bytes,  e.g. 2048 becomes '2KB')
   */
  prettyBytes (bytes) {
    let asNum = Number(bytes)
    if (isNaN(asNum) || asNum < 0) {
      return 'n/a'
    }
    if (asNum === 0) {
      return '0B'
    }

    let base = 1024
    let e = Math.floor(Math.log(bytes) / Math.log(base))
    let scaled = parseFloat((bytes / Math.pow(base, e)).toFixed(2))
    return scaled + ['B', 'KB', 'MB', 'GB'][e]
  }
}
