// Comment to get more information during initialization
logLevel := Level.Warn

resolvers += Resolver.sbtPluginRepo("releases")

addSbtPlugin("de.heikoseeberger" % "sbt-header" % "5.0.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-multi-jvm" % "0.4.0")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.7")

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.9.0")

addSbtPlugin("com.geirsson" % "sbt-scalafmt" % "1.5.1")

addSbtPlugin("io.github.jonas" % "sbt-paradox-material-theme" % "0.6.0")

//addSbtPlugin("io.gatling" % "gatling-sbt" % "3.0.0")
