# 导读

## 概览

Nacos SDK for Scala 是基于 [Nacos Java SDK](https://nacos.io/zh-cn/docs/sdk.html) 的封装，使得在 Scala 下更易使用。
支持使用 [HOCON](https://link.zhihu.com/?target=https%3A//github.com/lightbend/config) 做为配置文件，支持
[Akka Discovery](https://doc.akka.io/docs/akka/current/discovery/index.html) 和
[Play-WS](https://www.playframework.com/documentation/2.8.x/ScalaWS) 。

Nacos SDK for Scala 既可以 ***将 Nacos 引入 Scala 生态环境***，作为微服务应用里的配置管理和服务发现机制。同时，也可以
***将 Scala/Akka/Play 引入 Java 以及 Spring 微服务环境***，可以让你在微服务开发中混合使用 Java/Spring Cloud、Scala 应用、
Akka 应用和 Play 应用。它们都通过 Nacos 作为统一的配置管理和服务发现机制。

要快速上手请访问： @ref[Quick Start](quickstart.md) 。

*对于很多 Scala 爱好者，若公司主要使用 Spring 进行业务开发，恰好又使用 Nacos 作为配置管理与服务发现工具。那你可以使用此 SDK 将使用 Scala
实现的服务接入公司的 Spring 生态里。*

## 背景

作者是一个 12 年的开发老兵，从 2012 年开始接触并在工作中使用 Scala。经历过纯 Scala 的业务开发，也经历过纯 Java/Spring 的业务开发，
也用 Scala 写过 Spring 应用。但这样一来有个问题，在单个服务里混用 Java 和 Scala 两种语言造成不懂 Scala 的开发人员难以接手维护，
而且也造成代码理解和维护困难……。后来，随着微服务的兴起，各服务之间通过接口调用，服务内部实现细节被隐藏起来。作者就在思考是否可以将某些服务使用
Scala 开发并和 Java/Spring 的服务相互配合。

再后来在网上发现了阿里开发的 Nacos，对其作了短暂调研后将其引入作为我们的配置管理和服务发现机制。Nacos 的如下优点很吸引我们：

- **中文社区**：学习和问题解决更方便
- **可单独使用**：纯 Scala 应用也可以使用
- **支持 Spring Cloud**：团队里大部分成员都有 Spring 开发经验 

这样，在 2019 对公司产品进行微服务改造时就选择了 Nacos，并将消息、任务调度、文件、日志等工具性质的服务使用 Scala/Akka 实现，其余业务服务继续
使用 Spring。Spring 与 Scala 服务之间通过 gRPC 相互调用，文件服务因需要向公网提供接口，使用 Akka HTTP 提供了 RESTful 服务
（有兴趣的读者可以参阅作者翻译的 Akka HTTP 中文文档：[https://www.yangbajing.me/akka-http/](https://www.yangbajing.me/akka-http/)）。

## 关于作者

作者是一个 Java/Scala 程序员，在日常工作中大量使用 Scala/Akka。

- 博客：[https://www.yangbajing.me/](https://www.yangbajing.me/)
- 知乎专栏：[https://zhuanlan.zhihu.com/yangbajing](https://zhuanlan.zhihu.com/yangbajing)
