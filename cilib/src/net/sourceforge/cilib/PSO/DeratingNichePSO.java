package net.sourceforge.cilib.PSO;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import net.sourceforge.cilib.Algorithm.Algorithm;
import net.sourceforge.cilib.Algorithm.OptimisationAlgorithm;
import net.sourceforge.cilib.Problem.DeratingFunctionMaximisationProblem;
import net.sourceforge.cilib.Problem.FunctionMaximisationProblem;
import net.sourceforge.cilib.Problem.OptimisationProblem;
import net.sourceforge.cilib.Problem.OptimisationSolution;
import net.sourceforge.cilib.Util.DistanceMeasure;
import net.sourceforge.cilib.Util.EuclideanDistanceMeasure;


/**
 * <p>Title: DeratingNichePSO</p>
 * <p>Description: CILib (Computational Intelligence Library)</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Clive Naicker
 * @version 1.0
 */

public class DeratingNichePSO extends Algorithm implements OptimisationAlgorithm
{
  /**
   * This is the NichePSO that is used to find multiple solutions
   */
  private NichePSO nichePSO = null;
  /**
   * This is the vector of solutions that is found by the algorithm.
   */
  private Vector<OptimisationSolution> solutions = new Vector<OptimisationSolution>();
  /**
   * This is the problem that is solved by this algorithm
   */
  private DeratingFunctionMaximisationProblem problem = null;
  /**
   * This is the convergenceThreshold that is used to determine if the best
   * particle of the main swarm has converged to a solution.  The convergence
   * threshold is compared to the particles standard deviation.  It is
   * assumed that the each particle in the NichePSO has a DeviatioDecorator.
   */
  private double convergenceThreshold = 0.0001;
  /**
   * A Factory for creating NichePSO instances with the same parameter settings.
   */
  private NichePSOFactory nichePSOFactory = null;
  /**
   * This field is used to count the number of fitness evaluations.  The number
   * of evaluations includes all the evaluations from the NichePSO instances
   * plus this instance.
   */
  private int fitnessEvaluations = 0;
  /**
   * This convergence threshold is used to determine if a NichePSO has
   * converged to solution.  In particular, if the deviation position of the best
   * particle of the NichePSO is less than this threshold the the NichePSO
   * is considered to have converged.
   */
  private double convergence2Threshold = 0.000001;
  /**
   * This is the maximum number iterations that the NichePSO will run when
   * using the Derated Fitness Function or the Raw Fitness Function
   */
  private int maximumIterations = 500;
  /**
   * This is used to calculate the Euclidean distance between any two particles
   */
  private DistanceMeasure distanceMeasure = new EuclideanDistanceMeasure();

  /**
   * The default constructor.  Initialises the the nichePSO member to a new NichePSO
   * with default settings. Initialises the nichePSOFactory.
   */
  public DeratingNichePSO()
  {
    nichePSO = new NichePSO();
    nichePSOFactory = new NichePSOFactory(nichePSO);
  }

