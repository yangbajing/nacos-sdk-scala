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

import java.io.IOException
import java.util.Properties

import com.alibaba.nacos.api.PropertyKeyConst
import com.alibaba.nacos.api.naming.NamingService
import com.alibaba.nacos.api.naming.listener.EventListener
import com.alibaba.nacos.api.naming.pojo.{ Instance, ListView, ServiceInfo }
import com.alibaba.nacos.api.selector.AbstractSelector
import yangbajing.nacos4s.client.util.CollectionUtils._
import yangbajing.nacos4s.client.util.NetworkUtils

import scala.collection.immutable
import scala.jdk.CollectionConverters._

class Nacos4sNamingService(underlying: NamingService, props: Properties) {
  if ("true" == props.getProperty("autoRegisterInstance")) {
    val inst = new Instance
    inst.setIp(
      Option(props.getProperty("ip"))
        .orElse(NetworkUtils.firstOnlineInet4Address().map(_.getHostAddress))
        .getOrElse(throw new IOException("The local binding ip address could not be found.")))
    inst.setPort(props.getProperty("port").toInt)
    Option(props.getProperty(PropertyKeyConst.CLUSTER_NAME)).foreach(inst.setClusterName)
    registerInstance(
      props.getProperty("serviceName"),
      Option(props.getProperty("group")).getOrElse("DEFAULT_GROUP"),
      inst)
  }

  def registerInstance(serviceName: String, ip: String, port: Int): Unit =
    underlying.registerInstance(serviceName, ip, port)

  def registerInstance(serviceName: String, groupName: String, ip: String, port: Int): Unit =
    underlying.registerInstance(serviceName, groupName, ip, port)

  def registerInstance(serviceName: String, ip: String, port: Int, clusterName: String): Unit =
    underlying.registerInstance(serviceName, ip, port, clusterName)

  def registerInstance(serviceName: String, groupName: String, ip: String, port: Int, clusterName: String): Unit =
    underlying.registerInstance(serviceName, groupName, ip, port, clusterName)

  def registerInstance(serviceName: String, instance: Instance): Unit =
    underlying.registerInstance(serviceName, instance)

  def registerInstance(serviceName: String, groupName: String, instance: Instance): Unit =
    underlying.registerInstance(serviceName, groupName, instance)

  def deregisterInstance(serviceName: String, ip: String, port: Int): Unit =
    underlying.deregisterInstance(serviceName, ip, port)

  def deregisterInstance(serviceName: String, groupName: String, ip: String, port: Int): Unit =
    underlying.deregisterInstance(serviceName, groupName, ip, port)

  def deregisterInstance(serviceName: String, ip: String, port: Int, clusterName: String): Unit =
    underlying.deregisterInstance(serviceName, ip, port, clusterName)

  def deregisterInstance(serviceName: String, groupName: String, ip: String, port: Int, clusterName: String): Unit =
    underlying.deregisterInstance(serviceName, groupName, ip, port, clusterName)

  def deregisterInstance(serviceName: String, instance: Instance): Unit =
    underlying.deregisterInstance(serviceName, instance)

  def deregisterInstance(serviceName: String, groupName: String, instance: Instance): Unit =
    underlying.deregisterInstance(serviceName, groupName, instance)

  def getAllInstances(serviceName: String): immutable.Seq[Instance] =
    underlying.getAllInstances(serviceName).asScala.toVector

  def getAllInstances(serviceName: String, groupName: String): immutable.Seq[Instance] =
    underlying.getAllInstances(serviceName, groupName).asScala.toVector

  def getAllInstances(serviceName: String, subscribe: Boolean): immutable.Seq[Instance] =
    underlying.getAllInstances(serviceName, subscribe).asScala.toVector

  def getAllInstances(serviceName: String, groupName: String, subscribe: Boolean): immutable.Seq[Instance] =
    underlying.getAllInstances(serviceName, groupName, subscribe).asScala.toVector

  def getAllInstances(serviceName: String, clusters: Iterable[String]): immutable.Seq[Instance] =
    underlying.getAllInstances(serviceName, clusters.asJavaList).asScala.toVector

  def getAllInstances(serviceName: String, groupName: String, clusters: Iterable[String]): immutable.Seq[Instance] =
    underlying.getAllInstances(serviceName, groupName, clusters.asJavaList).asScala.toVector

  def getAllInstances(serviceName: String, clusters: Iterable[String], subscribe: Boolean): immutable.Seq[Instance] =
    underlying.getAllInstances(serviceName, clusters.asJavaList, subscribe).asScala.toVector

  def getAllInstances(
      serviceName: String,
      groupName: String,
      clusters: Iterable[String],
      subscribe: Boolean): immutable.Seq[Instance] =
    underlying.getAllInstances(serviceName, groupName, clusters.asJavaList, subscribe).asScala.toVector

  def selectInstances(serviceName: String, healthy: Boolean): immutable.Seq[Instance] =
    underlying.selectInstances(serviceName, healthy).asScala.toVector

  def selectInstances(serviceName: String, groupName: String, healthy: Boolean): immutable.Seq[Instance] =
    underlying.selectInstances(serviceName, groupName, healthy).asScala.toVector

