import Environment.{buildEnv, BuildEnv}
import de.heikoseeberger.sbtheader.HeaderPlugin.autoImport.{headerLicense, HeaderLicense}
import sbt.Keys._
import sbt._
import sbtassembly.MergeStrategy

object Commons {

  import Environment.{buildEnv, BuildEnv}
  import sbtassembly.AssemblyKeys.{assembly, assemblyMergeStrategy}
  import sbtassembly.{MergeStrategy, PathList}

  def basicSettings =
    Seq(
      organization := "com.helloscala.fusion",
      organizationName := "ihongka",
      organizationHomepage := Some(url("http://ihongka.cn")),
      homepage := Some(url("http://ihongka.github.cn/akka-fusion")),
      startYear := Some(2018),
      licenses += ("Apache-2.0", new URL("https://www.apache.org/licenses/LICENSE-2.0.txt")),
      headerLicense := Some(HeaderLicense.ALv2("2019", "ihongka")),
      scalacOptions ++= {
        var list = Seq(
          "-encoding",
          "UTF-8", // yes, this is 2 args
          "-feature",
          "-deprecation",
          "-unchecked",
          "-Xlint",
          "-Yno-adapted-args", //akka-http heavily depends on adapted args and => Unit implicits break otherwise
          "-Ypartial-unification",
          "-opt:l:inline",
          "-opt-inline-from",
          "-Ywarn-dead-code"
        )
        if (scalaVersion.value.startsWith("2.12")) {
          list ++= Seq("-opt:l:inline", "-opt-inline-from")
        }
        if (buildEnv.value != BuildEnv.Developement) {
          list ++= Seq("-Xelide-below", "2001")
        }
        list
      },
      javacOptions in Compile ++= Seq("-Xlint:unchecked", "-Xlint:deprecation"),
      javaOptions in run ++= Seq("-Xms128m", "-Xmx1024m", "-Djava.library.path=./target/native"),
      shellPrompt := { s =>
        Project.extract(s).currentProject.id + " > "
      },
      test in assembly := {},
      assemblyMergeStrategy in assembly := {
        case PathList("javax", "servlet", xs @ _*)                => MergeStrategy.first
        case PathList("io", "netty", xs @ _*)                     => MergeStrategy.first
        case PathList("jnr", xs @ _*)                             => MergeStrategy.first
        case PathList("com", "datastax", xs @ _*)                 => MergeStrategy.first
        case PathList("com", "kenai", xs @ _*)                    => MergeStrategy.first
        case PathList("org", "objectweb", xs @ _*)                => MergeStrategy.first
        case PathList(ps @ _*) if ps.last endsWith ".html"        => MergeStrategy.first
        case "application.conf"                                   => MergeStrategy.concat
        case "META-INF/io.netty.versions.properties"              => MergeStrategy.first
        case PathList("org", "slf4j", xs @ _*)                    => MergeStrategy.first
        case "META-INF/native/libnetty-transport-native-epoll.so" => MergeStrategy.first
        case x =>
          val oldStrategy = (assemblyMergeStrategy in assembly).value
          oldStrategy(x)
      },
//            resolvers ++= Seq(
//            "elasticsearch-releases" at "https://artifacts.elastic.co/maven"
//        ),
      fork in run := true,
      fork in Test := true,
      parallelExecution in Test := false
    ) ++ Environment.settings // ++ Formatting.settings

}

object Publishing {

  lazy val publishing = Seq(
    publishTo := (if (version.value.endsWith("-SNAPSHOT")) {
                    Some(
                      "Helloscala_sbt-public_snapshot" at "http://118.89.245.43:8081/artifactory/sbt-release;build.timestamp=" + new java.util.Date().getTime)
                  } else {
                    Some("Helloscala_sbt-public_release" at "http://118.89.245.43:8081/artifactory/libs-release")
                  }),
    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials_ihongka")
  )

  lazy val noPublish = Seq(
    publish := ((): Unit),
    publishLocal := ((): Unit),
    publishTo := None
  )
}

object Environment {

  object BuildEnv extends Enumeration {
    val Production, Stage, Test, Developement = Value
  }

  val buildEnv = settingKey[BuildEnv.Value]("The current build environment")

  val settings = Seq(
    onLoadMessage := {
      // old message as well
      val defaultMessage = onLoadMessage.value
      val env = buildEnv.value
      s"""|$defaultMessage
          |Working in build environment: $env""".stripMargin
    }
  )
}


