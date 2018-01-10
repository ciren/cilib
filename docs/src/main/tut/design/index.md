@@@ index

- [Overview](overview.md)
- [Lenses](lenses.md)
- [Position](position.md)
- [Entity](entity.md)
- [RVar](rvar.md)
- [Step](step.md)
- [Step with State](step_with_state.md)

@@@

# Design

CIlib is software library which aids in the experimentation and
research of Computational Intelligence algorithms. Previously, in
version 1.0 and lower, CIlib started demonstrating several
shortcomings, and as a result, the current development process
began. In order to address these shortcomings, the following goals
were highlighted:

## Principled design

It is very important to ensure that the library code is pure - thereby
reducing complexity. This has many advantages but, most importantly, it
allows for the controlling of side-effects which is a primary concern,
especially when randomness is involved. As a consequence of this, and
other aspects, CIlib makes an active effort to address the following:

- *Correctness:* All algorithmic components should be correct and
  operate as intended, doing nothing more. Peer-review is hugely valuable
  in this regard, providing the confidence that the implementations are
  correct and sound.
- *Type safety:* The use of types is a fantastic way to ensure that a
  program cannot represent invalid states. This removes a huge set of
  potential errors and ensures greater confidence, as the compiler is
  always double-checking the code.
- *Reproducability:* Within scientific research, being able to reproduce
  the work of another researcher is important. It's also a fundamental
  part of the scientific method. With complexities such as randomness,
  this becomes much more difficult and is generally extremely cumbersome.
  CIlib must allow for the perfect replication of experimental work.

## Project structure

Trying to maintain a modular set of functionalities, CIlib consists of
several sub-projects:

* *core* - contains typeclass definitions together with required data structures
* *exec* - simplistic execution code allowing for experimental execution
* *de* - data structures and logic related to Differential Evolution
* *docs* - sources for the website
* *ga* - data structures and logic related to Genetic Algorithms
* *moo* - typeclasses, instances and data structures for Multi-Objective
  Optimization
* *pso* - data structures and logic related to Particle Swarm Optimization

## Support

If you have any feedback or questions, please contact is in ``#cilib`` on
``FreeNode`` IRC or come chat to us in the project's
[Gitter channel](https://gitter.im/cirg-up/cilib); alternatively, feel free
to open an [issue](https://github.com/cirg-up/cilib/issues).