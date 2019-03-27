import sbt.Keys.onLoadMessage
import sbt.settingKey

object Environment {
  object BuildEnv extends Enumeration {
    val Production, Stage, Test, Developement = Value
  }

  val buildEnv = settingKey[BuildEnv.Value]("The current build environment")

  val settings = Seq(onLoadMessage := {
    // old message as well
    val defaultMessage = onLoadMessage.value
    val env            = buildEnv.value
    s"""|$defaultMessage
        |Working in build environment: $env""".stripMargin
  })
}
