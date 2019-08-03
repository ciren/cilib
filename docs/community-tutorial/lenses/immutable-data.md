## Immutable data

To understand a bit more of the motivation behind lenses here is an excerpt from the [CILib docs][cilib-docs].

<div class="callout callout-danger">

Immutable data is fantastic because it allows anyone to read the contents
of the data and provides the security that the data will not change. In some
cases, however, we would like to "change" some data value. Using immutable
data means that we cannot change the value within a structure, instead we
need to create an updated view of the data with the changes applied. This
updated view creates new data, where the old data is still present and
unchanged. It's recommended that the reader become familiar with
persistent data structures and how they operate. Due to how persistent
data structures update, by only changing the smallest number of references,
the needed speed and efficiency is achieved.

Scala tries to help with respect to immutable data, by providing a convenience
method on all `case class`es called `copy`. In situations where there is a
nesting of case classes, potentially several levels, the updating of a value
on the lower levels results in a bubbling-up process whereby each previous
layer needs to update the reference to the new data in the lower layer.
Although this is not difficult to do, the result is very verbose and
extremely cumbersome for the user. It would be nice if this "zooming"
update process was abstracted behind a data structure that would hide and
automate the tedious process.

</div>