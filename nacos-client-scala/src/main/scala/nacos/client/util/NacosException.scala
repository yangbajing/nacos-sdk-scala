package nacos.client.util

case class NacosException(errCode: Int, errMsg: String, cause: Throwable) extends RuntimeException(errMsg, cause)

object NacosException {

  /**
   * server error code, use http code 400 403 throw exception to user 500 502
   * 503 change ip and retry
   */
  /**
   *  invalid param
   */
  val INVALID_PARAM = 400

  /**
   *  no right
   */
  val NO_RIGHT = 403

  /**
   *  not found
   */
  val NOT_FOUND = 404

  /**
   *  conflict
   */
  val CONFLICT = 409

  /**
   *  server error
   */
  val SERVER_ERROR = 500

  /**
   *  bad gateway
   */
  val BAD_GATEWAY = 502

  /**
   *  over threshold
   */
  val OVER_THRESHOLD = 503

  def apply(errCode: Int, errMsg: String): NacosException = NacosException(errCode, errMsg, null)

  def apply(errMsg: String): NacosException = NacosException(SERVER_ERROR, errMsg, null)
}
