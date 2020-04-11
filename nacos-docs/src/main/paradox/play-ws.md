# Play WS

## 依赖

要在项目中使用，请添加以下依赖：

@@dependency[sbt,Maven,Gradle] {
  group="me.yangbajing.nacos4s"
  artifact="nacos-play-ws_$scala.binary_version$"
  version="$version$"
}

## 使用 - 依赖注入

并在 `application.conf` 配置文件中启用 `NacosWSModule` 模块。

@@snip [reference.conf](../../../../nacos-play-ws/src/main/resources/reference.conf)

与原生的 play-ws 使用唯一的区别就是在 **Inject** 时添加 `@Named("nacos")` 注解。

**Scala API**

@@snip [NacosWSClient](../../../src/main/scala/docs/play/ws/scaladsl/NacosWSClientController.scala) { #NacosWSClientController }

**Java API**

@@snip [NacosWSClient](../../../src/main/scala/docs/play/ws/javadsl/NacosWSClientController.java) { #NacosWSClientController }

## 使用 - 手动创建

**Scala API**

@@snip [NacosWSClientTest](../../../../nacos-play-ws/src/test/scala/yangbajing/nacos4s/play/ws/scaladsl/NacosWSClientTest.scala) { #NacosWSClientTest }

**Java API**

@@snip [NacosWSClientTest](../../../../nacos-play-ws/src/test/scala/yangbajing/nacos4s/play/ws/javadsl/NacosWSClientTest.scala) { #NacosWSClientTest }
