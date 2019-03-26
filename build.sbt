import Commons._
import Environment._

buildEnv in Global := {
  sys.props
    .get("build.env")
    .orElse(sys.env.get("BUILD_ENV"))
    .flatMap {
      case "prod"  => Some(BuildEnv.Production)
      case "stage" => Some(BuildEnv.Stage)
      case "test"  => Some(BuildEnv.Test)
      case "dev"   => Some(BuildEnv.Developement)
      case _       => None
    }
    .getOrElse(BuildEnv.Developement)
}

scalaVersion in Global := "2.12.8"

scalafmtOnCompile in Global := true

lazy val root = Project(id = "nacos-sdk-scala", base = file("."))
  .aggregate(
    nacosDocs,
    nacosAkka,
    nacosClientScala
  )
  .settings(Publishing.noPublish: _*)
  .settings(Environment.settings: _*)

lazy val nacosDocs = _project("nacos-docs")
  .enablePlugins(ParadoxMaterialThemePlugin)
  .dependsOn(
    nacosAkka,
    nacosClientScala
  )
  .settings(Publishing.noPublish: _*)
  .settings(
    Compile / paradoxMaterialTheme ~= {
      _.withLanguage(java.util.Locale.SIMPLIFIED_CHINESE)
        .withColor("indigo", "red")
        .withRepository(uri("https://github.com/yangbajing/nacos-sdk-scala"))
        .withSocial(
          uri("http://yangbajing.github.io/nacos-sdk-scala/"),
          uri("https://github.com/yangbajing"),
          uri("https://weibo.com/yangbajing")
        )
    },
    paradoxProperties ++= Map(
      "github.base_url" -> s"https://github.com/yangbajing/nacos-sdk-scala/tree/${version.value}",
      "version" -> version.value,
      "scala.version" -> scalaVersion.value,
      "scala.binary_version" -> scalaBinaryVersion.value,
      "scaladoc.akka.base_url" -> s"http://doc.akka.io/api/$versionAkka",
      "akka.version" -> versionAkka
    )
  )

lazy val nacosAkka = _project("nacos-akka")
  .dependsOn(nacosClientScala % "compile->compile;test->test")
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-stream" % versionAkka
    )
  )

lazy val nacosClientScala = _project("nacos-client-scala")
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe" % "config" % "1.3.3",
      "com.lihaoyi" %% "requests" % "0.1.7",
      "org.scalatest" %% "scalatest" % "3.0.7" % Test
    )
  )
  .settings(Publishing.noPublish)

def _project(name: String, _base: String = null) =
  Project(id = name, base = file(if (_base eq null) name else _base))
    .settings(basicSettings: _*)
    .settings(Publishing.publishing: _*)

lazy val versionAkka = "2.5.21"
