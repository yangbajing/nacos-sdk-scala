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

package yangbajing.nacos4s.play.ws.scaladsl

import org.apache.pekko.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import org.scalatest.time.Span
import org.scalatest.wordspec.AnyWordSpecLike
import play.api.libs.ws.ahc.StandaloneAhcWSClient

class NacosWSClientTest
    extends ScalaTestWithActorTestKit("pekko.actor.testkit.typed.default-timeout = 60.seconds")
    with AnyWordSpecLike {
  implicit override val patience: PatienceConfig =
    PatienceConfig(testKit.testKitSettings.DefaultTimeout.duration, Span(100, org.scalatest.time.Millis))

  "NacosWSClient" should {
    "WSClient" in {
      // #NacosWSClientTest
      val wsClient = NacosWSClient(StandaloneAhcWSClient())
      val response = wsClient.url("https://github.com/yangbajing/nacos-sdk-scala").get().futureValue
      response.status shouldBe 200
      wsClient.close()
      // #NacosWSClientTest
    }
  }
}
