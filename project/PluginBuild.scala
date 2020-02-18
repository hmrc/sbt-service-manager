import sbt.Keys._

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
