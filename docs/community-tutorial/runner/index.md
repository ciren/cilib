# Runner and Our First Algorithm

In this chapter we are going to create our very own Genetic Algorithm.
But there are a few things we are going to have to discuss first before we jump into the creating a GA.

CILib offers a data type that we may use to run our algorithms.
I say "may" because, if so choose, you create your own way of running the algorithms.
This data type is called `Runner`.
`Runner` isn't a part of the cilib-core, but is included in this part of the book because it is needed to demonstrate everything we have learnt.
In order to include `Runner` to your project you need to add `cilib-exec` as a dependency in your `build.sbt`.

And since we are creating a GA we should also include `cilib-ga`.
The reason we are creating a GA rather than, let's say,
a PSO is that the ga package uses types we have seen before where as a PSO uses types exclusive to the PSO package (W=we will explore these later).

Your library dependencies should look very similar to the following...

```
libraryDependencies ++= Seq(
    "net.cilib" %% "cilib-core" % "@CILIB_VERSION@",
    "net.cilib" %% "cilib-ga" % "@CILIB_VERSION@",
    "net.cilib" %% "cilib-exec" % "@CILIB_VERSION@"
)
```

Once you have refreshed your project we can start looking at what `Runner` is.
