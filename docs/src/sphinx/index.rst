.. CIlib documentation master file, created by
   sphinx-quickstart on Sat May  7 12:25:42 2016.
   You can adapt this file completely to your liking, but it should at least
   contain the root `toctree` directive.

=================================
Welcome to CIlib's documentation!
=================================

.. warning::

   **CIlib is currently in active development.** Feedback and contributions are welcomed as we work to improve the library.

***************
Getting Started
***************

CIlib is currently available for Scala 2.11 (builds for 2.12 are being planned).

To get started, you need will need to include the required CIlib module into your project's build.sbt

   libraryDependencies += "net.cilib" %% "cilib-*" % "<version>"

CIlib consists of several modules, each containing specialized parts of the larger library. Please refer to
the project structure for a listing of each of the modules and what they provide. Additional dependencies
on community projects include the following:

.. hlist::
   :columns: 3

   * `Scalaz <https://github.com/scalaz/scalaz>`_
   * `Spire <https://github.com/non/spire>`_
   * `Monocle <http://julien-truffaut.github.io/Monocle/>`_

***********
Motivations
***********

CIlib is software library which aids in the experimentation and research of
Computational Intelligence algorithms. Previously, in version 1.0 and lower, CIlib
started demonstrating several shortcomings, and as a result, the current
development process began. In order to address these shortcomings, the following were
highlighted:

================
Approachability:
================

The learning needed to understand and use CIlib should remain as low as possible and the community
around the library should continue to be a helpful and supportive group of individuals. Questions
and contributions are always welcome, and the feedback will allow better decisions to be made which
will hopefully allow faster understanding of the library itself. If you have any feedback for us, we would
love to hear from you. Please contact is in ``#cilib`` on ``FreeNode`` or come chat to us
in the project's `Gitter channel <https://gitter.im/cirg-up/cilib>`_; alternatively feel free to
open an issue on the `project page <https://github.com/cirg-up/cilib>`_.

=======================
Well principled design:
=======================

It is very important to ensure that the library code is pure. This has many advantages
but, most importantly, it allows for the controlling of side-effects which is a primary concern,
especially when randomness is involved. As a consequence of this, and other aspects, CIlib makes
an active effort to address the following:

* **Correctness:** All algorithmic components should be correct and operate as intended, doing nothing
  more. Peer-review is indispensable in this regard, providing confidence that the implementations are
  correct and sound.
* **Type safety:** The use of types is a fantastic way to ensure that a program cannot represent
  invalid states. This removes a huge set of potential errors and ensures greater confidence, as the
  compiler is always double-checking the code.
* **Reproducability:** Within scientific research, being able to reproduce the work of another
  researcher is important. When complexities such as randomness are involved, this becomes much more
  difficult and is generally extremely cumbersome. CIlib, as a result, must allow for the perfect
  replication of experimental work.

*****************
Project structure
*****************

Trying to maintain a modular set of functionalities, CIlib consists of several sub-projects:

* *core* - contains typeclass definitions together with instances and various data structures
* *exec* - simplistic execution code allowing for experimental execution
* *de* - data structures and logic related to Differential Evolution
* *ga* - data structures and logic related to Genetic Algorithms
* *moo* - typeclasses, instances and data structures for Multi-Objective Optimization
* *pso* - data structures and logic related to Particle Swarm Optimization

.. toctree::
   :maxdepth: 2
   :hidden:

   Scaladoc <https://cirg-up.github.io/cilib/api>

.. toctree::
   :maxdepth: 1
   :caption: Design
   :hidden:

   design/overview
   design/position
   design/entity
   design/lenses
   design/rvar
   design/step
   StepS <design/step_with_state>

.. toctree::
   :maxdepth: 1
   :caption: Examples
   :hidden:

   examples/gbestpso

..
  Indices and tables
  ==================
  * :ref:`genindex`
  * :ref:`modindex`
  * :ref:`search`
