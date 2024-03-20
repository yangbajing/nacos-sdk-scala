# Quick Start

Nacos SDK for Scala 基于 Java 客户端 [`nacos-client`](https://nacos.io/zh-cn/docs/sdk.html) 做了封装，使得在 Scala
下更易使用。支持使用 [HOCON](https://github.com/lightbend/config) 做为配置文件，支持 Pekko Discovery 和 Play-WS。

## 依赖

要在项目中使用，请添加以下依赖：

@@dependency[sbt,Maven,Gradle] {
group="me.yangbajing.nacos4s"
artifact="nacos-client-scala_$scala.binary_version$"
version="$version$"
}

并添加以下依赖源：

```scala
resolvers += Resolver.bintrayRepo("helloscala", "maven")
```

## 编程使用

### Nacos4sNamingService

@@snip [NamingDemo](../../../src/main/scala/docs/naming/NamingDemo.scala) { #NamingDemo }

### Nacos4sConfigService

@@snip [ConfigDemo](../../../src/main/scala/docs/config/ConfigDemo.scala) { #ConfigDemo }

## HOCON 配置

Nacos4s 支持使用 HOCON 作为配置文件。

*`application.conf`*

@@snip [application.conf](../../../src/main/resources/application.conf)

*代码*

```scala
val configService = Nacos4s.configService(ConfigFactory.load().getConfig("nacos4s.client.config"))
val namingService = Nacos4s.namingService(ConfigFactory.load().getConfig("nacos4s.client.config"))
```

## 服务自动注册

在使用 ConfigFactory 初始化 `Nacos4sNamingService` 时设置 `autoRegisterInstance = on` 可自动将服务注册到 Nacos。

## HOCON 完整配置

@@snip [reference.conf](../../../../nacos-client-scala/src/main/resources/reference.conf)

## 示例

更多 **Nacos4sConfigService**
使用示例见 [测试](https://github.com/yangbajing/nacos-sdk-scala/blob/master/nacos-client-scala/src/test/scala/yangbajing/nacos4s/client/config/Nacos4sConfigServiceTest.scala) ：

@@snip [Nacos4sConfigServiceTest](../../../../nacos-client-scala/src/test/scala/yangbajing/nacos4s/client/config/Nacos4sConfigServiceTest.scala) {
#ConfigService }

更多 **Nacos4sNamingService**
使用示例见 [测试](https://github.com/yangbajing/nacos-sdk-scala/blob/master/nacos-client-scala/src/test/scala/yangbajing/nacos4s/client/naming/Nacos4sNamingServiceTest.scala) ：

@@snip [Nacos4sNamingServiceTest](../../../../nacos-client-scala/src/test/scala/yangbajing/nacos4s/client/naming/Nacos4sNamingServiceTest.scala) {
#NamingService }
