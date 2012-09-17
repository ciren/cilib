0.7.5:
 - New clustering implementation.
 - Removed the "games" package due to its unmaintained state. It will be
   revised in a future release.
 - Removed several methods that are largely not required (in Meansurements
   and other related classes)
 - FunctionMinimizationProblem and FunctionMAximizationProblem have been
   removed. Instead, use FunctionOptimizationProblem with a given
   Objective. The only valid Objectives are Minimize and Maximize, with
   Minimize being assumed to be the default, if none is specified.
