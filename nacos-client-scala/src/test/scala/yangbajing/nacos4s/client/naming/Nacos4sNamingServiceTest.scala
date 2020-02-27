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

package yangbajing.nacos4s.client.naming

import java.util.concurrent.TimeUnit

import com.typesafe.config.ConfigFactory
import yangbajing.nacos4s.FusionWordSpecLike
import yangbajing.nacos4s.client.util.Nacos4s

class Nacos4sNamingServiceTest extends FusionWordSpecLike {
  private val namingService = Nacos4s.namingService(ConfigFactory.parseString(s"""{
    |  serverAddr = "127.0.0.1:8848"
    |  namespace = ""
    |  autoRegisterInstance = true
    |  serviceName = "me-auto-register"
    |  port = 9999
    |}""".stripMargin))

  private val serviceName = "me.yangbajing.nacos4s"
  private val group = "DEFAULT_GROUP"
  private val ip = "127.0.0.1"
  private val port = 8888

  "Nacos4sNamingService" should {
    "getServerStatus" in {
      namingService.getServerStatus shouldBe "UP"
      TimeUnit.SECONDS.sleep(1)
    }

    "getAllInstances" in {
      val insts = namingService.getAllInstances("me-auto-register")
      insts.foreach(println)
      insts should have length 1
    }

    "registerInstance" in {
      namingService.registerInstance(serviceName, group, ip, port)
      TimeUnit.SECONDS.sleep(1)
      val inst = namingService.selectOneHealthyInstance(serviceName)
      inst should not be null
      inst.isHealthy shouldBe true
      inst.isEnabled shouldBe true
      inst.isEphemeral shouldBe true
      inst.getServiceName shouldBe s"$group@@$serviceName"
      println(inst)
    }
  }
}
