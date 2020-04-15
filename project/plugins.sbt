logLevel := Level.Info

resolvers += Resolver.bintrayIvyRepo("2m", "sbt-plugins")

addSbtPlugin("de.heikoseeberger" % "sbt-header" % "5.2.0")
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.9")
addSbtPlugin("io.github.jonas" % "sbt-paradox-material-theme" % "0.6.0")
addSbtPlugin("com.dwijnand" % "sbt-dynver" % "4.0.0")
addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "1.0.0")
addSbtPlugin("com.lightbend.sbt" % "sbt-java-formatter" % "0.4.4")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.2.1")
addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.9.7-1")
addSbtPlugin("org.foundweekends" % "sbt-bintray" % "0.5.5")
addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "1.3.2")
addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages" % "0.6.3")
