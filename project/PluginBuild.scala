import sbt.Keys._
import sbt._
import uk.gov.hmrc.DefaultBuildSettings._
import uk.gov.hmrc.versioning.SbtGitVersioning
import uk.gov.hmrc.SbtAutoBuildPlugin

object PluginBuild extends Build {

  val pluginName = "sbt-service-manager"

  lazy val root = Project(pluginName, base = file("."))
    .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning)
    .settings(
      sbtPlugin := true,
      targetJvm := "jvm-1.7",
      scalaVersion := "2.10.5",
      organization := "uk.gov.hmrc",
      ArtifactDescription(),
      resolvers += Resolver.url(
        "sbt-plugin-releases",
        url("https://dl.bintray.com/content/sbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns),
      resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"
    ).settings(addSbtPlugin("com.typesafe.sbt" %% "sbt-native-packager" % "1.1.5"))

}

object ArtifactDescription {

  def apply() =
    pomExtra := <url>https://www.gov.uk/government/organisations/hm-revenue-customs</url>
      <licenses>
        <license>
          <name>Apache 2</name>
          <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
        </license>
      </licenses>
      <scm>
        <connection>scm:git@github.com:hmrc/sbt-service-manager.git</connection>
        <developerConnection>scm:git@github.com:hmrc/sbt-service-manager.git</developerConnection>
        <url>git@github.com:hmrc/sbt-service-manager.git</url>
      </scm>
      <developers>
        <developer>
          <id>carlosgarciapuerta</id>
          <name>Carlos Garcia Puerta</name>
          <url>http://www.equalexperts.com</url>
        </developer>
        <developer>
          <id>ianforsey</id>
          <name>Ian Forsey</name>
          <url>http://www.equalexperts.com</url>
        </developer>
        <developer>
          <id>brunobonanno</id>
          <name>Bruno Bonanno</name>
          <url>http://www.equalexperts.com</url>
        </developer>
      </developers>
}
