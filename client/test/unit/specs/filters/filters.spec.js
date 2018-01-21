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

import filters from '@/filters/filters'

describe('filters', () => {
  describe('prettyBytes', () => {
    it('should handle invalid input', () => {
      expect(filters.prettyBytes()).to.equal('n/a')
      expect(filters.prettyBytes('derp')).to.equal('n/a')
      expect(filters.prettyBytes(-50)).to.equal('n/a')
    })

    it('should convert B', () => {
      expect(filters.prettyBytes(0)).to.equal('0B')
      expect(filters.prettyBytes(100)).to.equal('100B')
      expect(filters.prettyBytes(1000)).to.equal('1000B')
      expect(filters.prettyBytes(1023)).to.equal('1023B')
    })

    it('should convert KB', () => {
      expect(filters.prettyBytes(1024)).to.equal('1KB')
      expect(filters.prettyBytes(50000)).to.equal('48.83KB')
      expect(filters.prettyBytes(1024 * 1023)).to.equal('1023KB')
    })

    it('should convert MB', () => {
      let megabyte = 1024 * 1024
      expect(filters.prettyBytes(megabyte)).to.equal('1MB')
      expect(filters.prettyBytes(megabyte * 99)).to.equal('99MB')
      expect(filters.prettyBytes(megabyte * 1023)).to.equal('1023MB')
    })

    it('should convert >= GB', () => {
      let gigabyte = 1024 * 1024 * 1024
      expect(filters.prettyBytes(gigabyte)).to.equal('1GB')
      expect(filters.prettyBytes(gigabyte * 100)).to.equal('100GB')
    })
  })
})
