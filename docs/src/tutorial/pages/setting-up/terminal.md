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