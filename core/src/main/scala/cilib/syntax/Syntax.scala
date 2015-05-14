package cilib
package syntax

trait Syntaxes {

  object step extends ToStepOps

  object all extends ToOps

}

trait ToOps extends ToStepOps
