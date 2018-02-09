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

