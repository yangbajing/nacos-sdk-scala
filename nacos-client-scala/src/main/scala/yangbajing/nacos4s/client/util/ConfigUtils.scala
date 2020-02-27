/*
 * Copyright 2020 me.yangbajing
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package yangbajing.nacos4s.client.util

import java.util.Properties

import com.typesafe.config.{ Config, ConfigObject, ConfigValueType }

object ConfigUtils {
  def discoveryConfig(config: Config): Config = {
    val method = config.getString("akka.discovery.method")
    if (config.hasPath(s"akka.discovery.$method")) config.getConfig(s"akka.discovery.$method")
    else config.getConfig(method)
  }

  def toProperties(config: Config): Properties = {
    def make(props: Properties, parentKeys: String, obj: ConfigObject): Unit =
      obj.keySet().forEach { key =>
        val value = obj.get(key)
        val propKey =
          if (StringUtils.isNoneBlank(parentKeys)) parentKeys + "." + key
          else key
        value.valueType() match {
          case ConfigValueType.OBJECT =>
            make(props, propKey, value.asInstanceOf[ConfigObject])
          case _ =>
            props.put(propKey, value.unwrapped().toString)
        }
      }

    val obj = config.root()
    val props = new Properties()
    make(props, "", obj)
    props
  }
}
