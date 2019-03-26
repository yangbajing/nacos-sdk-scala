package nacos.client.model

case class Instance(
    instanceId: String,
    ip: String,
    port: Int,
    serviceName: String,
    clusterName: String = "",
    weight: Double = 1.0D,
    healthy: Boolean = true,
    enabled: Boolean = true,
    metadata: Map[String, String] = Map()) {
  def toInetAddr: String = ip + ":" + port
}
