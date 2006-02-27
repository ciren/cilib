********************************************************

WARNING WARNING WARNING WARNING WARNING WARNING!!!

This README is a bit out of date - some effort has been made to
bring it (at least the code examples) in line with CILib 0.3 but a 
complete re-write is overdue.

Consult the cilib-users@lists.sourceforge.net mailing list if you have
any questions or need more up to date information about CILib.

Ingnore all the JAXP stuff, it is now included in the JFC (Java Foundation Classes)

********************************************************

CILib - Computational Intelligence Library (http://clib.sourceforge.net)
---------------------------------------------------------------------------------
Copyright (C) 2003,  2004 - CIRG@UP
Computational Intelligence Research Group (CIRG@UP)
Department of Computer Science
University of Pretoria
South Africa

CILib is a framework for implementing various computational intelligence 
algorithms. This version includes implementations for many particle swarm
optimisers, however, the framework is intended to be generic enough to
implement other computational intelligence algorithms such as neural networks 
(which can be used to solve classification and clustering problems), 
evolutionary algorithms (such as genetic and cultural evolutionary algorithms),
AIS (artificial immune systems) and in fact any algorithm falling within the  
computational intelligence paradigm.

In addition, CILib includes a simulator that can be used to execute and measure
the performance of any of the algorithms. The simulator is included primarily 
for academic purposes. All the algorithms are independent of the simulation 
engine so that they can be used in real world applications. 

CILib is released under the terms of version two of the GNU General Public 
License (GPL). A copy of the GPL has been included in the file COPYING. Please
make sure that you agree to the terms of the license before making use of any
code provided in the library. If you contribute code to this library please
append your name to the AUTHORS file.

Installation Instructions
-------------------------

CILib comes packaged in both source and binary forms. If you have obtained the 
sources then it is assumed that you are a developer and that you know what 
to do with it. The binary distribution takes the form of a JAR (Java Archive) 
file. 

To make use of the algorithms, include the JAR file in your CLASSPATH. If you
intend to make use of the simulator or the XMLObjectFactory then you also need 
to include the following JAR files from JAXP (Java API for XML Processing) 
in your CLASSPATH: (JAXP is available from http://java.sun.com)

dom.jar
jaxp-api.jar
sax.jar
xercesImpl.jar

This is because the simulator reads its configuration data from an XML file. The
XML APIs are not necessary if you do not intend to make use of the simulator. If
you intend to make use of the algorithms within your own code (hence you are a
developer) then you can still make use of the binary distribution in the same
way (or get the sources).

The CILib Simulator
-------------------

The simulation engine classes reside in the net.sourceforge.cilib.Simulator
package. The simulator accepts a single command line argument which is the XML
configuration file. Example:

# java -server net.sourceforge.cilib.Simulator.Simulator example.xml

The following is the general skeleton for the config file:

<?xml version="1.0" encoding="UTF-8"?>

<!DOCTYPE simulator [
  <!ATTLIST algorithm id ID #IMPLIED>
  <!ATTLIST problem id ID #IMPLIED>
  <!ATTLIST measurements id ID #IMPLIED>
]>

<simulator>
    <algorithms>
        <algorithm id="algorithm1" class="..." />
        <algorithm id="algorithm2" class="..." />
        ...
        <algorithm id="algorithmN" class="..." />
    </algorithms>
    
    <problems>
        <problem id="problem1" class="..." />
        <problem id="problem2" class="..." />
        ... 
        <problem id="problemN" class="..." />
    </problems>

    <measurements id="list" class="Simulator.MeasurementSuite" samples="10" resolution="1000">
        <addMeasurement class="..." />
        <addMeasurement class="..." />
    </measurements>
    
    <simulations>
        <simulation>
            <algorithm idref="algorithm1" />
            <problem idref="problem1" />
            <measurements idref="list" file="output1.txt" />
        </simulation>
        <simulation>
            <algorithm idref="algorithm1" />
            <problem idref="problem2" />
            <measurements idref="list" file="output2.txt" />
        </simulation>
        ...        
        <simulation>
            <algorithm idref="algorithmN" />
            <problem idref="problemN" />
            <measurements idref="list" file="outputN.txt" />
        </simulation>        
    </simulations>
</simulator>

The simulator executes each simulation defined within the <simulations> element
in turn. Each simulation consists of an algorithm, a problem and a measurement 
suite. 

The various algorithms all inherit from Algorithm in the 
net.sourceforge.cilib.Algorithm package. Some generic algorithms (such as
the CoOperativeOptimisationAlgorithm and the MultistartOptimisationAlgorithm) 
are also included in this package. The particle swarm algorithms are included
in the net.sourceforge.cilib.PSO package. 

All the different problems that can be applied to any CILib algorithm are 
included in the net.sourceforge.cilib.Problem package. Currently, the only 
problem interface that exists is OptimisationProblem (particle swarms find 
solutions to optimisation problems). The FunctionMinimisationProblem class 
implements OptimisationProblem and can be used to find the minimum of any of 
the functions in the net.sourceforge.cilib.Functions package. The simulator is 
clever enough to figure out if a particular problem can be solved by a 
particular algorithm. 

Finally, and most importantly for the simulator, the measurement suite logs 
the performance of the algorithm to file. The measurements are all contained in
the net.sourceforge.cilib.Measurement package. The MeasurementSuite has three
properties of interest: The resolution is determines how often measurements are
logged to file, in the above example, measurements are loged every 1000 
iterations. The number of samples is the number of times the simulation is 
repeated and re-measured. Finally, the file property, which is overridden 
for each simulation, specifies the file to log the measurements to.

Note that the problems, algorithms and measurements can be included inline 
instead of making use of references. By using references you can easily re-use 
the measurement suite, execute the same algorithm on different problems and 
vise versa. The only catch is the XML DOM (Document Object Model) needs to 
know which attributes are ID attributes, hence the DOCTYPE definition. It is 
not easy to enforce a strict XML schema for the document because the tags used 
are dependent on the alogrithm, problem and measurement classes and the methods 
and properties that they expose.

The algorithms, problems and measurements elements are all passed to the 
XMLObjectFactory class which is responsible for instanciating algorithms, 
problems and measurements. The XMLObjectFactory allows you to configure every 
aspect of these classes by setting properties and calling arbitrary methods. 
All class names are relative to the net.sourceforge.cilib package. Two sample 
config files have been included. The first, ngcpso.xml tests the performance
of six different configurations of the GCPSO and PSO with different 
neighbourhood topologies on seven different function minimisation problems. 
The second, mcpso.xml is a more complicated example that wraps a standard
PSO within a co-operative algorithm which in turn is wrapped in a multi start
algorithm. For futher XMLObjectFactory samples refer to the XMLObjectFactory 
demo at http://www.sourceforge.net/projects/cilib/

Under unix, it is a good idea to nice the simulator process since the simulator
creates a new thread for each sample. You may also need to increase the 
maximum available VM memory using the -Xmx switch if all the threads cannot
fit into memory at the same time. Future versions will include a max threads 
switch to alleviate this issue. Under windows, well... I don't use windows and 
neither should you. :-)

Using an Algorithm in your own code
-----------------------------------

The basic proceedure is outlined as follows:

1. Implement one of the Problem interfaces in your code.
2. Instanciate an Algorithm.
3. Set the problem.
4. Add stopping conditions 
5. Setup any other properties of the algorithm.
6. Add any necessary event listeners to the algorithm.
7. Call the initialise() method on the algorithm.
8. Start the algorithm.

Algorithm implements the Runnable interface so you can have it execute in a separate 
thread. Alternatively, call the run() method directly if you do not want the algorithm to run 
concurrently with your code. 

All classes must provide a default constructor (this is a requirement of 
XMLObjectFactory) so sensible defaults have been provided for most properties. 

In the case of the PSO:

1. Implement the OptimisationProblem interface:

        class MyProblem implements OptimisationProblem {
            public double getFitness(double[] solution) {
                // return the fitness of the given potential solution.
                // higher fitness values indicate a better solution to the problem.
            }
         
            public Domain getDomain(int component) {
                // return the search bounds for the given dimension of the problem.
            }
    
            public int getDimension();
                // return the dimension of the problem.
            }
        }

