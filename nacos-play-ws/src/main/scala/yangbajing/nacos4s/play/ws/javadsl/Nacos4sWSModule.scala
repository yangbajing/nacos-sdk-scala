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

package yangbajing.nacos4s.play.ws.javadsl

import akka.stream.Materializer
import javax.inject.{ Inject, Provider, Singleton }
import play.api.inject.{ SimpleModule, bind }
import play.libs.ws.WSClient
import play.libs.ws.ahc.StandaloneAhcWSClient

class Nacos4sWSModule
    extends SimpleModule(
      bind[NacosWSClient].toProvider[NacosWSClientProvider],
      bind[WSClient].qualifiedWith("nacos").to[NacosWSClient])

@Singleton
class NacosWSClientProvider @Inject() (underlyingClient: StandaloneAhcWSClient, mat: Materializer)
    extends Provider[NacosWSClient] {
  override lazy val get: NacosWSClient = new NacosWSClient(underlyingClient, mat)
}
