/*
 * Copyright 2020 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc

import sbt.Keys.test
import sbt._

object ServiceManagerPlugin extends AutoPlugin {

  /**
  * Defines all settings/tasks that get automatically imported,
  * when the plugin is enabled
  */
  object Keys {
    val startItDependencies = taskKey[Unit]("Start dependencies for integration tests")
    val stopItDependencies = taskKey[Unit]("Stop dependencies for integration tests")
    val itDependenciesList = settingKey[List[ExternalService]]("List dependencies for integration tests")
  }

  import Keys._

  lazy val serviceManagerConfiguration = Def.setting(new ServiceManagerConfiguration(itDependenciesList.value))

  lazy val serviceManagerSettings = Seq(
    itDependenciesList := List.empty,
    startItDependencies := {
      serviceManagerConfiguration.value.start()
    },
    stopItDependencies := {
      serviceManagerConfiguration.value.stop()
    },
    test in IntegrationTest := {
      stopItDependencies
        .dependsOn(test in IntegrationTest)
        .dependsOn(startItDependencies).value
    }
  )

}

sealed trait RunFrom

case object Source extends RunFrom
case object Jar extends RunFrom

case class ExternalService(name: String, enableTestOnlyEndpoints: Boolean = false, extraConfig: Map[String, String] = Map.empty, runFrom: RunFrom = Jar) {
  val configToString = {
    val fullConfig = if (enableTestOnlyEndpoints) extraConfig ++ Map("application.router" -> "testOnlyDoNotUseInAppConf.Routes") else extraConfig
    if (fullConfig.isEmpty) ""
    else
      s""""$name": [${fullConfig.toList.map(t => s""""-D${t._1}=${t._2}"""").mkString(",")}]"""
  }
}

class ServiceManagerConfiguration(externalServices: List[ExternalService]) {

  require(externalServices.nonEmpty, "externalServices can not be empty")

  val serviceManagerWorkspaceDirectoryPath = sys.env.getOrElse("WORKSPACE", "/path/to/service/manager")

  import sys.process._

  def stopServicesCommand() = {
    val stopCommand = s"sm --stop ${externalServices.map(_.name).mkString(" ")}"
    println("Stopping services:")
    println(stopCommand)
    stopCommand
  }

  val smConfigurationDirectory = s"$serviceManagerWorkspaceDirectoryPath/service-manager-config"
  val cloneConfiguration = s"rm -rf $serviceManagerWorkspaceDirectoryPath/service-manager-config" #&& s"git clone git@github.com:hmrc/service-manager-config.git $serviceManagerWorkspaceDirectoryPath/service-manager-config"
  val cloneConfigurationIfNeeded = s"test -d $smConfigurationDirectory" #|| cloneConfiguration

  def start() = {

    val (servicesFromJar, servicesFromSources) = externalServices.partition {
      case ExternalService(_, _, _, Jar) => true
      case _ => false
    }

    val startCommandFromJar = runServicesCommand(servicesFromJar, runFromBinary = true)
    val startCommandFromSource = runServicesCommand(servicesFromSources, runFromBinary = false)

    val setupCommands =
      "echo ---------- cloning config if needed" #&&
        cloneConfigurationIfNeeded #&&
        stopServicesCommand() #&&
        "echo ---------- cleaning logs" #&&
        "sm --cleanlogs" #&&
        s"echo ---------- starting services from sources: ${startCommandFromSource.mkString(" ")}" #&&
        startCommandFromSource #&&
        s"echo ---------- starting services from jar: ${startCommandFromJar.mkString(" ")}" #&&
        startCommandFromJar

    val setupExitCode = setupCommands.run().exitValue()
    if (setupExitCode != 0) throw new RuntimeException(s"Test setup failed. Exit code: $setupExitCode")
  }

  def stop() = stopServicesCommand().run()

  private def runServicesCommand(externalServices: List[ExternalService], runFromBinary: Boolean): Seq[String] = {
    if (externalServices.isEmpty) Seq(s"echo", s"no services required running from runFromBinary $runFromBinary")
    else {
      val runFromBinaryFlag = if (runFromBinary) List("-r") else Nil
      Seq("sm", "--start") ++ externalServices.map(_.name) ++ List("--appendArgs", "{" + externalServices.map(_.configToString).filter(_.nonEmpty).mkString(",") + "}", "--wait", "240") ++ runFromBinaryFlag
    }
  }
}
