---
id: motivation
title: Motivation
hide_title: true
---

# Motivation

Computational intelligence has been an active field of study.
Unfortunately, the tools availble for such study have been less than desirable for a variety of reasons.
The fundamental problem arises due to the algorithms within Computational Intelligence being **highly stochastic**.

The stochastic nature of the algorithms makes the reproducibility of the algorithm results difficult.
Often, research papers are accepted _as is_ because the amount of work to try and verify published results is far too large.
Compounding on this fact, most published research does not make the source code easily available.
Requesting the source code for a specific paper may also not be a productive endeavour: the program code may no longer be in a working state due to [bit rot](https://en.wikipedia.org/wiki/Software_rot).

To try combat such problems, a execution framework evolved to allow interested persons to share programs.
The project was developed as open source software, with several prominant researchers contributing to the project.
This was the initial version of CIlib which allowed users to define algorithms, problems and measurements using an XML based execution tool called the `simulator`.

Over time, however, the implementation of CIlib 1.0 started to be influenced by the `simulator`, essentially dictating the way the program code would fit together.
Once several errors were discovered that were directly related to the use of the `simulator`, it was decided that simplicity should be the main focus and CIlib 1.0 was deprecated.

From the learning obtained from the CIlib 1.0 code base, three explicit requirements would need to be addressed in the new version of the project.
