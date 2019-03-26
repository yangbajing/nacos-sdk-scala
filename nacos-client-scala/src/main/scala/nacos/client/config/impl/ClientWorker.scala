package nacos.client.config.impl

import nacos.client.config.Listener
import nacos.client.constant.{ ApiConstants, Constants, Headers, Params }
import nacos.client.util.{ Http, NacosException, NacosSetting, StringUtils }

final class ClientWorker(setting: NacosSetting, agent: Http) {
  @volatile private var cache     = Map.empty[ConfigId, ConfigContent]
  @volatile private var listeners = Map.empty[ConfigId, Set[Listener]]

  def updateCache(configId: ConfigId, configContent: ConfigContent): Unit =
    cache = cache.updated(configId, configContent)

  def removeCache(configId: ConfigId): Unit = cache = cache - configId

  def getCache(configId: ConfigId): Option[ConfigContent] = cache.get(configId)

  def addListener(dataId: String, group: String, listener: Listener): Listener = {
    // TODO 多个listener的控制
    val request = agent
      .request(ApiConstants.CONFIG_CONTROLLER_LISTENER_PATH)
      .copy(headers = List(Headers.LONG_PULLING_TIMEOUT -> Constants.TIMEOUT_MS.toString))
    val configId = ConfigId(dataId, group, setting.namespace)
    val payload  = generateListeningConfigs(configId)

    // 线程调度
    // TODO 从cache中获取所有ConfigId和配置，一次批量监控所有配置的变化状态
    val response = agent.post(request, List(Params.LISTENING_CONFIGS -> payload))
    if (response.is2xx) {
      val confStr = response.text
      if (StringUtils.isNoneBlank(confStr)) {
        updateCache(configId, ConfigContent(confStr))
        for {
          list <- listeners.get(configId)
          func <- list
        } func(confStr)
      }
    }

    listener
  }

  def removeListener(dataId: String, group: String, listener: Listener): Unit = {
    val configId = ConfigId(dataId, group, setting.namespace)
    listeners.get(configId) match {
      case Some(list) =>
        listeners = listeners.updated(configId, list.filterNot(_ == listener))
      case _ => // do nothing
    }
  }

  private def generateListeningConfigs(configId: ConfigId): String = {
    val contentMD5 = cache.get(configId).map(_.md5).getOrElse(ConfigContent.EMPTY_MD5)
    val init       = s"${configId.dataId}${Constants.WORD_SEPARATOR}${configId.group}${Constants.WORD_SEPARATOR}$contentMD5"
    configId.namespace.foldLeft(init)((str, ns) => s"$str${Constants.WORD_SEPARATOR}$ns")
  }

  def getConfig(dataId: String, group: String, timeoutMs: Int = Constants.TIMEOUT_MS): String = {
    val params   = wrapParams(dataId, group)
    val request  = agent.request(ApiConstants.CONFIG_CONTROLLER_PATH, params).copy(readTimeout = timeoutMs)
    val response = agent.get(request)
    if (response.is2xx) {
      val content = response.text
      updateCache(ConfigId(dataId, group, setting.namespace), ConfigContent(content))
      content
    } else {
      throw NacosException(response.statusCode, response.statusMessage)
    }
  }

  def wrapParams(dataId: String, group: String, params: (String, String)*): Seq[(String, String)] =
    wrapParams(List(Params.DATA_ID -> dataId, Params.GROUP -> group) ::: params.toList)

  def wrapParams(params: Seq[(String, String)]): Seq[(String, String)] =
    setting.namespace.foldLeft(params)((vec, ns) => vec :+ Params.TENANT -> ns)

}
