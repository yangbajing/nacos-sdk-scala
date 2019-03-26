package nacos.client.model

case class ServiceInfo(
    name: String,
    clusters: String,
    cacheMillis: Long = 1000L,
    hosts: Seq[Instance] = Nil,
    lastRefTime: Long = 0L,
    checksum: String = "",
    allIPs: Boolean = false) {
  def ipCount: Int     = hosts.size
  def expired: Boolean = (System.currentTimeMillis - lastRefTime) > cacheMillis
  def isValid: Boolean = hosts != null
}
