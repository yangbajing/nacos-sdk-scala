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

package yangbajing.nacos4s.akka

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import akka.discovery.Discovery
import org.scalatest.wordspec.AnyWordSpecLike
import scala.concurrent.duration._

// #NacosServiceDiscoveryTest
class NacosServiceDiscoveryTest extends ScalaTestWithActorTestKit("""
akka.actor.testkit.typed {
  default-timeout = 10.seconds
}
akka.discovery {
  method = nacos
  nacos {
    onlyHealth = true
    oneHealth = true
  }
}
nacos4s.client.naming {
  serverAddr = "127.0.0.1:8848"
  namespace = ""
  autoRegisterInstance = true
  serviceName = "me-auto-register"
  port = 9999
}""".stripMargin) with AnyWordSpecLike {
  "NacosServiceDiscovery" should {
    "lookup" in {
      val resolved = Discovery(system).discovery.lookup("me-auto-register", 10.seconds).futureValue
      println(resolved)
      resolved.serviceName shouldBe "me-auto-register"
      resolved.addresses should not be empty
      val address = resolved.addresses.head
      address.port shouldBe Some(9999)
    }
  }
}
// #NacosServiceDiscoveryTest
