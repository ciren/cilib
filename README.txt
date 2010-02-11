---------------------------------------------------------------------------------
CIlib - Computational Intelligence Library (http://www.cilib.net)
---------------------------------------------------------------------------------
Copyright (C) 2003,  2010 - CIRG@UP
Computational Intelligence Research Group (CIRG@UP)
Department of Computer Science
University of Pretoria
South Africa

CIlib is a framework for implementing various computational intelligence
algorithms. The goal of the project is to create a library that can be used
and referenced by individuals.

CIlib is released under the terms of version two of the GNU General Public
License (GPL). A copy of the GPL has been included in the file COPYING. Please
make sure that you agree to the terms of the license before making use of any
code provided in the library.

-------------------------
Contact information
-------------------------
CIlib has a busy community, and we encourage you to join us. The developers and
users may be contacted using either the project site / user forums or the
developer mailing list.

    http://www.cilib.net                        (Web site)
    http://code.google.com/p/cilib              (Project site)
    http://forums.cilib.net                     (User forums)
    http://groups.google.com/group/cilib-dev    (Developer mailing list)


-------------------------
Installation Instructions
-------------------------

CIlib comes packaged in both source and binary forms. If you have obtained the
sources then it is assumed that you are a developer and that you know what
to do with it. The binary distribution takes the form of a JAR (Java Archive)
file.

For downloads, please consult http://code.google.com/p/cilib/downloads/list

-------------------
The CIlib Simulator
-------------------

Bundled within the CIlib jar, is a simulator application that allows the user to
define the structure of a simulation using XML tags.

An example of an incomplete XML configuration file is provided:

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
suite. A clear separation is provided between the algorithm, problem and
measurements.

The various algorithms all inherit from Algorithm in the
net.sourceforge.cilib.Algorithm package. Currently, support is provided for
PSO, ECs (including GA, DE, EP, etc), ABC, NNs (and the list continues as
more and more algorithms are added).

All the different problems that can be applied to any CIlib algorithm are
included in the net.sourceforge.cilib.Problem package. Currently, the only
problem interface that exists is OptimisationProblem (particle swarms find
solutions to optimization problems). The FunctionMinimisationProblem class
implements OptimisationProblem and can be used to find the minimum of any of
the functions in the net.sourceforge.cilib.Functions package.

Finally, and most importantly for the simulator, the measurement suite logs
the performance of the algorithm. The MeasurementSuite has three
properties of interest:

    - The resolution is determines how often measurements are
      logged to file, in the above example, measurements are lagged every 1000
      iterations.
    - The number of samples is the number of times the simulation is
      repeated and re-measured.
    - Finally, the file property, which is overridden for each simulation,
      specifies the file to log the measurements to.

Note that the problems, algorithms and measurements can be included inline
instead of making use of references. By using references you can easily re-use
the measurement suite, execute the same algorithm on different problems and
vise versa. The only catch is the XML DOM (Document Object Model) needs to
know which attributes are ID attributes, hence the DOCTYPE definition. It is
not easy to enforce a strict XML schema for the document because the tags used
are dependent on the algorithm, problem and measurement classes and the methods
and properties that they expose.

Samples of XML configuration of the simulator are provided in the 'xml'
sub-directory of the CIlib package.

-----------------------------------
Using an Algorithm in your own code
-----------------------------------

The basic procedure is outlined as follows:

1. Implement one of the Problem interfaces in your code.
2. Instantiate an Algorithm.
3. Set the problem.
4. Add stopping conditions
5. Setup any other properties of the algorithm.
6. Add any necessary event listeners to the algorithm.
7. Call the initialise() method on the algorithm.
8. Start the algorithm.

Algorithm implements the Runnable interface so you can have it execute in a separate
thread. Alternatively, call the run() method directly if you do not want the algorithm to run
concurrently with your code.

All classes must provide a default constructor (this is a current requirement
that is being debated). Currently, sensible defaults have been provided for most properties.

In the case of the PSO:

1. Use a currently provided problem instance, or implement the
   OptimisationProblem interface:

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

2. Construct a PSO:

        PSO pso = new PSO();

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

        // We will really be using the GCPSO algorithm
        GCVelocityUpdate vu = new GCVelocityUpdate();
        vu.setRhoRandomNumberGenerator(new ZiffGFSR4());
        pso.setVelocityUpdate(vu);

   Alternatively, you can make use of the XMLObjectFactory to setup properties
   in the same way that the simulator does.

6. Add event listeners, if you would like to be notified of progress information:

        pso.addIterationEventListener(this);
        // The assumption is that this class implements IterationEventListener

7. Call initialise() on the algorithm to ensure that the algorithm is ready for
   execution.

        pso.initialise();

8. Start the algorithm:

        pso.run(); // Executes in current thread

   OR

        new Thread(pso).start(); // Executes in a new thread


To get feedback from the algorithm, you can make use of the provided or
self implemented measurements. Alternatively, you can query the algorithm directly:

        pso.getBestSolution(); // will return the best solution found so far.
