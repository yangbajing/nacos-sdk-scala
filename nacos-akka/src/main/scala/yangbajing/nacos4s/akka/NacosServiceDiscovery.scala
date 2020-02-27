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

import akka.actor.ActorSystem
import akka.discovery.ServiceDiscovery.{ DiscoveryTimeoutException, Resolved, ResolvedTarget }
import akka.discovery.{ Lookup, ServiceDiscovery }
import yangbajing.nacos4s.client.util.{ ConfigUtils, Constants, Nacos4s }

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ Future, Promise }
import scala.util.Success

class NacosServiceDiscovery(system: ActorSystem) extends ServiceDiscovery {
  import system.dispatcher
  private val discoveryConfig = ConfigUtils.discoveryConfig(system.settings.config)
  private def namingConfig = {
    val path = discoveryConfig.getString(Constants.NAMING_CONFIG)
    if (discoveryConfig.hasPath(path)) discoveryConfig.getConfig(path) else system.settings.config.getConfig(path)
  }
  private val oneHealth = discoveryConfig.getBoolean(Constants.ONE_HEALTH)
  private val onlyHealth = discoveryConfig.getBoolean(Constants.ONLY_HEALTH)
  private val namingService = Nacos4s.namingService(namingConfig)

  override def lookup(lookup: Lookup, resolveTimeout: FiniteDuration): Future[ServiceDiscovery.Resolved] = {
    val f = Future {
      val instances = if (oneHealth) {
        val instance = namingService.selectOneHealthyInstance(lookup.serviceName)
        Vector(ResolvedTarget(instance.getIp, Some(instance.getPort), None))
      } else {
        namingService
          .selectInstances(lookup.serviceName, onlyHealth)
          .map(instance => ResolvedTarget(instance.getIp, Some(instance.getPort), None))
          .toVector
      }
      Resolved(lookup.serviceName, instances)
    }
    resolveAndTimeout(lookup, resolveTimeout, f)
  }

  @inline private def resolveAndTimeout(
      lookup: Lookup,
      resolveTimeout: FiniteDuration,
      f: Future[Resolved]): Future[Resolved] = {
    val promise = Promise[Resolved]()
    val cancellable = system.scheduler.scheduleOnce(resolveTimeout) {
      promise.failure(
        new DiscoveryTimeoutException(
          s"Service [[${lookup.serviceName}]] unreachable, resolve timeout is $resolveTimeout."))
    }
    Future.firstCompletedOf(List(f, promise.future)).andThen {
      case Success(_) if !cancellable.isCancelled => cancellable.cancel()
    }
  }
}
