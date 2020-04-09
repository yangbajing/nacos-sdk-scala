# nacos-sdk-scala

支持 Scala 2.12, 2.13。

Nacos 版本：1.2.1。

```sbt
// Scala API
libraryDependencies += "me.yangbajing.nacos4s" %% "nacos-client-scala" % "1.2.0"

// Akka Discovery
libraryDependencies += "me.yangbajing.nacos4s" %% "nacos-akka" % "1.2.0"

// Play WS
libraryDependencies += "me.yangbajing.nacos4s" %% "nacos-play-ws" % "1.2.0"
```

需要添加以下源：

```sbt
resolvers += Resolver.bintrayRepo("helloscala", "maven")
```

## 阅读文档

**在线文档**：[https://yangbajing.github.io/nacos-sdk-scala](https://yangbajing.github.io/nacos-sdk-scala)

**本地阅读**：

以下命令将自动编译并打开默认浏览器以阅读文档：

```
git clone https://github.com/yangbajing/nacos-sdk-scala
cd nacos-sdk-scala
sbt nacos-docs/paradoxBrowse
```
