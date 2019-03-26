package nacos.client.util

import com.typesafe.config.Config
import nacos.client.constant.{ Constants, Params }

case class NacosSetting(conf: Config) {
  def namespace: Option[String] = Utils.configString(conf, Params.NAMESPACE, Params.TENANT)
  def secure: Boolean           = Utils.configBoolean(conf, Params.SECURE).getOrElse(false)
  def dataId: Option[String]    = Utils.configString(conf, Params.DATA_ID, "data-id")
  def group: Option[String]     = Utils.configString(conf, Params.GROUP)
  def timeoutMs: Int            = Utils.configInt(conf, Params.TIMEOUT_TS, "timeout-ms").getOrElse(Constants.TIMEOUT_MS)
  def serverAddr: String =
    Utils
      .configString(conf, Params.SERVER_ADDR, "server-addr")
      .getOrElse(throw NacosException(NacosException.INVALID_PARAM, "need parameter serverAddr or server-addr"))
}

object NacosSetting {
  def apply(conf: Config, path: String): NacosSetting =
    NacosSetting(if (StringUtils.isNoneBlank(path)) conf.getConfig(path) else conf)
}
