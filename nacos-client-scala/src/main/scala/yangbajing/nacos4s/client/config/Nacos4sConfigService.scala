/*
 * Copyright 2020 me.yangbajing
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package yangbajing.nacos4s.client.config

import com.alibaba.nacos.api.config.ConfigService
import com.alibaba.nacos.api.config.listener.Listener

class Nacos4sConfigService(underlying: ConfigService) {
  def getConfig(dataId: String, group: String, timeoutMs: Long): String = underlying.getConfig(dataId, group, timeoutMs)

  def getConfigAndSignListener(dataId: String, group: String, timeoutMs: Long, listener: Listener): String =
    underlying.getConfigAndSignListener(dataId, group, timeoutMs, listener)

  def addListener(dataId: String, group: String, listener: Listener): Unit =
    underlying.addListener(dataId, group, listener)

  def publishConfig(dataId: String, group: String, content: String): Boolean =
    underlying.publishConfig(dataId, group, content)

  def removeConfig(dataId: String, group: String): Boolean = underlying.removeConfig(dataId, group)

  def removeListener(dataId: String, group: String, listener: Listener): Unit =
    underlying.removeListener(dataId, group, listener)

  def getServerStatus: String = underlying.getServerStatus
}
