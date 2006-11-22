/*
 * DeratingNichePSO.java
 * 
 * Created on Jul 24, 2004
 *
 * Copyright (C) 2004 - CIRG@UP 
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science 
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.pso;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.controlparameterupdatestrategies.RandomisedParameterUpdateStrategy;
import net.sourceforge.cilib.controlparameterupdatestrategies.ConstantUpdateStrategy;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.problem.DeratingFunctionMaximisationProblem;
import net.sourceforge.cilib.problem.FunctionMaximisationProblem;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.pso.particle.DeviationDecorator;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.pso.velocityupdatestrategies.StandardVelocityUpdate;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;


/**
 * <p>Title: DeratingNichePSO</p>
 * <p>Description: CILib (Computational Intelligence Library)</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Clive Naicker
 * @version 1.0
 * @deprecated Will be rewriten with a new design focused on flexibility, 
 *             plugability and useabilty
 */
public class DeratingNichePSO extends Algorithm
{
  /**
   * This is the NichePSO that is used to find multiple solutions
   */
  private NichePSOOld nichePSO = null;
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
    nichePSO = new NichePSOOld();
    nichePSOFactory = new NichePSOFactory(nichePSO);
  }

  /**
   * Implemenation of the abstract method inherited from the Algorithm base class.
   */
  public void performIteration()
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
      //Iterator particlesIterator = pso.getTopology().particles();
      Iterator particlesIterator = pso.getTopology().iterator();
      while (particlesIterator.hasNext())
      {
        Particle p = (Particle)particlesIterator.next();
        DeviationDecorator decorator = DeviationDecorator.extract(p);
        decorator.reset();
      }
    }

    // for each particle in the main swarm reset the particles deviation for
    // the second part of the convergence
    //Iterator particlesIterator = nichePSO.getMainSwarm().getTopology().particles();
    Iterator particlesIterator = nichePSO.getMainSwarm().getTopology().iterator();
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

        //double distance = distanceMeasure.distance((double[])solution1.getPosition(),
        //                                           (double[])solution2.getPosition());
        double distance = distanceMeasure.distance((net.sourceforge.cilib.type.types.Vector)solution1.getPosition(),
                                                   (net.sourceforge.cilib.type.types.Vector)solution2.getPosition());

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
    //if (nichePSO.getMainSwarm().getTopology().particles().hasNext())
    if (nichePSO.getMainSwarm().getTopology().iterator().hasNext())
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
    //if (nichePSO.getMainSwarm().getTopology().particles().hasNext())
    if (nichePSO.getMainSwarm().getTopology().iterator().hasNext())
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
  public void setNichePSO(NichePSOOld nichePSO)
  {
    this.nichePSO = nichePSO;
    nichePSOFactory = new NichePSOFactory(nichePSO);
  }

  /**
   * Accessor method for the member nichePSO
   * @return The nichePSO member
   */
  public NichePSOOld getNichePSO()
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
  public List<OptimisationSolution> getSolutions()
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

    public NichePSOFactory(NichePSOOld nichePSO)
    {
      //int size = 0;
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
    @SuppressWarnings("unchecked")
	public NichePSOOld newNichePSO() throws Exception
    {
      try
      {
        NichePSOOld nichePSO = new NichePSOOld();
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
        	Topology<Particle> mainSwarmTopology = (Topology<Particle>) mainSwarmTopologyClass.newInstance();
        	nichePSO.getMainSwarm().setTopology(mainSwarmTopology);
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
        ConstantUpdateStrategy zeroAcceleration = new ConstantUpdateStrategy();
        RandomisedParameterUpdateStrategy standardAcceleration = new RandomisedParameterUpdateStrategy();
        ConstantUpdateStrategy standardInertia = new ConstantUpdateStrategy();
        ConstantUpdateStrategy vMax = new ConstantUpdateStrategy();

        zeroAcceleration.setParameter(0.0);
        standardAcceleration.setParameter(1.2);
        standardInertia.setParameter(0.7);
        vMax.setParameter(0.5);

        standardVelocityUpdate.setSocialAcceleration(zeroAcceleration);
        standardVelocityUpdate.setCognitiveAcceleration(standardAcceleration);
        standardVelocityUpdate.setInertiaWeight(standardInertia);
        standardVelocityUpdate.setVMax(vMax);

        //mainSwarm.setVelocityUpdate(standardVelocityUpdate);

        // the main swarms particles need to be able to calculate the standard
        // deviation of their fitness.
        mainSwarm.getInitialisationStrategy().setEntityType(new DeviationDecorator(new StandardParticle(), observations));

        return nichePSO;
      }
      catch(Exception ex)
      {
        throw ex;
      }
    }
  }
}