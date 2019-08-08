# Setting Up Your Work Environment {#sec:setting-up}

This section will take you through setting an sbt project to include CILib.
If you're more of a IDE programmer, don't worry, we've got you covered. We will look at setting up CILib in both an IDE environment as well as a text editor and terminal based environment. After we have our structure set up, we will look at modules from CILib that we will be able to add to our list of library dependencies.

*Note: You need Java in order for Scala to compile so make sure you have a jdk installed.*

## Using an IDE

If you prefer to use a terminal and text editor you can skip ahead to that section.

In the this section, we'll be making use of one of the most popular IDEs, that being [InteliJ][link-intelij]. You can get InteliJ from [here][link-intelij].
It should detect what operating system you are running when you click download. Select the version you wan and install it.

### Installing Scala Plugins

Run InteliJ and you should see something very similar to this.

![Scala IDE](src/pages/setting-up/images/intelij-start.PNG)\


We are now going to create an empty project of no significance.
This is just to allows us to get into the IDE and install Scala.
Go ahead and click `create new project`, `Empty Project`, `Next` and name the project `Test-Setup` or any name of your choice and choose where you would like to store the project.

![Scala IDE2](src/pages/setting-up/images/test-setup.PNG)\


A window should appear called `Project Structure`, you can just cancel out of that.
Proceed to `File`, `Settings`, `Plugins`.
You should now be at a window like this.

![Scala IDE3](src/pages/setting-up/images/setup-plugins.PNG)\


You can now search for `Scala` and install that, as well as `SBT`.
If you don't find any of these you can click `Browse Repositories` and search for them there.

### Your First Scala Project

Once that is done, restart the IDE. Proceed to either `create new project` or `File` -> `New Project` depending on what windows appears.
You should now see the option to choose a new Scala project.
You can then name your project `CILib-Demo`, choose where you would like to store the project and click finish.

![Scala IDE4](src/pages/setting-up/images/new-sbt-project.PNG)\
![Scala IDE5](src/pages/setting-up/images/cilib-demo.PNG)\


### Creating a Scala Worksheet

Lastly, we are going to create a Scala Worksheet. This will mimic the experience one might get using Scala's REPL mode on a terminal.
On the project window, open `src`, `main` and then right click on `scala`, go to `new` and select `Scala Worksheet`.

![Scala IDE5](src/pages/setting-up/images/new-worksheet.PNG)\


I have named my worksheet `demo`. The worksheet allows you to type Scala code on the right hand side displays what the expressions evaluate to.

![Scala IDE5](src/pages/setting-up/images/worksheet-demo.PNG)\


## Using a Terminal and Text Editor

If you have already set up using an IDE then don't worry about this section.
However, It might be interesting to read through it.

### Getting Scala and SBT

This is a relatively simple process.
Head over to the [Scala site][scala-link] and download the latest version for your operating system.
After that, all thats left now is to get [SBT][sbt-link].
If you prefer, you can also use your operating systems' package manger to get Scala and SBT.
For example, on ubuntu you would use `sudo apt-get install scala` and `sudo apt-get install sbt`.

### Creating a New SBT Project

You should now be able to create a new project using SBT.
To do this, open a terminal and go to a location of your choice.
Use the command `sbt new sbt/scala-seed.g8` to generate a template project that we will use.

![Terminal-1](src/pages/setting-up/images/console-sbt-new-project.png)\


As you can see, I created a new a SBT project called `CILib-Demo` and will be stored in a folder called `CILib-Demo`.
You will be able to edit any files from the project by opening in your text editor.
I have opened mine in Visual Code.

![Terminal-1](src/pages/setting-up/images/text-editor.png)\


### Using REPL

Still in the terminal, move into the newly created project folder and type `sbt` and and then once it is ready enter the command `console`.
Alternatively you could type `sbt console` at once.
This allows us to use the Scala REPL. The code entered in the console will yield the result of the evaluated expression. As you can see below.

![Terminal-1](src/pages/setting-up/images/sbt-console.png)\

To allow for multiline statements in REPL, use the `:paste` command.
This can be done by typing `:paste` and pressing enter before entering your multiline statement.


## Summary

Awesome! If you made it this far you know now where you'll be doing most of your coding.
Either using the Scala Worksheet in InteliJ or using REPL in the `sbt console`.
All we need to do now is set up our library dependencies to include CILib.
