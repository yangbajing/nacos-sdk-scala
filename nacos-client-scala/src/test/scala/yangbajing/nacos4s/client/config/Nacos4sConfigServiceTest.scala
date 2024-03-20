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

import com.alibaba.nacos.api.config.listener.AbstractListener
import com.typesafe.config.ConfigFactory
import yangbajing.nacos4s.FusionWordSpecLike
import yangbajing.nacos4s.client.util.Nacos4s

import java.time.OffsetDateTime
import java.util.concurrent.TimeUnit

class Nacos4sConfigServiceTest extends FusionWordSpecLike {
  // #ConfigService
  private val configService = Nacos4s.configService(ConfigFactory.parseString(
    """{
      |  serverAddr = "127.0.0.1:8848"
      |  namespace = ""
      |}""".stripMargin))
  private val dataId = "me.yangbajing.nacos4s"
  private val group = "DEFAULT_GROUP"
  private val timeoutMs = 30000

  "ConfigService" should {
    val listener = new AbstractListener {
      override def receiveConfigInfo(configInfo: String): Unit = {
        println(s"[${OffsetDateTime.now()}] Received new config is:\n$configInfo")
      }
    }

    "getServerStatus" in {
      configService.getServerStatus shouldBe "UP"
    }

    "publishConfig" in {
      val content = """nacos4s.client {
                      |  serverAddr = "127.0.0.1:8848"
                      |  namespace = ""
                      |  serviceName = "me.yangbajing.nacos4s"
                      |}""".stripMargin
      configService.publishConfig(dataId, group, content) shouldBe true
      TimeUnit.SECONDS.sleep(1)
    }

    "addListener" in {
      configService.addListener(dataId, group, listener)
    }

    "getConfig" in {
      val content = configService.getConfig(dataId, group, timeoutMs)
      content should include("""serverAddr = "127.0.0.1:8848"""")
    }

    "removeConfig" in {
      TimeUnit.SECONDS.sleep(1)
      configService.removeConfig(dataId, group) shouldBe true
    }

    "removeListener" in {
      TimeUnit.SECONDS.sleep(1)
      configService.removeListener(dataId, group, listener)
    }
  }
  // #ConfigService
}
