## Testing The Core

You should now be able to use any of the components of `cilib-core`.
Let's give it a try. Open up your Scala Worksheet or the `sbt console` and type the following. 

```tut:book
import cilib._
val doubles = RVar.doubles(4)
```

If you get the same result then everything is working perfectly!
