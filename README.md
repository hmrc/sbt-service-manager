
# sbt-service-manager

[![Build Status](https://travis-ci.org/hmrc/sbt-service-manager.svg?branch=master)](https://travis-ci.org/hmrc/sbt-service-manager) [ ![Download](https://api.bintray.com/packages/hmrc/sbt-plugin-releases/sbt-service-manager/images/download.svg) ](https://bintray.com/hmrc/sbt-plugin-releases/sbt-service-manager/_latestVersion)

An SBT plugin to start and stop service manager dependencies for integration test

Configuration
-------------

It requires to have configured `service-manager` in order to be able to use this plugin. You can find it how [here](https://github.com/hmrc/service-manager)

In your `project/plugins.sbt` file:
```
resolvers += Resolver.url("hmrc-sbt-plugin-releases",
  url("https://dl.bintray.com/hmrc/sbt-plugin-releases"))(Resolver.ivyStylePatterns)

addSbtPlugin("uk.gov.hmrc" % "sbt-service-manager" % "x.x.x")
```

where 'x.x.x' is the latest release as advertised above.

In your `project/FrontendBuild.scala` or `project/MicroserviceBuild.scala` after the line ```.settings(inConfig(TemplateItTest)(Defaults.itSettings): _*)```

1. Add list of services that are required to be started.

```
lazy val externalServices = List(
    ExternalService(name = "SERVICE1", enableTestOnlyEndpoints = true, extraConfig = Map("key" -> "value"), runFrom = Source),
    ExternalService(name = "SERVICE2", enableTestOnlyEndpoints = false, runFrom = Jar)
)
```

2. Add the line ```.settings(ServiceManagerPlugin.serviceManagerSettings)```
3. Add the line ```.settings(itDependenciesList := externalServices)```

The attributes of ```ExternalService``` are:

* **name**: Name of the service defined in the `service-manager` configuration.
* **enableTestOnlyEndpoints**: To enable the test only endpoints. Default value is `false`.
* **extraConfig**: Extra configuration required for the service. Default value is `Map()`.
* **runFrom**: Whether to run the service from 'Jar' or 'Source'. Default value is `Jar`.

Usage
-----

Running `sbt it:test` will start the dependencies configured before running the tests and stop them at then end.
Run `sbt startItDependencies` will start the dependencies configured.
Run `sbt stopItDependencies` will stop the dependencies configured.
Run `sbt itDependenciesList` will show the dependencies configured.

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
