package nacos.client.util

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

import com.typesafe.config.{ Config, ConfigValue, ConfigValueType }

import scala.util.Try

object Utils {
  import ConfigValueType._

  def configBoolean(c: Config, paths: String*): Option[Boolean] = configValue(c, paths: _*).flatMap { v =>
    v.valueType() match {
      case BOOLEAN => Some(v.unwrapped().asInstanceOf[Boolean])
      case STRING  => Try(v.unwrapped().asInstanceOf[String].toBoolean).toOption
      case NUMBER  => Try(if (v.unwrapped().asInstanceOf[Number].intValue() == 0) false else true).toOption
      case _       => None
    }
  }

  def configString(c: Config, paths: String*): Option[String] = configValue(c, paths: _*).flatMap {
    case v if v.valueType() == STRING => Some(v.unwrapped().toString)
    case _                            => None
  }

  def configInt(c: Config, paths: String*): Option[Int] = configValue(c, paths: _*).flatMap {
    case v if v.valueType() == NUMBER => Some(v.unwrapped().asInstanceOf[Number].intValue())
    case s if s.valueType() == STRING => Try(s.unwrapped().asInstanceOf[String].toInt).toOption
    case _                            => None
  }

  def configValue(c: Config, paths: String*): Option[ConfigValue] =
    paths.foldLeft(Option.empty[ConfigValue]) { (maybe, path) =>
      if (c.hasPathOrNull(path)) maybe else Some(c.getValue(path))
    }

  def md5: MessageDigest = MessageDigest.getInstance("MD5")

  def md5Hex(str: String): String = StringUtils.hex2Str(md5.digest(str.getBytes(StandardCharsets.UTF_8)))

}