  /**
   * Implemenation of the abstract method inherited from the Algorithm base class.
   */
  protected void performIteration()
  {
    // create a new NichePSO
    try
    {
      nichePSO = nichePSOFactory.newNichePSO();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
      System.err.println("Could not create new NichePSO.");
      System.err.println("Cannot continue...");

      throw new UnsupportedOperationException("Could not create new NichePSO");
    }

    // ensure that the nichePSO has set its optimisation problem.
    nichePSO.setOptimisationProblem(problem);

    // ensure that the nichePSO is initialised.
    nichePSO.initialise();

    int iteration = 0;
    // perform an iteration on the nichePSO until all the convergence criteria are met.
    while (!convergence() && iteration < maximumIterations)
    {
      // perform the NichePSO iteration
      nichePSO.performIteration();
      iteration++;
    }

    // get the solutions found in the nichePSO.
    Vector subswarms = nichePSO.getSubswarms();
    Iterator iterator = subswarms.iterator();
    while (iterator.hasNext())
    {
      PSO pso = (PSO)iterator.next();

      // for each particle in the subswarm reset the decorator for second part of convergence
      Iterator particlesIterator = pso.getTopology().particles();
      while (particlesIterator.hasNext())
      {
        Particle p = (Particle)particlesIterator.next();
        DeviationDecorator decorator = DeviationDecorator.extract(p);
        decorator.reset();
      }
    }

    // for each particle in the main swarm reset the particles deviation for
    // the second part of the convergence
    Iterator particlesIterator = nichePSO.getMainSwarm().getTopology().particles();
    while (particlesIterator.hasNext())
    {
      Particle p = (Particle)particlesIterator.next();
      DeviationDecorator decorator = DeviationDecorator.extract(p);
      decorator.reset();
    }

    // create a new problem without the function modifications
    FunctionMaximisationProblem newProblem = new FunctionMaximisationProblem();
    newProblem.setFunction(problem.getFunction());
    nichePSO.setOptimisationProblem(newProblem);

    // optimise the new niche pso
    iteration = 0;
    while (!convergence2() && iteration < maximumIterations)
    {
      nichePSO.performIteration();
      iteration++;
    }
    // fitnessEvaluations += nichePSO.getFitnessEvaluations();


    iterator = nichePSO.getSolutions().iterator();
    while (iterator.hasNext())
    {
      OptimisationSolution solution1 = (OptimisationSolution)iterator.next();
      solutions.add(solution1);
    }

    for (int i=0; i<solutions.size()-1; i++)
    {
      OptimisationSolution solution1 = (OptimisationSolution)solutions.get(i);

      for (int j=i+1; j<solutions.size(); j++)
      {
        OptimisationSolution solution2 = (OptimisationSolution)solutions.get(j);

        double distance = distanceMeasure.distance((double[])solution1.getPosition(),
                                                   (double[])solution2.getPosition());

        if (nichePSO.normalise(distance) < nichePSO.getMergeThreshold())
        {
          // the solution are similiar
          if (solution1.getFitness().compareTo(solution2.getFitness()) > 0)
          {
            // solution1 is better, so remove solution2 and add solution1
            solutions.remove(j);
            j--;
          }
          else
          {
            solutions.remove(i);
            i--;
            j = solutions.size();
          }
        }
      }
    }

    problem.clear();
    for (int i=0; i<solutions.size(); i++)
    {
      OptimisationSolution solution1 = (OptimisationSolution)solutions.get(i);
      problem.addSolution((double[])solution1.getPosition());
    }
    // System.out.println("solutions = " + solutions.size());
  }

  /**
   * This method is used to determine if the nichePSO has converged.  The nichePSO has
   * converged when the best particle of each subswarms has a positional standard deviation
   * less than the convergence threshold and there are no particles in the MainSwarm.
   * @return true is returned if the convergence criteria is met otherwise false.
   */
  private boolean convergence()
  {
    Vector subswarms = nichePSO.getSubswarms();
    Iterator iterator = subswarms.iterator();
    while (iterator.hasNext())
    {
      // get the PSO
      PSO pso = (PSO)iterator.next();

      // get the deviation from the best particle
      double deviation = DeviationDecorator.extract(pso.getBestParticle()).getPositionDeviation();
      if (deviation > convergenceThreshold)
      {
        // convergence is not satisfied.
        return false;
      }
    }
    if (nichePSO.getMainSwarm().getTopology().particles().hasNext())
    {
      // not all particles in the MainSwarm have converged to a solution.
      return false;
    }
    // otherwise convergence is satisfied.
    return true;
  }

  /**
   * This method is used to determine if the nichePSO has converged.  The nichePSO
   * has converged when the best particle of each subswarms has a positional standard
   * deviation less than the convergence threshold and there are no particles in the
   * MainSwarm.
   * @return true is returned if the convergence criteria is met otherwise false.
   */
  private boolean convergence2()
  {
    Vector subswarms = nichePSO.getSubswarms();
    Iterator iterator = subswarms.iterator();
    while (iterator.hasNext())
    {
      // get the PSO
      PSO pso = (PSO)iterator.next();

      // get the deviation from the best particle
      double deviation = DeviationDecorator.extract(pso.getBestParticle()).getPositionDeviation();
      if (!Double.isNaN(deviation) && deviation > convergence2Threshold)
      {
        // convergence is not satisfied.
        return false;
      }
    }
    if (nichePSO.getMainSwarm().getTopology().particles().hasNext())
    {
      // not all particles in the MainSwarm have converged to a solution.
      return false;
    }
    // otherwise convergence is satisfied.
    return true;
  }

