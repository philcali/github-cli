name := "project-finder"

version := "1.0"

organization := "com.github.philcali"

libraryDependencies <++= (scalaVersion) (sv => Seq(
  "net.databinder.dispatch" %% "dispatch-core" % "0.9.4",
  "com.github.scopt" %% "scopt" % "2.1.0",
  "org.scala-lang" % "jline" % sv
))

resolvers += "sonatype-public" at "https://oss.sonatype.org/content/groups/public"
