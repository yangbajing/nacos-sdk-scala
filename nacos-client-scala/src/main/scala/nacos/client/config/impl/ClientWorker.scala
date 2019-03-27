package nacos.client.config.impl

import java.util.concurrent.{ Executors, TimeUnit }

import nacos.client.config.Listener
import nacos.client.constant.{ ApiConstants, Constants, Headers, Params }
import nacos.client.util._

import scala.util.{ Failure, Success, Try }

final class ClientWorker(setting: NacosSetting, agent: HttpAgent) {
  @volatile private var cache     = Map.empty[ConfigId, ConfigContent]
  @volatile private var listeners = Map.empty[ConfigId, Set[Listener]]
  private val executor            = Executors.newFixedThreadPool(2)
  private val scheduledExecutor = Executors.newScheduledThreadPool(1, (r: Runnable) => {
    val t = new Thread(r)
    t.setName(s"nacos.client.worker.${agent.name}")
    t.setDaemon(true)
    t
  })
  init()

  private def init(): Unit = {
    scheduledExecutor.scheduleWithFixedDelay(() => {
      try {
        checkConfigs()
      } catch {
        case e: Throwable =>
          e.printStackTrace()
      }
    }, 1L, 10L, TimeUnit.MILLISECONDS)
  }

  def updateCache(configId: ConfigId, configContent: ConfigContent): Unit =
    cache = cache.updated(configId, configContent)

  def removeCache(configId: ConfigId): Unit = cache = cache - configId

  def getCache(configId: ConfigId): Option[ConfigContent] = cache.get(configId)

  def addListener(dataId: String, group: String, listener: Listener): Listener = {
    val configId = ConfigId(dataId, group, setting.namespace)
    listeners = listeners.updated(configId, listeners.getOrElse(configId, Set()) + listener)
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

  private def checkConfigs(): Unit = {
    val request = agent.request(ApiConstants.CONFIG_CONTROLLER_LISTENER_PATH, headers = List(Headers.LONG_PULLING_TIMEOUT -> Constants.TIMEOUT_MS.toString))
    val payload = listeners.keysIterator.map(configId => idToString(configId)).mkString(Constants.LINE_SEPARATOR)

    val response = agent.post(request, List(Params.LISTENING_CONFIGS -> payload))
    if (response.is2xx) {
      for {
        text     <- StringUtils.option(response.text)
        line     <- text.split(Constants.LINE_SEPARATOR)
        configId <- parseConfigId(line)
      } dispatch(configId)
    }
  }

  private def dispatch(configId: ConfigId): Unit = getConfig(configId) match {
    case Success(confStr) =>
      for {
        items    <- listeners.get(configId)
        listener <- items
      } listener(confStr)
    case Failure(e) => e.printStackTrace()
  }

  private def parseConfigId(line: String): Option[ConfigId] = line.split(Constants.WORD_SEPARATOR) match {
    case Array(dataId, group, tenant) => Some(ConfigId(dataId, group, Some(tenant)))
    case Array(dataId, group)         => Some(ConfigId(dataId, group, None))
    case _                            => None
  }

  private def idToString(configId: ConfigId): String = {
    val contentMD5 = cache.get(configId).map(_.md5).getOrElse(ConfigContent.EMPTY_MD5)
    val init       = s"${configId.dataId}${Constants.WORD_SEPARATOR}${configId.group}${Constants.WORD_SEPARATOR}$contentMD5"
    configId.namespace.foldLeft(init)((str, ns) => s"$str${Constants.WORD_SEPARATOR}$ns")
  }

  def getConfig(configId: ConfigId, timeoutMs: Int = Constants.TIMEOUT_MS): Try[String] = Try {
    val params   = configId.toParams
    val request  = agent.request(ApiConstants.CONFIG_CONTROLLER_PATH, params).copy(readTimeout = timeoutMs)
    val response = agent.get(request)
    if (response.is2xx) {
      val content = response.text
      updateCache(configId, ConfigContent(content))
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
