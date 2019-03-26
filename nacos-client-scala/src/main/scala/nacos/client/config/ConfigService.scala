package nacos.client.config

import com.typesafe.config.Config
import nacos.client.config.impl.ConfigServiceImpl
import nacos.client.constant.Constants
import nacos.client.util.Http

trait ConfigService {
  def agent: Http

  /**
   * Get config
   *
   * @param dataId    dataId
   * @param group     group
   * @param timeoutMs read timeout
   * @return config value
   */
  def getConfig(dataId: String, group: String, timeoutMs: Int): String
  def getConfig(dataId: String, group: String): String = getConfig(dataId, group, Constants.TIMEOUT_MS)
  def getConfig: String

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
  def addListener(dataId: String, group: String, listener: Listener): Listener

  /**
   * Publish config.
   *
   * @param dataId  dataId
   * @param group   group
   * @param content content
   * @return Whether publish
   */
  def publishConfig(dataId: String, group: String, content: String): Boolean

  /**
   * Remove config
   *
   * @param dataId dataId
   * @param group  group
   * @return whether remove
   */
  def removeConfig(dataId: String, group: String): Boolean

  /**
   * Remove listener
   *
   * @param dataId   dataId
   * @param group    group
   * @param listener listener
   */
  def removeListener(dataId: String, group: String, listener: Listener): Unit

  /**
   * Get server status
   *
   * @return whether health
   */
  def getServerStatus: String

}

object ConfigService {
  def apply(config: Config): ConfigService = new ConfigServiceImpl(config, Http(config))
}
