# Akka Discovery

适用于基于 Akka 的程序使用。

## 依赖

要在项目中使用，请添加以下依赖：

@@dependency[sbt,Maven,Gradle] {
  group="me.yangbajing.nacos4s"
  artifact="nacos-akka_$scala.binary_version$"
  version="$version$"
}

## 配置

配置 Akka Discovery 使用 `Nacos4sNamingService` 

@@snip [reference.conf](../../../../nacos-akka/src/main/resources/reference.conf)

`nacos4s.client.naming` 配置见： @ref[nacos4s.client.naming](./quickstart.md#hocon-完整配置)

## 示例

更多使用示例见 [测试](https://github.com/yangbajing/nacos-sdk-scala/blob/master/nacos-akka/src/test/scala/yangbajing/nacos4s/akka/NacosServiceDiscoveryTest.scala)

@@snip [NacosServiceDiscoveryTest](../../../../nacos-akka/src/test/scala/yangbajing/nacos4s/akka/NacosServiceDiscoveryTest.scala) { #NacosServiceDiscoveryTest }
