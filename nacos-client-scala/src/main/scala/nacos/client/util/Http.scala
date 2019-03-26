package nacos.client.util

import java.nio.charset.StandardCharsets

import com.typesafe.config.Config
import requests.RequestBlob.EmptyRequestBlob
import requests.{ Request, RequestBlob, Response }

case class WrapResponse(response: Response) extends AnyVal {
  def is2xx: Boolean        = (response.statusCode >> 2) == 2
  def is3xx: Boolean        = (response.statusCode >> 2) == 3
  def is4xx: Boolean        = (response.statusCode >> 2) == 4
  def is5xx: Boolean        = (response.statusCode >> 2) == 5
  def statusCode: Int       = response.statusCode
  def statusMessage: String = response.statusMessage
  def text: String          = response.text(StandardCharsets.UTF_8)
}

final class Http private (val setting: NacosSetting) {
  private var _isHealth = true

  def isHealth: Boolean = _isHealth

  def request(path: String, queries: Seq[(String, String)] = Nil): Request =
    Request(generateUrl(path), params = queries)

  def get(request: Request): WrapResponse = {
    wrapResponse(requests.get(request, EmptyRequestBlob))
  }

  def post(request: Request, data: RequestBlob): WrapResponse = {
    wrapResponse(requests.post(request, data))
  }

  def put(request: Request, data: RequestBlob): WrapResponse = {
    wrapResponse(requests.put(request, data))
  }

  def delete(request: Request): WrapResponse = {
    wrapResponse(requests.delete(request, EmptyRequestBlob))
  }

  private def wrapResponse(response: Response): WrapResponse = {
    _isHealth = (response.statusCode >> 2) == 2
    WrapResponse(response)
  }

  private def generateUrl(path: String): String = {
    val sb = StringBuilder.newBuilder
    sb.append(if (setting.secure) "https" else "http")
    sb.append("://")
    sb.append(setting.serverAddr)
    if (path.head != '/') {
      sb.append('/')
    }
    sb.append(path)
    sb.toString()
  }

}

object Http {
  def apply(config: Config): Http = new Http(NacosSetting(config))
}
