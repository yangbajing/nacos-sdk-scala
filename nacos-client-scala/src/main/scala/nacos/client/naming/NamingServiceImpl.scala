package nacos.client.naming

import com.typesafe.config.Config
import nacos.client.model.{ Event, Instance, ServiceInfo }

import scala.collection.immutable

final class NamingServiceImpl(val config: Config) extends NamingService {

  /**
   * register a instance to service
   *
   * @param serviceName name of service
   * @param ip          instance ip
   * @param port        instance port
   */
  override def registerInstance(serviceName: String, ip: String, port: Int): Unit = ???

  /**
   * register a instance to service with specified cluster name
   *
   * @param serviceName name of service
   * @param ip          instance ip
   * @param port        instance port
   * @param clusterName instance cluster name
   *
   */
  override def registerInstance(serviceName: String, ip: String, port: Int, clusterName: String): Unit = ???

  /**
   * register a instance to service with specified instance properties
   *
   * @param serviceName name of service
   * @param instance    instance to register
   *
   */
  override def registerInstance(serviceName: String, instance: Instance): Unit = ???
  override def registerInstanceCurrent(config: Config): Unit                   = ???

  /**
   * deregister instance from a service
   *
   * @param serviceName name of service
   * @param ip          instance ip
   * @param port        instance port
   *
   */
  override def deregisterInstance(serviceName: String, ip: String, port: Int): Unit = ???

  /**
   * deregister instance with specified cluster name from a service
   *
   * @param serviceName name of service
   * @param ip          instance ip
   * @param port        instance port
   * @param clusterName instance cluster name
   *
   */
  override def deregisterInstance(serviceName: String, ip: String, port: Int, clusterName: String): Unit = ???
  override def deregisterInstanceCurrent(config: Config): Unit                                           = ???

  /**
   * get all instances of a service
   *
   * @param serviceName name of service
   * @return A list of instance
   *
   */
  override def getAllInstances(serviceName: String): Seq[Instance] =
    ???

  /**
   * Get all instances of a service
   *
   * @param serviceName name of service
   * @param subscribe   if subscribe the service
   * @return A list of instance
   *
   */
  override def getAllInstances(serviceName: String, subscribe: Boolean): Seq[Instance] = ???

  /**
   * Get all instances within specified clusters of a service
   *
   * @param serviceName name of service
   * @param clusters    list of cluster
   * @return A list of qualified instance
   *
   */
  override def getAllInstances(serviceName: String, clusters: Seq[String]): Seq[Instance] = ???

  /**
   * Get all instances within specified clusters of a service
   *
   * @param serviceName name of service
   * @param clusters    list of cluster
   * @param subscribe   if subscribe the service
   * @return A list of qualified instance
   *
   */
  override def getAllInstances(serviceName: String, clusters: Seq[String], subscribe: Boolean): Seq[Instance] = ???

  /**
   * Get qualified instances of service
   *
   * @param serviceName name of service
   * @param healthy     a flag to indicate returning healthy or unhealthy instances
   * @return A qualified list of instance
   *
   */
  override def selectInstances(serviceName: String, healthy: Boolean): Seq[Instance] = ???

  /**
   * Get qualified instances of service
   *
   * @param serviceName name of service
   * @param healthy     a flag to indicate returning healthy or unhealthy instances
   * @param subscribe   if subscribe the service
   * @return A qualified list of instance
   *
   */
  override def selectInstances(serviceName: String, healthy: Boolean, subscribe: Boolean): Seq[Instance] = ???

  /**
   * Get qualified instances within specified clusters of service
   *
   * @param serviceName name of service
   * @param clusters    list of cluster
   * @param healthy     a flag to indicate returning healthy or unhealthy instances
   * @return A qualified list of instance
   *
   */
  override def selectInstances(serviceName: String, clusters: Seq[String], healthy: Boolean): Seq[Instance] = ???

  /**
   * Get qualified instances within specified clusters of service
   *
   * @param serviceName name of service
   * @param clusters    list of cluster
   * @param healthy     a flag to indicate returning healthy or unhealthy instances
   * @param subscribe   if subscribe the service
   * @return A qualified list of instance
   *
   */
  override def selectInstances(
      serviceName: String,
      clusters: Seq[String],
      healthy: Boolean,
      subscribe: Boolean): Seq[Instance] = ???

  /**
   * Select one healthy instance of service using predefined load balance strategy
   *
   * @param serviceName name of service
   * @return qualified instance
   *
   */
  override def selectOneHealthyInstance(serviceName: String): Instance =
    ???

  /**
   * select one healthy instance of service using predefined load balance strategy
   *
   * @param serviceName name of service
   * @param subscribe   if subscribe the service
   * @return qualified instance
   *
   */
  override def selectOneHealthyInstance(serviceName: String, subscribe: Boolean): Instance = ???

  /**
   * Select one healthy instance of service using predefined load balance strategy
   *
   * @param serviceName name of service
   * @param clusters    a list of clusters should the instance belongs to
   * @return qualified instance
   *
   */
  override def selectOneHealthyInstance(serviceName: String, clusters: Seq[String]): Instance = ???

  /**
   * Select one healthy instance of service using predefined load balance strategy
   *
   * @param serviceName name of service
   * @param clusters    a list of clusters should the instance belongs to
   * @param subscribe   if subscribe the service
   * @return qualified instance
   *
   */
  override def selectOneHealthyInstance(serviceName: String, clusters: Seq[String], subscribe: Boolean): Instance = ???

  /**
   * Subscribe service to receive events of instances alteration
   *
   * @param serviceName name of service
   * @param listener    event listener
   *
   */
  override def subscribe(serviceName: String, listener: Event => Unit): Unit = ???

  /**
   * subscribe service to receive events of instances alteration
   *
   * @param serviceName name of service
   * @param clusters    list of cluster
   * @param listener    event listener
   *
   */
  override def subscribe(serviceName: String, clusters: Seq[String], listener: Event => Unit): Unit = ???

  /**
   * unsubscribe event listener of service
   *
   * @param serviceName name of service
   * @param listener    event listener
   *
   */
  override def unsubscribe(serviceName: String, listener: Event => Unit): Unit = ???

  /**
   * unsubscribe event listener of service
   *
   * @param serviceName name of service
   * @param clusters    list of cluster
   * @param listener    event listener
   *
   */
  override def unsubscribe(serviceName: String, clusters: Seq[String], listener: Event => Unit): Unit = ???

  /**
   * get all service names from server
   *
   * @param pageNo   page index
   * @param pageSize page size
   * @return list of service names (Seq[Service Name], count)
   */
  override def getServicesOfServer(pageNo: Int, pageSize: Int): (immutable.Seq[String], Int) = ???

  /**
   * Get all subscribed services of current client
   *
   * @return subscribed services
   */
  override def getSubscribeServices: Seq[ServiceInfo] = ???

  /**
   * get server health status
   *
   * @return is server healthy
   */
  override def getServerStatus: String = ???
}
