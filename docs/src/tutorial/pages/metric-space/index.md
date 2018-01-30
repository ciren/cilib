# Metric Space

In this chapter we will start off with a brief explanation of "what are metric spaces".
After which we explore CILib's implementations of certain metric spaces.
It should be noted that the focus of this chapter is learning what and how metric spaces are used in CILib, not actually learning metric spaces.

## Brief Definition of Metric Spaces

A MetricSpace is a set together with a notion of distance between elements.
Those distances, taken together, are called a metric on the set.
Distance is computed by a function dist which has the following four laws:

* Non-negative: for all x y. dist x y >= 0
* Identity of indiscernibles: for all x y. dist x y == 0 <=> x == y
* Symmetry: for all x y. dist x y == dist y x
* Triangle inequality: for all x y z. dist x z <= dist x y + dist y z

The [Wikipedia][metric-space-link] article can provide more details on metric spaces if you are unfamiliar.