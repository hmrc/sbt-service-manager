resolvers += "HMRC Releases" at "https://artefacts.tax.service.gov.uk/artifactory/hmrc-releases/"

resolvers += Resolver.url("hmrc-sbt-plugin-releases", url("https://dl.bintray.com/hmrc/sbt-plugin-releases"))(Resolver.ivyStylePatterns)

resolvers += "Typesafe Releases" at "https://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("uk.gov.hmrc" % "sbt-auto-build" % "2.13.0")

addSbtPlugin("uk.gov.hmrc" % "sbt-git-versioning" % "2.2.0")

addSbtPlugin("uk.gov.hmrc" % "sbt-artifactory" % "1.13.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.6.1")