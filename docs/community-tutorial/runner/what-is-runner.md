## What is Runner?

`Runner` is an object within CILib that allows us to control the execution of our algorithm.
It provides us with methods that allow us to specify the:

- Number of iterations the algorithm should run for.
- The algorithm.
- The entities in the search space search space.

Pretty cool, hey?
These are the discussed methods.

```scala
repeat[F[_],A,B](n: Int, alg: Kleisli[Step[A,?],F[B],F[B]], collection: RVar[F[B]]): Step[A,F[B]]

repeatS[F[_],A,S,B](n: Int, alg: Kleisli[StepS[A,S,?],F[B],F[B]], collection: RVar[F[B]]): StepS[A,S,F[B]]
```

"Woah, that looks intimidating".
Sure, it definitely can appear that way so let's try to clarify it a bit.
The difference between the algorithms is simply that the one is dealing with `Step` and the other `StepS`.
So you would choose which repeat method based on the type you used in your algorithm.
As for the parameters, `n: Int` is the number of iterations the algorithm should run for.
And `collection: RVar[F[B]]` are our *entities* in our search space.
Now the second parameter is something we haven't come across yet.
All it is saying that we need an iteration type based on the algorithm.

### Iteration

`Iteration` is an object defined within `cilib-core` that will create an *iterator* of our algorithm.
An *iterator* simply means are we running our algorithm synchronously or Asynchronously.

```scala
sync_[M[_]:Applicative,A,B](f: NonEmptyList[A] => A => M[B]): Kleisli[M,NonEmptyList[A],NonEmptyList[B]]

async_[M[_]: Monad,A](f: NonEmptyList[A] => A => M[A]): Kleisli[M,NonEmptyList[A],NonEmptyList[A]
```

These methods are very *raw* and generic.
They are made public so that if you ever wanted to perhaps make your own iterator you could make use them if you desired so.
The following methods use the *raw* and generic methods to create something we are a bit more familiar with.

```scala
def sync[A,B,C](f: NonEmptyList[B] => B => Step[A,C]) = sync_[Step[A,?],B,C](f)

def syncS[A,S,B,C](f: NonEmptyList[B] => B => StepS[A,S,C]) = sync_[StepS[A,S,?], B,C](f)

def async[A,B](f: NonEmptyList[B] => B => Step[A,B]) = async_[Step[A,?], B](f)

def asyncS[A,S,B](f: NonEmptyList[B] => B => StepS[A,S,B]) = async_[StepS[A,S,?], B](f)
```

As we saw with runner we are given methods to handle both `Step` and `StepS` based algorithms.

### Conclusion

This may be a lot to take in.
If so don't worry as we are now going to create a `GA` from start to finish!
