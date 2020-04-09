# Play WS

## 依赖

要在项目中使用，请添加以下依赖：

@@dependency[sbt,Maven,Gradle] {
  group="me.yangbajing.nacos4s"
  artifact="nacos-play-ws_$scala.binary_version$"
  version="$version$"
}

## 使用

与原生的 play-ws 使用唯一的区别就是在 **Inject** 时添加 `@Named("nacos")` 注解。

### Scala API

@@snip [NacosWSClient](../../../src/main/scala/docs/play/ws/scaladsl/NacosWSClientController.scala) { #NacosWSClientController }

### Java API

@@snip [NacosWSClient](../../../src/main/scala/docs/play/ws/javadsl/NacosWSClientController.java) { #NacosWSClientController }