  /**
   * Accessor method for the member nichePSO
   * @param nichePSO The new nichePSO
   */
  public void setNichePSO(NichePSO nichePSO)
  {
    this.nichePSO = nichePSO;
    nichePSOFactory = new NichePSOFactory(nichePSO);
  }

  /**
   * Accessor method for the member nichePSO
   * @return The nichePSO member
   */
  public NichePSO getNichePSO()
  {
    return nichePSO;
  }

  /**
   * Accessor method for the member problem
   * @param problem The new problem
   */
  public void setProblem(DeratingFunctionMaximisationProblem problem)
  {
    this.problem = problem;
  }

  /**
   * Accessor method for the member problem
   * @return The problem member
   */
  public DeratingFunctionMaximisationProblem getProblem()
  {
    return problem;
  }

  /**
   * Accessor method for the member convergenceThreshold
   * @param convergenceThreshold The new convergenceThreshold
   */
  public void setConvergenceThreshold(double convergenceThreshold)
  {
    this.convergenceThreshold = convergenceThreshold;
  }

  /**
   * Accessor method for the member convergenceThreshold
   * @return The convergenceThreshold member.
   */
  public double getConvergence2Threshold()
  {
    return convergence2Threshold;
  }

  /**
   * Accessor method for the member convergenceThreshold
   * @param convergenceThreshold The new convergenceThreshold
   */
  public void setConvergence2Threshold(double convergence2Threshold)
  {
    this.convergence2Threshold = convergence2Threshold;
  }

  /**
   * Accessor method for the member convergenceThreshold
   * @return The convergenceThreshold member.
   */
  public double getConvergenceThreshold()
  {
    return convergenceThreshold;
  }

  /**
   * Sets the optimisation problem to be solved.
   *
   * @param problem The {@link net.sourceforge.cilib.Problem.OptimisationProblemAdapter} to be solved.
   */
  public void setOptimisationProblem(OptimisationProblem problem)
  {
    if (problem instanceof DeratingFunctionMaximisationProblem)
    {
      this.problem = (DeratingFunctionMaximisationProblem)problem;
    }
    else
    {
      throw new IllegalArgumentException("Must use " + DeratingFunctionMaximisationProblem.class +
                                         "or a subclass of it.");
    }
  }

  /**
   * Accessor for the optimisation problem to be solved.
   *
   * @return The {@link net.sourceforge.cilib.Problem.OptimisationProblemAdapter} to be solved.
   */
  public OptimisationProblem getOptimisationProblem()
  {
    return problem;
  }

  /**
   * Returns the best solution found by the algorithm so far.
   *
   * @return A double array of length {@link net.sourceforge.cilib.Problem.OptimisationProblemAdapter#getDimension()} that represents the best solution found.
   */
  public OptimisationSolution getBestSolution()
  {
    if (solutions.isEmpty())
    {
      return nichePSO.getBestSolution();
    }
    else
    {
      // find the best solution.
      Iterator iterator = solutions.iterator();

      // assume the best solution is the first solution
      OptimisationSolution bestSolution = (OptimisationSolution)iterator.next();
      while (iterator.hasNext())
      {
        // compare the current best solution with the next solution.
        OptimisationSolution solution = (OptimisationSolution)iterator.next();
        if (solution.getFitness().compareTo(solution.getFitness()) > 0)
        {
          // update the best solution.
          bestSolution = solution;
        }
      }
      // return the best solution
      return bestSolution;
    }
  }

