## Modules in CILib

CILib currently offers several modules that would be available to us.

- core - Contains type class definitions together with required data structures.
- exec - Simplistic execution code allowing for experimental execution.
- de - Data structures and logic related to Differential Evolution.
- docs - Sources for the website.
- ga - Data structures and logic related to Genetic Algorithms.
- moo - Type classes, instances and data structures for Multi-Objective Optimization.
- pso - Data structures and logic related to Particle Swarm Optimization.

In the next part of the book our focus is going to be on the `core` module.
So let's include that one to start off with.
Head over to your `build.sbt` file.
It should look very similar to this

```scala
name := "CILib-Demo"
version := "0.1"
scalaVersion := "2.12.4"
```

We are now going to add a library dependency by making the necessary adjustments to your `build.sbt` file to include the `cilib-core` module.
It should now look something similar to this

```scala
name := "CILib-Demo"
version := "0.1"
scalaVersion := "2.12.4"
resolvers ++= Seq(Resolver.sonatypeRepo("snapshots"))
libraryDependencies ++= Seq("net.cilib" %% "cilib-core" % "@CILIB_VERSION@")
```

For those of you using InteliJ it should provide you with the option to refresh the project otherwise you can do it yourself by hovering over the dependency and clicking the light bulb. If you are using the terminal exit the project and re-enter it with `sbt console`.

Congratulations! You now have the `core` of CILib in your project.
In general, however, you can just use the following

```scala
libraryDependencies ++= Seq("net.cilib" %% "cilib-<module>" % "<version>")
```
