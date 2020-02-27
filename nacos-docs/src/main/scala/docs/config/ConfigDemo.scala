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

package docs.config

// #ConfigDemo
import yangbajing.nacos4s.client.config.Nacos4sConfigService
import yangbajing.nacos4s.client.util.Nacos4s

object ConfigDemo extends App {
  val configService: Nacos4sConfigService = Nacos4s.configService("127.0.0.0:8848", "")
  val status = configService.getServerStatus
  assert(status == "UP")
}
// #ConfigDemo
