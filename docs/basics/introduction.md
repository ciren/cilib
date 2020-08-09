---
id: basic-tutorial
title: Preface
hide_title: true
---

# Preface

## About the basic tutorial

This basic tutorial is aimed at walking you through the different parts of CILib (Computational Intelligence Library).
CILib is written in Scala and familiarity with the language is recommended.
Scala is a JVM language which allows for the expression and usage of more advanced type system capabilities, which Java, nor the other JVM languages are able to provide.

[Essential Scala](https://underscore.io/training/courses/essential-scala/) and [Advanced Scala With Cats](https://underscore.io/training/courses/advanced-scala/) are freely available, online Scala resources.
It should be noted that these resources recommend practices that are not always followed within CIlib.
The core focus in CIlib development is to *always* prefer a functional approach for implementation and avoid the use of object-orientation as much as possible.
The benefits obtained through this style of code far outweigh any perceived overhead!

> #### Compiler verified code samples
> Throughout the tutorial you will see a lot of theory along side practical examples represented through blocks of code.
> These blocks of code are verified during the documentation processing and will always be up to date with the referenced version of CIlib.


## Knowledge requirements

The code samples that follow will reference various functional programmming abstractions.
Firstly, let's state outright that these concepts are **not scary**, contrary the general misinformation that exists within the programming world.
The abstractions may be unfamiliar to you, which is fair, but this is nothing more than an oppurtunity to learn general programming language theory which is true regardless of the programming language being used :smile:.

Functional programming structures (such as Functor, Applicative and Monad) allow us to be very expressive by clearly restricting what we can and cannot be done with a given structure.
Furthermore, these structures also predefine behaviour that is very useful and enables better composition.
For example, knowing that a given structure has an instance of `Functor` available will immediately inform us that this structure, regardless of what it is, can accpet a transformation function to transform the internal values.

We won't be focusing on this too much and it's not really all that important to use CIlib.
If you do, however, struggle with to follow the examples, please feel free to contact the developers online in order to aid you in understanding and to provide some references to resources that are helpful.
Your questions may also result in updates to this tutorial, which you may also provide yourself by submitting a pull request :tada:.


## Categories of data-structures

In the sections that follow, different data structures will be discussed.
The first few data-structures (`RVar`, `Step` and `StepS`) are the main structures within CIlib and are the building blocks which allow for the expression of algorithms.
Additional data structures follow are important structures in their own right, but are suplimental when compared to the three core data strucutres.
The additional structures are closely related to algorithms, but are concerned with algorithm specifics instead of providing a computational platform.
