---
id: three-requirements
title: Pre-requisites
hide_title: true
---

# Pre-requisites

The experiences learned from the first version highlighted the following requirements:

## Correctness

Having algorithms be available within an open source project prevents accidental mistakes from going unnoticed.
Code may be reviewed at any time, allowing for open discussion and corrections to mistakes.
The most important property is that an algorithm be correctly implemented.
When any uncertainty presents itself regarding algorithm implementation, the correctness of the implementation will always be preferred over any improvement to the speed of algorithm execution, etc.
Other improvements may be incorporated once the correctness has been established and is verifiable.

As an additional impact to the need for correctness, data should be immutable.
Enforcing immutable data will also prevent external functions and methods from altering data in error.

## Type-safety

The Java implementation and the influence of the `simulator` program resulted in an execution structure that relied heavily on reflection and type casting.
Furthermore, the casting became a neccessary evil when working with deep inheritance hierarchies.
Deep inheritance results in parent classes and interfaces losing information becuase they are too general.
An example of such information loss is present in the collections hierarchy of Java where data structures extend the `Collection` interface.
In some cases, the functionality defined in `Collection` is not applicable to a data structure, resulting in oddly behaving methods.

Other examples do exist, but the point has been made.
At the end of the day, such code is fragile.
**Very fragile.**

Instead, the type system of the language should be exploited as much as possible.
Using the type system, truly generic code may be written where only unneccessary information for the current operation would be ignored.
The type system can also ensure that invalid program states are simply not possible to construct, reducing possible defects even further.
Given that the type system can provide such safety, no intermediate representation (such as the XML based specification) would be needed.

## Reproducibility

The abiltity to reproduce the results of a published work is a fundamental part of the scientific method.
With effects such as randomness applied to algorithms, reproducing results becomes much more difficult.
The definition of algorithms should allow for this property, effectively allowing an algorithm to be a deterministic process.
This would allow for explicit testing of algorithms, without the need of attempts to verify the results using a sample of execution results and statistics.
