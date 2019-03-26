package nacos.client.constant

object Params {
  val SERVER_ADDR       = "serverAddr"
  val DATA_ID           = "dataId"
  val GROUP             = "group"
  val TENANT            = "tenant"
  val NAMESPACE         = "namespace"
  val NAMESPACE_ID      = "namespaceId"
  val SERVICE_NAME      = "serviceName"
  val CLUSTER_NAME      = "clusterName"
  val CONTENT           = "content"
  val LISTENING_CONFIGS = "Listening-Configs"
  val TIMEOUT_TS        = "timeoutMs"
  val SECURE            = "secure"
}

object Constants {
  val LINE_SEPARATOR = Character.toString(1)
  val WORD_SEPARATOR = Character.toString(2)
  val DEFAULT_GROUP  = "DEFAULT_GROUP"
  val TIMEOUT_MS     = 30000
  val UP             = "UP"
  val DOWN           = "DOWN"
}