  /**
   * Gets the solutions currently available from this algorithm
   * @return The solution currently available
   */
  public Collection<OptimisationSolution> getSolutions()
  {
    if (solutions.isEmpty())
    {
      return nichePSO.getSolutions();
    }
    else
    {
      // System.out.println("solutions = " + solutions.size());
      return solutions;
    }
  }

  /**
   * Returns the number of times that the fitness function, specified by {@link net.sourceforge.cilib.Problem.OptimisationProblemAdapter#getFitness(double[])}, has been evaluated.
   * @deprecated This method is no longer in use
   * @return The number of fitness evaluations.
   */
  public int getFitnessEvaluations()
  {
    return fitnessEvaluations;
  }

  /**
   * <p>Title: NichePSOFactory</p>
   * <p>Description: CILib (Computational Intelligence Library)</p>
   * <p>Copyright: Copyright (c) 2004</p>
   * <p>Company: </p>
   * @author Clive Naicker
   * @version 1.0
   */
  protected class NichePSOFactory
  {
    private int mainSwarmSize = 0;
    private int observations = 0;
    private double mergeThreshold = 0;
    private double solutionThreshold = 0;
    private Class mainSwarmClass = null;
    private Class mainSwarmTopologyClass = null;

    public NichePSOFactory(NichePSO nichePSO)
    {
      int size = 0;
      mainSwarmSize = nichePSO.getMainSwarm().getParticles();
      observations = nichePSO.getObservations();
      mergeThreshold = nichePSO.getMergeThreshold();
      solutionThreshold = nichePSO.getSolutionThreshold();
      mainSwarmClass = nichePSO.getMainSwarm().getClass();
      mainSwarmTopologyClass = nichePSO.getMainSwarm().getTopology().getClass();
    }

    /**
     * Creates a new NichePSO that is uninitialised.
     * @return A new NichePSO.
     * @throws Exception
     */
    public NichePSO newNichePSO() throws Exception
    {
      try
      {
        NichePSO nichePSO = new NichePSO();
        try
        {
          nichePSO.setMainSwarm((PSO)mainSwarmClass.newInstance());
        }
        catch(IllegalAccessException ex)
        {
          ex.printStackTrace();
          throw new Exception("Could not set MainSwarm for the NichePSO.");
        }
        catch(InstantiationException ex)
        {
          ex.printStackTrace();
          throw new Exception("Could not set MainSwarm for the NichePSO");
        }
        nichePSO.getMainSwarm().setParticles(mainSwarmSize);
        try
        {
          nichePSO.getMainSwarm().setTopology((Topology)mainSwarmTopologyClass.newInstance());
        }
        catch(IllegalAccessException ex)
        {
          ex.printStackTrace();
          throw new Exception("Could not set Topology for the MainSwarm.");
        }
        catch(InstantiationException ex)
        {
          ex.printStackTrace();
          throw new Exception("Could not set Topology for the MainSwarm.");
        }
        nichePSO.setMergeThreshold(mergeThreshold);
        nichePSO.setSolutionThreshold(solutionThreshold);

        PSO mainSwarm = nichePSO.getMainSwarm();

        StandardVelocityUpdate standardVelocityUpdate = new StandardVelocityUpdate();
        ZeroVelocityComponent zeroAcceleration = new ZeroVelocityComponent();
        StandardAcceleration standardAcceleration = new StandardAcceleration();
        StandardInertia standardInertia = new StandardInertia();

        standardAcceleration.setAcceleration(1.2);
        standardInertia.setInertia(0.7);

        standardVelocityUpdate.setSocialComponent(zeroAcceleration);
        standardVelocityUpdate.setCognitiveComponent(standardAcceleration);
        standardVelocityUpdate.setInertiaComponent(standardInertia);
        standardVelocityUpdate.setMaximumVelocity(0.5);

        mainSwarm.setVelocityUpdate(standardVelocityUpdate);

        // the main swarms particles need to be able to calculate the standard
        // deviation of their fitness.
        mainSwarm.setPrototypeParticle(new DeviationDecorator(new StandardParticle(), observations));

        return nichePSO;
      }
      catch(Exception ex)
      {
        throw ex;
      }
    }
  }
}