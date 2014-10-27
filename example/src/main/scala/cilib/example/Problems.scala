package cilib

/**
  Examples of how to define "Problem" instances.

  There are two types of problems: dynamic and static. With a static
  problem, the problem space never changes, but in the dynamic environment,
  the problem may change at any time.

  There is a very strong link between the quantification of a problem solution
  and the type of fitness that is the result:
  * Valid(x) - A valid fitness in the environment with `x` and the value
  * Penalty(x, y) - A valid fitness, but the solution has had a penalty of `y` applied

  */
object Problems {

  /* Some of the more common static benchmark problems */
  val spherical = Problem.static((a: List[Double]) => Valid(a.map(x => x*x).sum))

  // Not sure where to put these yet....

  /* G13 Problems. Runarrson */

  val g1 = Problem.constrain(spherical, List((a: List[Double]) => Violation()))

}
