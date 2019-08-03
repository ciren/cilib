# Lenses

Great! You just learnt about creating positions in a search space.
But you might be asking yourself "How can we modify our set positions to explore the search space?"
And this is exactly what we intend to learn in the coming chapter.
Well, partly.
This chapter is going to focus on `Lenses`, which are super awesome getters, setters and more!
CILib makes use of the two optics from the [`monocle`][Monocle-link] library.

- [`Lens`][lens-link]
- [`Prism`][prism-link]

I deeply encourage you to check out [`monocle`][Monocle-link] if you are not familiar with any of the concepts I just mentioned.
The use of these optics is beneficial as they allow us to create a new *mutated* instance while still preserving the original.
Or they may shed light on an instance by *zooming* in and getting/returning data.

<div class="callout callout-danger">

Lenses provide an API that is first and foremost, composition and lawful.
This means that the various optics are well behaved and rules exist that
govern their usage. Furthermore, different optics may be composed together
to create new optics that are the combination of the original optics. This
is obviously only possible if the provided types correctly line up.

Building on the usage of optics in general, we use a mechanism known as
"classy lens" in Haskell. This mechanism prevents invalid usage, by letting
the compiler fail based on the types being used. In the case of ``Entity``,
the compiler would look up instances, using its implicit resolution rules,
to obtain evidence for a typeclass with a given set of types, at compile time.

This provides an additional level of surety, that the data being passed to
a function that requires evidence in order to extract some other piece of
information for a given type. The scala compiler provides the evidence
through the use of its implicit lookup mechanics.

</div>

Now that you understand the motivation for `Lenses` we can start to look at what CILib offers us.
If you are still a bit unclear about optics then hopefully the following sections will clear that up as we go through some examples.
Just one last thing.
You might have picked up that [Gary][link-gary] made reference to something called `Entity`.
Fear not as this will be explained more in detail soon in this chapter and the next chapter, which is all about `Entity`.
