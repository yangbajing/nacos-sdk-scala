package nacos.client.util

import java.nio.charset.StandardCharsets

import com.typesafe.config.Config
import requests.RequestBlob.EmptyRequestBlob
import requests.{Request, RequestBlob, Response}

import scala.util.Random

case class WrapResponse(response: Response) extends AnyVal {
  def is2xx: Boolean        = (response.statusCode >> 2) == 2
  def is3xx: Boolean        = (response.statusCode >> 2) == 3
  def is4xx: Boolean        = (response.statusCode >> 2) == 4
  def is5xx: Boolean        = (response.statusCode >> 2) == 5
  def statusCode: Int       = response.statusCode
  def statusMessage: String = response.statusMessage
  def text: String          = response.text(StandardCharsets.UTF_8)
}

final class HttpAgent private (val setting: NacosSetting) {
  private var _isHealth           = true
  val serverAddrs: Vector[String] = setting.serverAddr.split(',').toVector
  val name: String                = serverAddrs.map(_.replace(':', '_')).mkString("-")
  def isHealth: Boolean           = _isHealth

  def request(path: String, queries: Seq[(String, String)] = Nil, headers: Seq[(String, String)] = Nil): Request =
    Request(path, params = queries, headers = headers)

  def get(request: Request): WrapResponse = {
    wrapResponse(requests.get(wrapRequest(request), EmptyRequestBlob))
  }

  def post(request: Request, data: RequestBlob): WrapResponse = {
    wrapResponse(requests.post(wrapRequest(request), data))
  }

  def put(request: Request, data: RequestBlob): WrapResponse = {
    wrapResponse(requests.put(wrapRequest(request), data))
  }

  def delete(request: Request): WrapResponse = {
    wrapResponse(requests.delete(wrapRequest(request), EmptyRequestBlob))
  }

  private def wrapResponse(response: Response): WrapResponse = {
    _isHealth = (response.statusCode >> 2) == 2
    WrapResponse(response)
  }

  private def wrapRequest(request: Request): Request = {
    val path = request.url
    val sb = StringBuilder.newBuilder
    sb.append(if (setting.secure) "https" else "http")
    sb.append("://")
    sb.append(serverAddrs(Random.nextInt(serverAddrs.size)))
    if (path.head != '/') {
      sb.append('/')
    }
    sb.append(path)
    val url = sb.toString()
    request.copy(url = url)
  }

}

object HttpAgent {
  def apply(config: Config): HttpAgent = new HttpAgent(NacosSetting(config))
}
