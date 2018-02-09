# Entity

By now you should have enough some experience with `Entity`.
If not, refer to the "A Quick Look at Entity" section in the `Lenses` chapter.
Thus we can say the following about `Entity`.

<div class="callout callout-info">
An `Entity` is a simple structure that contains and manages two very
specific things:

- A `Position[A]` within the current search space of the problem
- A "state" that contains all addition data required by the `Entity`
  which is not managed by the `Position[A]`.
</div>

An `Enity` is used to represent a singular *thing* exploring the search space.
This *thing* changes from algorithm to algorithm.
[Gary][Link-Gary] gives a clear explanation in the following segment as to why CILib uses `Entity`.

<div class="callout callout-danger">
Within swarm intelligence, evolutionary computation and other, similar
algorithms, there is always a metaphor that the algorithm is based on.
Using this metaphor, the participants within the algorithm are also
appropriately named. For example, within a Particle Swarm Optimization (PSO),
the participants are referred to as Particles, with Individuals being
used in both Differential Evolution (DE) and Genetic Algorithms (GA).
Many other examples can easily be identified in available literature.

It is not practical to have several representations for a very similar
concept used within these algorithms. Based on experimentation within CIlib,
a common structure was identified that could be used to represent the
participants for these metaphor-based population based algorithms. We
refer, collectively, to these algorithm participants as `Entity` instances.
</div>