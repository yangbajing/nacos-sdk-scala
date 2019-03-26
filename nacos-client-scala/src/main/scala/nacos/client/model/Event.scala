package nacos.client.model

import scala.collection.immutable

sealed trait Event

case class NamingEvent(serviceName: String, instances: immutable.Seq[Instance]) extends Event {}
