# Contribution guide

CIlib is an open-source project, and it depends its users to continually improve.
We are very pleased that you would like to help!

## Submitting issues

If you notice a bug, have an idea for a feature, or have a question about the
code, please don't hesitate to [create a GitHub issue]. Many features come
about simply by the asking of a question, or requesting a feature directly.

## Submitting a pull request

### Claiming an issue

If there is already an issue on GitHub for a task you are working on, it would be
helpful for you to mention this in the issue, so that others could get informed
of your efforts when they read the issue or get a notification.

If there isn't already an issue for a task (non-trivial or not), it's a good
idea to create one and then mention that you are working on it. Hopefully, this
will prevent duplicate effort from contributors.

### Building the project

It's a good idea to get familiar with the manner in which [GitHub pull requests]
work. The process is not complex and will allow for discussion about any proposed
changes in a centralised manner.

The project uses [sbt] as the build tool, so being familiar with it is
recommended. The normal build targets for the project are the default defined
tasks in sbt.

If you need assistance with the build process, please feel free to ask in the
[Gitter] of the project.

### Testing

- We use ScalaCheck for the test suite (we might consider adding Specs2), and
  favor the definition of laws and invariants for the tests.
  Understandably, there are situations where this is not possible, nor clear,
  so traditional example unit tests would be encouraged also.
- Tests are located in the `tests` module, under `src/test/scala`
- We are not really concerned with arbitrary metrics of the code, but
  a coverage tool might allow for insights into areas of the project that are
  not well tested.

### Contributing documentation

Documentation is often neglected, but is highly appreciated and a good way
to start contributing to CIlib.

[The documentation] is stored alongside the project source, in the
[docs subproject] where you'll find a README describing
how the site is developed. For quick updates, you could use the edit link
directly to update the content within the GitHub interface.

### Targetting a branch

CIlib maintains two branches:

* [master]: The current actively developed version
* [series-1.0.x]: The original Java implementation, no longer developed and deprecated

The guide below helps find the most appropriate branch for your change.

My change is...                               | Branch
----------------------------------------------|-------------------
Documentation of existing features            | [master]
Documentation of unreleased features          | [master]
Change to docs                                | [master]
Binary compatible with current release        | [master]

Still unsure?  Don't worry!  Send us that PR, and we'll cherry-pick it
to the right place.

### Attributions

If your contribution has been derived from or inspired by other work,
please state this in its scaladoc comment and provide proper
attribution. When possible, include the original authors' names and a
link to the original work.

### Grant of license

cilib is licensed under the [Apache-2.0 License]. Opening a pull
request signifies your consent to license your contributions under the
Apache-2.0 License.

## Building the Community

### Join the adopters list

It's easy to [add yourself as an adopter].  You get free advertising
on the [adopters list], and a robust list encourages gives new users
the confidence to try cilib and grows the community for all.

### Spread the word

We watch [Gitter] and [GitHub issues] closely, but it's a bubble.  If
you like cilib, a blog post, meetup presentation, or appreciative
tweet helps reach new people, some of whom go on to contribute and
build a better cilib for you.

### Code of Conduct

Discussion around cilib is currently happening on [Gitter] as
well as [Github issues].  We hope that our community will be
respectful, helpful, and kind.  If you find yourself embroiled in a
situation that becomes heated, or that fails to live up to our
expectations, you should disengage and contact one of the [community
staff] in private. We hope to avoid letting minor aggressions and
misunderstandings escalate into larger problems.

If you are being harassed, please contact one of the [community staff]
immediately so that we can support you.

## Acknowledgements

This document is heavily based on the [Cats contributor's guide].


[create a GitHub issue]: https://github.com/cirg-up/cilib/issues/new
[Github pull requests]: https://help.github.com/articles/about-pull-requests/
[sbt]: http://www.scala-sbt.org/0.13/tutorial/Setup.html
[Gitter]: https://gitter.im/cirg-up/cilib
[The documentation]: https://cirg-up.github.io/cilib
[docs subproject]: https://github.com/cirg-up/cilib/tree/master/docs/
[master]: https://github.com/cirg-up/cilib/tree/master
[series-1.0.x]: https://github.com/cirg-up/cilib/tree/series/1.0.x
[Apache-2.0 License]: https://github.com/cirg-up/cilib/blob/master/LICENSE
[add yourself as an adopter]: https://github.com/cirg-up/cilib/edit/master/docs/src/hugo/content/adopters.md
[adopters list]: https://cirg-up.github.io/cilib/adopters/
[GitHub issues]: https://github.com/cirg-up/cilib/issues
[community staff]: https://cirg-up.github.io/cilib/community/conduct.html#community-staff
[Cats contributor's guide]: https://github.com/typelevel/cats/blob/master/CONTRIBUTING.md