  def selectInstances(serviceName: String, healthy: Boolean, subscribe: Boolean): immutable.Seq[Instance] =
    underlying.selectInstances(serviceName, healthy, subscribe).asScala.toVector

  def selectInstances(
      serviceName: String,
      groupName: String,
      healthy: Boolean,
      subscribe: Boolean): immutable.Seq[Instance] =
    underlying.selectInstances(serviceName, groupName, healthy, subscribe).asScala.toVector

  def selectInstances(serviceName: String, clusters: Iterable[String], healthy: Boolean): immutable.Seq[Instance] =
    underlying.selectInstances(serviceName, clusters.asJavaList, healthy).asScala.toVector

  def selectInstances(
      serviceName: String,
      groupName: String,
      clusters: Iterable[String],
      healthy: Boolean): immutable.Seq[Instance] =
    underlying.selectInstances(serviceName, groupName, clusters.asJavaList, healthy).asScala.toVector

  def selectInstances(
      serviceName: String,
      clusters: Iterable[String],
      healthy: Boolean,
      subscribe: Boolean): immutable.Seq[Instance] =
    underlying.selectInstances(serviceName, clusters.asJavaList, healthy, subscribe).asScala.toVector

  def selectInstances(
      serviceName: String,
      groupName: String,
      clusters: Iterable[String],
      healthy: Boolean,
      subscribe: Boolean): immutable.Seq[Instance] =
    underlying.selectInstances(serviceName, groupName, clusters.asJavaList, healthy, subscribe).asScala.toVector

  def selectOneHealthyInstance(serviceName: String): Instance = underlying.selectOneHealthyInstance(serviceName)

  def selectOneHealthyInstance(serviceName: String, groupName: String): Instance =
    underlying.selectOneHealthyInstance(serviceName, groupName)

  def selectOneHealthyInstance(serviceName: String, subscribe: Boolean): Instance =
    underlying.selectOneHealthyInstance(serviceName, subscribe)

  def selectOneHealthyInstance(serviceName: String, groupName: String, subscribe: Boolean): Instance =
    underlying.selectOneHealthyInstance(serviceName, groupName, subscribe)

  def selectOneHealthyInstance(serviceName: String, clusters: immutable.Seq[String]): Instance =
    underlying.selectOneHealthyInstance(serviceName, clusters.asJavaList)

  def selectOneHealthyInstance(serviceName: String, groupName: String, clusters: immutable.Seq[String]): Instance =
    underlying.selectOneHealthyInstance(serviceName, groupName, clusters.asJavaList)

  def selectOneHealthyInstance(serviceName: String, clusters: Iterable[String], subscribe: Boolean): Instance =
    underlying.selectOneHealthyInstance(serviceName, clusters.asJavaList, subscribe)

  def selectOneHealthyInstance(
      serviceName: String,
      groupName: String,
      clusters: Iterable[String],
      subscribe: Boolean): Instance =
    underlying.selectOneHealthyInstance(serviceName, groupName, clusters.asJavaList, subscribe)

  def subscribe(serviceName: String, listener: EventListener): Unit = underlying.subscribe(serviceName, listener)

  def subscribe(serviceName: String, groupName: String, listener: EventListener): Unit =
    underlying.subscribe(serviceName, groupName, listener)

  def subscribe(serviceName: String, clusters: Iterable[String], listener: EventListener): Unit =
    underlying.subscribe(serviceName, clusters.asJavaList, listener)

  def subscribe(serviceName: String, groupName: String, clusters: Iterable[String], listener: EventListener): Unit =
    underlying.subscribe(serviceName, groupName, clusters.asJavaList, listener)

  def unsubscribe(serviceName: String, listener: EventListener): Unit = underlying.unsubscribe(serviceName, listener)

  def unsubscribe(serviceName: String, groupName: String, listener: EventListener): Unit =
    underlying.unsubscribe(serviceName, groupName, listener)

  def unsubscribe(serviceName: String, clusters: Iterable[String], listener: EventListener): Unit =
    underlying.unsubscribe(serviceName, clusters.asJavaList, listener)

  def unsubscribe(serviceName: String, groupName: String, clusters: Iterable[String], listener: EventListener): Unit =
    underlying.unsubscribe(serviceName, groupName, clusters.asJavaList, listener)

  def getServicesOfServer(pageNo: Int, pageSize: Int): ListView[String] =
    underlying.getServicesOfServer(pageNo, pageSize)

  def getServicesOfServer(pageNo: Int, pageSize: Int, groupName: String): ListView[String] =
    underlying.getServicesOfServer(pageNo, pageSize, groupName)

  def getServicesOfServer(pageNo: Int, pageSize: Int, selector: AbstractSelector): ListView[String] =
    underlying.getServicesOfServer(pageNo, pageSize, selector)

  def getServicesOfServer(pageNo: Int, pageSize: Int, groupName: String, selector: AbstractSelector): ListView[String] =
    underlying.getServicesOfServer(pageNo, pageSize, groupName, selector)

  def getSubscribeServices: immutable.Seq[ServiceInfo] = underlying.getSubscribeServices.asScala.toVector

  def getServerStatus: String = underlying.getServerStatus
}
