package nacos.client.config.impl

import com.typesafe.config.Config
import nacos.client.config.{ ConfigService, Listener }
import nacos.client.constant.{ ApiConstants, Constants, Params }
import nacos.client.util._

import scala.util.Try

case class ConfigId(dataId: String, group: String, namespace: Option[String]) {
  def toParams: List[(String, String)] =
    namespace.foldLeft(List(Params.DATA_ID -> dataId, Params.GROUP -> group))((vec, ns) => vec :+ Params.TENANT -> ns)
}

case class ConfigContent(content: String, md5: String)
object ConfigContent {
  val EMPTY_MD5: String = Utils.md5Hex("")
  def apply(content: String): ConfigContent = {
    require(StringUtils.isNoneBlank(content), "content must not be empty")
    ConfigContent(content, Utils.md5Hex(content))
  }
}

final class ConfigServiceImpl(val config: Config, val agent: HttpAgent) extends ConfigService {
  private val worker = new ClientWorker(agent.setting, agent)

  /**
   * Get config
   *
   * @param dataId    dataId
   * @param group     group
   * @param timeoutMs read timeout (miiliseconds)
   * @return config value
   */
  override def getConfig(dataId: String, group: String, timeoutMs: Int): Try[String] =
    worker.getConfig(ConfigId(dataId, group, agent.setting.namespace))

  /**
   * Add a listener to the configuration, after the server modified the
   * configuration, the client will use the incoming listener callback.
   * Recommended asynchronous processing, the application can implement the
   * getExecutor method in the ManagerListener, provide a thread pool of
   * execution. If provided, use the main thread callback, May block other
   * configurations or be blocked by other configurations.
   *
   * @param dataId   dataId
   * @param group    group
   * @param listener listener
   */
  override def addListener(dataId: String, group: String, listener: Listener): Listener =
    worker.addListener(dataId, group, listener)

  /**
   * Publish config.
   *
   * @param dataId  dataId
   * @param group   group
   * @param content content
   * @return Whether publish
   */
  override def publishConfig(dataId: String, group: String, content: String): Boolean = {
    val params   = worker.wrapParams(dataId, group, Params.CONTENT -> content)
    val request  = agent.request(ApiConstants.CONFIG_CONTROLLER_PATH)
    val response = agent.post(request, params)
    if (response.is2xx) {
      worker.updateCache(ConfigId(dataId, group, cc.namespace), ConfigContent(content))
    }
    response.is2xx
  }

  /**
   * Remove config
   *
   * @param dataId dataId
   * @param group  group
   * @return whether remove
   */
  override def removeConfig(dataId: String, group: String): Boolean = {
    val params   = worker.wrapParams(dataId, group)
    val request  = agent.request(ApiConstants.CONFIG_CONTROLLER_PATH, params)
    val response = agent.delete(request)
    if (response.is2xx) {
      worker.removeCache(ConfigId(dataId, group, cc.namespace))
    }
    response.is2xx
  }

  /**
   * Remove listener
   *
   * @param dataId   dataId
   * @param group    group
   * @param listener listener
   */
  override def removeListener(dataId: String, group: String, listener: Listener): Unit = {
    worker.removeListener(dataId, group, listener)
  }

  /**
   * Get server status
   *
   * @return whether health
   */
  override def getServerStatus: String = if (agent.isHealth) Constants.UP else Constants.DOWN

  private def cc: NacosSetting = agent.setting

}
