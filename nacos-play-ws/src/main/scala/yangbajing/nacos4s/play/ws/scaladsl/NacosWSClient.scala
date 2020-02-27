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

import java.net.URI

import akka.actor.ActorSystem
import javax.inject.{ Inject, Named, Singleton }
import play.api.libs.ws.ahc.{ AhcWSRequest, StandaloneAhcWSClient, StandaloneAhcWSRequest }
import play.api.libs.ws.{ WSClient, WSRequest }
import yangbajing.nacos4s.client.util.{ Constants, Nacos4s }

@Named("nacos")
@Singleton
class NacosWSClient @Inject() (underlyingClient: StandaloneAhcWSClient)(implicit system: ActorSystem) extends WSClient {
  private val namingService = Nacos4s.namingService(system.settings.config.getConfig(Constants.NACOS4S_CLIENT_NAMING))

  override def underlying[T]: T = underlyingClient.underlying[T]

  override def url(url: String): WSRequest = {
    val realUrl = try {
      val uri = new URI(url)
      if (uri.getPort == -1) {
        val inst = namingService.selectOneHealthyInstance(uri.getHost)
        new URI(uri.getScheme, uri.getUserInfo, inst.getIp, inst.getPort, uri.getPath, uri.getQuery, uri.getFragment).toString
      } else url
    } catch {
      case e: Exception =>
        system.log.warning("Service discovery failed, direct access. url is {}.", url, e)
        url
    }
    AhcWSRequest(underlyingClient.url(realUrl).asInstanceOf[StandaloneAhcWSRequest])
  }

  override def close(): Unit = underlyingClient.close()
}
