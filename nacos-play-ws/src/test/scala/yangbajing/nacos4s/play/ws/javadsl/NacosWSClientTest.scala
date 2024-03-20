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

import org.apache.pekko.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import org.apache.pekko.stream.SystemMaterializer
import org.scalatest.wordspec.AnyWordSpecLike
import org.slf4j.LoggerFactory
import play.libs.ws.ahc.StandaloneAhcWSClient
import play.shaded.ahc.org.asynchttpclient.DefaultAsyncHttpClient

class NacosWSClientTest
    extends ScalaTestWithActorTestKit("pekko.actor.testkit.typed.default-timeout = 60.seconds")
    with AnyWordSpecLike {
  private val log = LoggerFactory.getLogger(classOf[NacosWSClientTest])
  "NacosWSClient" should {
    "WSClient" in {
      import scala.jdk.FutureConverters.given
      // #NacosWSClientTest
      val ahcWSClient = new StandaloneAhcWSClient(new DefaultAsyncHttpClient(), SystemMaterializer(system).materializer)
      val wsClient = new NacosWSClient(system, ahcWSClient)
      val begin = System.currentTimeMillis()
      val response = wsClient.url("https://github.com/yangbajing/nacos-sdk-scala").get().asScala.futureValue
      val end = System.currentTimeMillis()
      log.debug(s"Cost time ${end - begin}ms.")
      response.getStatus shouldBe 200
      wsClient.close()
      // #NacosWSClientTest
    }
  }
}