2. Construct a PSO (in this case a GCPSO): 

        PSO pso = new GCPSO();

3. Set the problem:
  
        MyProblem problem = new MyProblem();
        pso.setOptimisationProblem(problem);

4. Add stopping conditions (this stops after 10000 iterations):

        MaximumIterations maxIter = new maximumIterations(10000);
        pso.addStoppingCondition(maxIter);

5. Set any other properties (for this example, we would like to make use of the 
   Von Neumann Topology with 40 particles, as well as use the ZiffGFSR4 random 
   number generator for rho updates):

        pso.setTopology(new VonNeumannTopology());
        pso.setParticles(40);

        GCVelocityUpdate vu = new GCVelocityUpdate();
        vu.setRhoRandomNumberGenerator(new ZiffGFSR4());
        pso.setVelocityUpdate(vu);

   Alternatively, you can make use of the XMLObjectFactory to setup properties
   in the same way that the simulator does.

6. Add event listeners:

        pso.addIterationEventListener(this);
        // The assumption is that this class implements IterationEventListener

7. Call initialise():

        pso.initialise();

8. Start the algorithm:

        pso.run(); // Executes in current thread

   OR

        new Thread(pso).start(); // Executes in a new thread


To get feedback from the algorithm, you can make use of the measurements in 
net.sourceforge.cilib.Measurement or you can query the algorithm directly:

        pso.getBestSolution(); // will return the best solution found so far.



        