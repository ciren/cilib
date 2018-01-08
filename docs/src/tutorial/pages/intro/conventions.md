## Conventions Used in This Book {-}

Throughout the book you will see a lot of theory along side practical examples represented through blocks of code.

### Typographical Conventions {-}

- New terms and definitions will be represented in a **bold font**. 
- Phrases will be represented by *itilics*. 

After the initial introduction they will be written in normal font.

- Program code, filenames, and file contents, are written in `monospace font`.
- References to external resources are written as [hyperlinks][link-cilib].
- References to API documentation are written using a combination of hyperlinks and monospace font, for example: [`scala.Option`][scala.Option].

Note that we do not distinguish between singular and plural forms.
For example, might write `String` or `Strings` to refer to `java.util.String`.

### Source Code {-}

You will come across two different types of code blocks. The first being a standard code block as follows.

```tut:book:silent
val number = 21 // My number
println(s"My favorite number is $number") // My message
```
The second type of code block uses [tut][link-tut] to ensure it compiles. You can see the result of the compilation aas a comment. 

```tut:book
"cilib".toUpperCase
```

### Callout Boxes {-}

We use two types of *callout box* to highlight particular content:

<div class="callout callout-info">
Tip callouts indicate handy summaries, recipes, or best practices.
</div>

<div class="callout callout-warning">
Advanced callouts provide additional information
on corner cases or underlying mechanisms.
</div>

<div class="callout callout-danger">
Information and insight provided from CILib docs project, written by [Gary][link-gary].
</div>
