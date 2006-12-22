/*
 * NichePSO.java
 *
 * Created on January 26, 2004, 10:00 PM
 *
 *
 * Copyright (C) 2004 - Clive Naicker
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
 *
 */
package net.sourceforge.cilib.pso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameterupdatestrategies.ConstantUpdateStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.pso.particle.DeviationDecorator;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.pso.velocityupdatestrategies.StandardVelocityUpdate;


/**
 * <p>Title: NichePSO</p>
 * <p>Description: CILib (Computational Intelligence Library).  This
 * class defines the NichePSO which can be used to find multilple
 * solutions within a search space.
 * </p>
 * <p>
 * The interested reader is referred to:
 * Brits, R., (2002).  Niching Strategies for Particle Swarm Optimization,
 * University of Pretoria.
 * </p>
 * @author Clive Naicker
 * @deprecated This is being rewritten
 */
// TODO: Create a more generic nicheing thing????
public class NichePSOOld extends PopulationBasedAlgorithm
{
  /**
   * The main swarm from which all subswarms will be created.
   */
  private PSO mainSwarm;
  /**
   * All subswarms created will be added to this vector.  Subswarms are of GCPSO.
   * Subswarms that merge are removed from the vector and replaced with the new
   * merged subswarm at the end of the vector.
   */
  private Collection<PSO> subswarms;
  /**
   * The merge threshold is used to determine if any two subswarms are close
   * enough to be merged.  That is, the particles from the two subswarms
   * form a single subswarm.
   */
  private double mergeThreshold;
  /**
   * The solutionThreshold represents the standard deviation of a particle's fitness
   * in the main swarm.  If the standard deviation of the particle is less than
   * the threshold then the particle is deemed to have found a solution.  The
   * particle is then removed from the main swarm and then a GCPSO is created
   * from that particle and its closest neighbour.
   */
  private double solutionThreshold;
  /**
   * The problem to solve.
   */
  private OptimisationProblem problem;
  /**
   * This is the number of observations to use to calculate the standard
   * deviation of the particles fitness and position.
   */
  private int observations;
  /**
   * This instance variable is used to calculate the Euclidean distance
   * between particles.
   */
  //private DistanceMeasure distanceMeasure;

  /**
   * The default constructor.  Initialises the mainSwarm to use
   * the ZeroVelocityComponent, StandardAcceleration and StandardInertia.
   * The acceleration is set to 1.2 and the inertia is set to 0.7.  The
   * maximum velocity is clamped to 0.5
   */
  public NichePSOOld()
  {
    // create a PSO for the main swarm.
    mainSwarm = new PSO();
    this.mergeThreshold = 0.0001;
    this.solutionThreshold = 0.1;
    this.observations = 3;
    
    //this.distanceMeasure = new EuclideanDistanceMeasure();

    ConstantUpdateStrategy zeroAcceleration = new ConstantUpdateStrategy(0.0);
    //AccelerationUpdateStrategy standardAcceleration = new AccelerationUpdateStrategy(1.2);
    //ConstantUpdateStrategy standardInertia = new ConstantUpdateStrategy(0.7);

    StandardVelocityUpdate standardVelocityUpdate = new StandardVelocityUpdate();
    standardVelocityUpdate.setSocialAcceleration(zeroAcceleration);
    //standardVelocityUpdate.setCognitiveAcceleration(standardAcceleration);
    //standardVelocityUpdate.setInertiaWeight(standardInertia);
    
    ConstantUpdateStrategy vmax = new ConstantUpdateStrategy(0.5);
    standardVelocityUpdate.setVMax(vmax);

    // the main swarms particles need to be able to calculate the standard
    // deviation of their fitness.
    
    Particle mainSwarmParticle = new StandardParticle(); //TODO: Pull this out
    mainSwarmParticle.setVelocityUpdateStrategy(standardVelocityUpdate);
    
    mainSwarm.getInitialisationStrategy().setEntityType(mainSwarmParticle);

    // initialise the subswarms.
    subswarms = new ArrayList<PSO>();
  }
  
  public NichePSOOld(NichePSOOld copy) {
	  
  }
  
  public NichePSOOld clone() {
	  return new NichePSOOld(this);
  }

  /**
   * The mainSwarm is initialised.
   */
  public void performInitialisation() {
  	// TODO: template method patterns breaks down for chained inheritance
  	// so while we don't have to remeber to Algorithm.initialise() we must still initialise the parent PSO stuff :(
  	//super.performInitialisation(); 
    
    // only the main swarm needs to be initialised.
    mainSwarm.initialise();
  }

  /**
   * This method will perform one iteration of the NichePSO
   * algorithm as defined by Brits et. al.
   */
  public void performIteration()
  {
    // perform one iteration of the mainSwarm (PSO algorithm).
    mainSwarm.performIteration();

    // double inertia = ((StandardVelocityUpdate)mainSwarm.getVelocityUpdate()).getInertiaComponent().getContribution();
    //int iteration = getIterations();
    //ConstantUpdateStrategy standardInertia = new ConstantUpdateStrategy();
    //standardInertia.setParameter(((0.1 - 0.7)/(500.0))*iteration + 0.7);
    // ((StandardVelocityUpdate)mainSwarm.getVelocityUpdate()).setInertiaWeight(standardInertia);

    // iterate through the subswarms and perform one iteration.
    Iterator<PSO> iterator = subswarms.iterator();
    while (iterator.hasNext())
    {
      // get the gcpso
      PSO pso = iterator.next();

      // perform one iteration for the gcpso.
      pso.performIteration();

      //iteration = pso.getIterations();
      //standardInertia = new ConstantUpdateStrategy();
      //standardInertia.setParameter(((0.1 - 0.7)/(500.0))*iteration + 0.7);
      // ((GCVelocityUpdate)gcpso.getVelocityUpdate()).getStandardVelocityUpdate().setInertiaWeight(standardInertia);
    }

    // merge subswarms
    mergeSubswarms();

    // absorb particles
    absorbParticles();

    // iterate through the particles of the mainSwarm to determine if any
    // of the particles satisfy the solutionThreshold.
    //iterator = mainSwarm.getTopology().particles();
  //  iterator = mainSwarm.getTopology().iterator();
    while (iterator.hasNext())
    
    {
      // get the particle.
    //  Particle particle = (Particle)iterator.next();

      // calculate the fitness deviation.
      /**
       * CN
       * @todo Note that with Riaans NichePSO he calculated the deviation of the particles fitness.
       * Inorder to normalise the particles fitness we need to know the maximum and minimum of the function.
       * As a result, this knowledge becomes problem dependent.  I suggest that we rather use the standard deviation
       * of the particles fitness which does need to be normalised to determine if a solution is found.
       */
      //double deviation = DeviationDecorator.extract(particle).getFitnessDeviation()/3;
      //double deviation = DeviationDecorator.extract(particle).getPositionDeviation();

      // determine if the deviation is less than the threshold.
      //if (Double.isNaN(deviation) || deviation < solutionThreshold)
      //{
        // the particle has found a solution.  Find the particles closest
        // neighbour.
        //Particle particleNeighbor = findClosestNeighbor(particle, mainSwarm.getTopology());

        //if (particleNeighbor != null)
        //{
          // remove the particle from the main swarm
          //removeParticle(mainSwarm.getTopology(), particle);

          // remove the neighboring particle from the main swarm.
          //removeParticle(mainSwarm.getTopology(), particleNeighbor);

          // we need to reinitialise the particle iterator for the main swarm.
          //iterator = mainSwarm.getTopology().particles();
          //iterator = mainSwarm.getTopology().iterator();

          // create a new GCPSO with the two particles.
          //GCPSO gcpso = new GCPSO();
          //gcpso.setOptimisationProblem(getOptimisationProblem());

          //Topology<Particle> topology = gcpso.getTopology();
          //Particle gcParticle = new GCDecorator(particle);
          //Particle gcParticleNeighbor = new GCDecorator(particleNeighbor); 
          //topology.add(gcParticle);
          //topology.add(gcParticleNeighbor);

          //GCVelocityUpdate gcVelocityUpdate = new GCVelocityUpdate();
          //StandardVelocityUpdate standardVelocityUpdate = new StandardVelocityUpdate();
          //ConstantUpdateStrategy gcStandardAcceleration = new ConstantUpdateStrategy();
          //ConstantUpdateStrategy gcStandardInertia = new ConstantUpdateStrategy();
          //ConstantUpdateStrategy gcVMax = new ConstantUpdateStrategy();

          //gcStandardAcceleration.setParameter(1.2);
          //gcStandardInertia.setParameter(0.7);
          //gcVMax.setParameter(0.5);

          //standardVelocityUpdate.setVMax(gcVMax);
          //standardVelocityUpdate.setSocialAcceleration(gcStandardAcceleration);
         // standardVelocityUpdate.setCognitiveAcceleration(gcStandardAcceleration);
          //standardVelocityUpdate.setInertiaWeight(standardInertia);
          //gcVelocityUpdate.setStandardVelocityUpdate(standardVelocityUpdate);

          //gcpso.setVelocityUpdate(gcVelocityUpdate);

          // we need to set the nieghborhood best of both particles.
          //particle.setNeighbourhoodBest(gcpso.getBestParticle());
          //particleNeighbor.setNeighbourhoodBest(gcpso.getBestParticle());

          // add the new gcpso subswarm to the vector of subswarms.
          //subswarms.add(gcpso);
        //}
      }
    //}

    // numberOfParticles();
    // particlePositions();
  }

  /**
   * This is a utility method that is used to calculate the number
   * of particles in the subswarms and mainSwarm
   * @return
   */
  /*private int numberOfParticles()
  {
    int mainSwarmSize = ((GBestTopology)mainSwarm.getTopology()).getSize();
    int subswarmSize = 0;

    Iterator iterator = subswarms.iterator();
    while (iterator.hasNext())
    {
      GCPSO gcpso = (GCPSO)iterator.next();
      subswarmSize += ((GBestTopology)gcpso.getTopology()).getSize();
    }
    System.out.print("Main swarm size = " + mainSwarmSize + ", ");
    System.out.println("Subswarms size = " + subswarmSize + ", Subswarms = " + subswarms.size());

    return mainSwarmSize + subswarmSize;
  }*/

  /**
   * This is debugging method to find all the particles positions
   */
  /*private void particlePositions()
  {
    printSwarm(mainSwarm);

    Iterator iterator = subswarms.iterator();
    while (iterator.hasNext())
    {
      PSO pso = (PSO)iterator.next();
      printSwarm(pso);
    }
  }*/

  /**
   * This is a debugging method to print to screen all
   * all the particles positions of the swarm in the PSO
   * @param pso The pso to print all the particles off.
   */
  /*private void printSwarm(PSO pso)
  {
    Iterator iterator = pso.getTopology().particles();
    while (iterator.hasNext())
    {
      Particle p = (Particle)iterator.next();
      //double[] position = p.getPosition();
      net.sourceforge.cilib.Type.Types.Vector position = p.getPosition();
      /*for (int i=0; i<position.length; i++)
      {
        System.out.print(position[i] + " ");
      }*
      for (int i = 0; i < position.getDimension(); i++) {
    	  System.out.println(position.getReal(i) + " ");
      }
      System.out.println();
    }
  }*/

  /**
   * In this method we absorb the particles that are in the mainswarm which
   * have intersected in the radius of a subswarm.  The particle is absorbed
   * into the first subswarm that satisfies this condition.
   */
  private void absorbParticles()
  {
    // Iterate through the particles in the main swarm.
    //Iterator iterator = mainSwarm.getTopology().particles();
	  Iterator iterator = mainSwarm.getTopology().iterator();
    while (iterator.hasNext())
    {
      // get the particle.
      Particle particle = (Particle)iterator.next();

      // Iterate through the subswarms to determine if the particle can be
      // absorbed with the subswarm.
      Iterator iterator2 = subswarms.iterator();

      // we need to keep track of the best index of the subswarm for this particle to be absorbed into.
      int bestIndex = -1;

      // we need to keep track of the index of the subswarm we are considering.
      int index = -1;

      while (iterator2.hasNext())
      {
        // get the subswarm.
        GCPSO gcpso = (GCPSO)iterator2.next();

        // increment the index
        index ++;

        // calculate the distance between the particle and the gcpso best particle
        double distance = distance(particle, gcpso.getBestParticle());

        // calculate the radius of the gcpso.
        double radius = getSwarmRadius(gcpso);

        // determine if the particle meets the absorbtion criterion.
        if (distance < radius)
        {
          bestIndex = index;
        }
      }

      // determine if a subswarm has been found.
      if (bestIndex != -1)
      {
        // the best index is not -1, therefore the particle can be absorbed into the subswarm at the index, bestIndex.
        // get the gcpso at bestIndex.
    	  
        //GCPSO gcpso = (GCPSO)subswarms.get(bestIndex);

        // absorb the particle into the subswarm.
//        Topology<Particle> topology = gcpso.getTopology();
        //Particle gcParticle = new GCDecorator(particle);
 //       topology.add(gcParticle);
  //      particle.setNeighbourhoodBest(gcpso.getBestParticle());

        // remove the particle from the mainSwarm.
        iterator.remove();
      }
    }
  }

  /**
   * In this method, all subswarms that intersect within the specified merge
   * threshold distance are merged into one subswarm.  The subswarms that form
   * the merge are removed from the vector subswarms and the new subswarm takes
   * its place.
   */
  private void mergeSubswarms()
  {
    // loop through the subswarms.
  //  for (int i=0; i<subswarms.size(); i++)
   // {
      // get the gcpso.
    //  GCPSO gcpso1 = (GCPSO)subswarms.get(i);

      // get the next gcpso
      //for (int j=i+1; j<subswarms.size(); j++)
      //{
        // get the next subswarm that is not the same as gcpso1
      //  GCPSO gcpso2 = (GCPSO)subswarms.get(j);

        // determine if they can be merged.
        // get the distance between the best particles of the subswarms
        // normalise the distance so that the mergeThreshold is not dependent on the problem
        // domain.
        //double distance = distance(gcpso1.getBestParticle(), gcpso2.getBestParticle());

        // get the swarm radius of gcpso1.
        //double radius1 = getSwarmRadius(gcpso1);

        // get the swarm radius of gcpso2.
        //double radius2 = getSwarmRadius(gcpso2);

        // if the radius approximately 0 then determine if the merge threshold is met.
        //if (radius1 < Math.pow(10.0, -3.0) &&
        //    radius2 < Math.pow(10.0, -3.0) &&
        //    normalise(distance) < getMergeThreshold())
        //{
//          // the swarms radii are too small to make a good observation.
//          if (normalise(distance) < getMergeThreshold())
//          {
            // merge the subswarms.
       //     mergeSubswarms(gcpso1, gcpso2);

            // remove gcpso2 as it is merged with gcpso1. gcpso1 contains the
            // particles that were in gcpso2
     //       subswarms.remove(j);

            // we need to determine if the new gcpso1 with the larger radius
            // can merge with the previous subswarms.
            /**
             * CN
             * @todo It is not certain whether this is necessary or should be done at all.  The result of restarting
             * the merge process with the larger subswarm is that no more merges can be done in this iteration.
             */
  //          j = subswarms.size();
   //         i = -1;
//          }
//        }
        /*else
        {
          // determine if the merge threshold is satisfied.
          if (distance < (radius1 + radius2) &&
              normalise(distance) < getMergeThreshold())
          {
            // merge the subswarms
            mergeSubswarms(gcpso1, gcpso2);

            // remove gcpso2 as it is merged with gcpso1.  gcpso1 contains the
            // particles that were in gcpso2
            subswarms.remove(j);

            // we need to determine if the new gcpso1 with the larger radius
            // can merge with the previous subswarms.
            /**
             * CN (Similiar point repeated here for clearity).
             * @todo It is not certain whether this is necessary or should be done at all.  The result of restarting
             * the merge process with the larger subswarm is that no more merges can be done in this iteration.
             *
            j = subswarms.size();
            i = -1;
          } // end of if
        } // end of else
      } // end of for loop
    } */ // end of for loop
  }

  /**
   * This method normalises the value between the range (-1.0, 1.0)  The function used for this process is the
   * hyperbolic tangent.
   * @param value The value to be normalised
   * @return A value in the range (-1.0, 1.0).
   */
  protected double normalise(double value)
  {
    double lambda = 1.00;
    return Math.abs((Math.exp(lambda*value) - Math.exp(-lambda*value))/(Math.exp(lambda*value) + Math.exp(-lambda*value)));
  }

  /**
   * This method is used to determine the swarm radius of the PSO.  The swarm radius is the largest distance from the
   * best particle in the PSO to all other particles in the PSO.
   * @param pso The PSO to determine the radius off.
   * @return The radius of the PSO.
   */
  public double getSwarmRadius(PSO pso)
  {
    // assume that the radius is 0
    double radius = 0;

    // the center is the best particle
    Particle center = pso.getBestParticle();

    // find the maximum distance from the center to the other particles
    //Iterator iterator = pso.getTopology().particles();
    Iterator iterator = pso.getTopology().iterator();
    while (iterator.hasNext())
    {
      // get the particle
      Particle particle = (Particle)iterator.next();

      // calculate the distance
      double distance = distance(center, particle);

      // determine if the distance is larger than the current radius.
      if (distance > radius)
      {
        // update the radius
        radius = distance;
      }
    }
    // return the radius
    return radius;
  }

  /**
   * This method takes the particles from gcpso2 and adds them to gcpso1
   * @param gcpso1 The subswarm that will have the particles from gcpso2 added
   * to it.
   * @param gcpso2 The subswarm that will have its particles removed from it
   * and added to gcpso1.
   */
 /* private void mergeSubswarms(GCPSO gcpso1, GCPSO gcpso2)
  {
    // iterate through the particles of gcpso2
    //Iterator iterator = gcpso2.getTopology().particles();
	Iterator iterator = gcpso2.getTopology().iterator();
    while (iterator.hasNext())
    {
      // get the particle from gcpso2
      Particle particle = (Particle)iterator.next();

      // remove the particle from gcpso2.
      iterator.remove();

      // add the particle to gcpso1
      gcpso1.getTopology().add(particle);
    }
  }*/

  /**
   * This is a utility method to remove the particle from the swarm.
   * @param topology The topology to remove the particle from
   * @param particle The particle to remove
   */
  /*private void removeParticle(Topology topology, Particle particle)
  {
    // we need to iterate through the particles in the topology to find the
    // particle and remove it.
    //Iterator iterator = topology.particles();
	  Iterator iterator = topology.iterator();
    while (iterator.hasNext())
    {
      // get the particle
      Particle p = (Particle)iterator.next();

      // determine if this is the particle to remove.
      if (p.equals(particle))
      {
        // remove the particle
        iterator.remove();

        // there is no need to iterate through the topology any further
        // as it is assumed that the particle cannot occur twice in the
        // topology.
        return;
      }
    }
  }*/

  /**
   * This is a utility method that will find the particle that is closest to the
   * particle specified.  The Euclidean distance is used to determine the closest
   * particle.  That is, the particle that has the smallest Euclidean distance
   * to the specified particle is the closest particle.
   * @param particle The specified particle to find the closest neighbor to it.
   * @param topology The topology which contains the particles neighbors.
   * @return The closest neighbor to the specified particle. If no particle
   * could be found then null is returned.
   */
  public static Particle findClosestNeighbor(Particle particle, Topology<Particle> topology)
  {
    // We need to iterate through the particles in the topology.
    //Iterator iterator = topology.particles();
	  Iterator<Particle> iterator = topology.iterator();

    // assume that the closest distance is positive infinity.
    double closestDistance = Double.POSITIVE_INFINITY;

    // let the result be null.
    Particle result = null;

    while (iterator.hasNext())
    {
      // get the particle.
      Particle p = (Particle)iterator.next();

      // we must make sure that this particle is not the same as the
      // specified particle.
      if (!particle.equals(p))
      {
        // determine the distance.
        double distance = distance(particle, p);

        // determine if the distance is the closest distance thus far.
        if (distance < closestDistance)
        {
          // update the closestDistance
          closestDistance = distance;

          // update the result.
          result = p;
        }
      }
    }
    // return the result.
    return result;
  }

  /**
   * This is a utility method that is used to find the Euclidean distance
   * between two particles.
   * @param p1 Particle p1
   * @param p2 Particle p2
   * @return The Euclidean distance between particle p1 and p2.
   */
  public static double distance(Particle p1, Particle p2)
  {
	 // net.sourceforge.cilib.Type.Types.Vector p1Position = (net.sourceforge.cilib.Type.Types.Vector) p1.getPosition();
	 // net.sourceforge.cilib.Type.Types.Vector p2Position = (net.sourceforge.cilib.Type.Types.Vector) p2.getPosition();
	  //return distanceMeasure.distance(p1Position, p2Position);
	  return 0;
  }

  /**
   * Accessor method for the mergeThreshold.  Set the threshold at which
   * subswarms are to be merged into one subswarm.
   * @param mergeThreshold The merge threshold.
   */
  public void setMergeThreshold(double mergeThreshold)
  {
    this.mergeThreshold = mergeThreshold;
  }

  /**
   * Accessor method for the mergeThreshold.  Get the threshold at which
   * subswarms are to be merged into one subswarm.
   * @return The mergeTheshold
   */
  public double getMergeThreshold()
  {
    return mergeThreshold;
  }

  /**
   * Accessor method for the solutionThreshold.  Set the threshold at which
   * a particles standard fitness deviation in the mainswarm is to be so that
   * the particle can be considered to have found a solution.
   * @param solutionThreshold The solution threshold.
   */
  public void setSolutionThreshold(double solutionThreshold)
  {
    this.solutionThreshold = solutionThreshold;
  }

  /**
   * Accessor method for the solutionThreshold.  Get the threshold for the
   * standard deviation of a particles fitness to identify a solution in the
   * main swarm.
   * @return The solutionThreshold
   */
  public double getSolutionThreshold()
  {
    return solutionThreshold;
  }

  /**
   * Accessor method for the mainSwarm.  Set the mainSwarm for the NichePSO.
   * @param mainSwarm The mainSwarm.
   */
  public void setMainSwarm(PSO mainSwarm)
  {
    this.mainSwarm = mainSwarm;
    this.mainSwarm.getInitialisationStrategy().setEntityType(new DeviationDecorator(new StandardParticle(), getObservations()));

    /**
     * @todo we need to test if the mainSwarm's particles have the
     * DeviationDecorator.
     */
  }

  /**
   * Accessor method for the mainSwarm.  Get the mainSwarm for the NichePSO
   * @return The main swarm for the NichePSO.
   */
  public PSO getMainSwarm()
  {
    return mainSwarm;
  }

  /**
   * This is an accessor method for the field subswarms.
   * @return The field subswarms.
   */
  public Vector getSubswarms()
  {
    //return subswarms;
	  return null;
  }

  public void setSubswarms(Vector<PSO> subswarms)
  {
    this.subswarms = subswarms;
  }

  /**
   * Accessor method for observations.  Get the number of observations that
   * are used to calculate the standard deviation of the particles fitness
   * and position.
   * @return The observations
   */
  public int getObservations()
  {
    return observations;
  }

  /**
   * Accessor method for observations. Set the number of observations that
   * are used to calculate the standard deviation of the particles fitness
   * and position
   * @param observations The observations.
   */
  public void setObservations(int observations)
  {
    this.observations = observations;
  }

  /**
   * Sets the optimisation problem to be solved.
   * @param problem The {@link net.sourceforge.cilib.Problem.OptimisationProblemAdapter}
   * to be solved.
   */
  public void setOptimisationProblem(OptimisationProblem problem)
  {
    this.problem = problem;
    mainSwarm.setOptimisationProblem(problem);
    Iterator iterator = subswarms.iterator();
    while (iterator.hasNext())
    {
      GCPSO gcpso = (GCPSO)iterator.next();
      gcpso.setOptimisationProblem(problem);
    }
  }

  /**
   * Accessor for the optimisation problem to be solved.
   * @return The {@link net.sourceforge.cilib.Problem.OptimisationProblemAdapter}
   * to be solved.
   */
  public OptimisationProblem getOptimisationProblem()
  {
    return problem;
  }

  /**
   * Returns the best solution found by the algorithm so far.
   * @return A double array of length {@link net.sourceforge.cilib.Problem.OptimisationProblemAdapter#getDimension()}
   * that represents the best solution found.
   */
  public OptimisationSolution getBestSolution()
  {
    // get the solutions all subswarms including the main swarm.
    Collection solutions = getSolutions();

    // iterate through the solutions to determine which is the best solution.
    Iterator iterator = solutions.iterator();

    // assume the best solution is the first object.
    OptimisationSolution result = (OptimisationSolution)iterator.next();

    // find a better solution.
    while (iterator.hasNext())
    {
      // get the next solution.
      OptimisationSolution solution = (OptimisationSolution)iterator.next();

      // determine if this solution is better.
      if (solution.getFitness().compareTo(result.getFitness()) < 0)
      {
        // update the result with the better fitness.
        result = solution;
      }
    }
    // return the result.
    return result;
  }

  public List<OptimisationSolution> getSolutions()
  {
    // if there are no subswarms, then we must return the solution from the
    // main swarm.
    if (subswarms.isEmpty())
    {
      return mainSwarm.getSolutions();
    }
    else
    {
      // get the solution from the mainSwarm
      List<OptimisationSolution> solutions = new Vector<OptimisationSolution>();

      // if the main swarm has particles then we can get solutions
      //if (mainSwarm.getTopology().particles().hasNext())
      if (mainSwarm.getTopology().iterator().hasNext())
      {
        solutions = mainSwarm.getSolutions();
      }

      // add to the solutions, the solutions from the subswarms.
      Iterator iterator = subswarms.iterator();
      while (iterator.hasNext())
      {
        // get the gcpso
        GCPSO gcpso = (GCPSO)iterator.next();

        // get the solutions.
        Collection<OptimisationSolution> gcpsoSolutions = gcpso.getSolutions();

        // add the solutions.
        Iterator solutionsIterator = gcpsoSolutions.iterator();
        while (solutionsIterator.hasNext())
        {
          OptimisationSolution solution = (OptimisationSolution)solutionsIterator.next();

          // add the solution
          solutions.add(solution);
        }
      }
      // return the solutions.
      return solutions;
    }
  }

  /**
   * Returns the number of times that the fitness function,
   * specified by {@link net.sourceforge.cilib.Problem.OptimisationProblem#getFitness(double[])},
   * has been evaluated.
   * @return The number of fitness evaluations.
   */
  /*
  public int getFitnessEvaluations()
  {
    // let the result be 0.
    int result = 0;

    // add the fitness evaluations of the subswarms.
    Iterator iterator = subswarms.iterator();
    while (iterator.hasNext())
    {
      // get the GCPSO.
      GCPSO gcpso = (GCPSO)iterator.next();

      // add the fitness evaluations.
      result += gcpso.getFitnessEvaluations();
    }
    // add the fitness evaluations of the mainSwarm.
    result += mainSwarm.getFitnessEvaluations();

    // return the result
    return result;
  }
  */

  /**
   * Returns contribution to the solution for the co-operative optimisation algorithm.
   * @return The algorithm's solution contribution.
   */
  public Particle getContribution() {
	  //TODO: this may not be what you want, change as desired
	  return getBestParticle();
  }

  /**
   * Returns the fitness of contribution to the solution.
   * @return The fitness of the solution contribution.
   */
  public Fitness getContributionFitness() {
	  //TODO: this may not be what you want, change as desired
      return getBestParticle().getBestFitness();
  }

  /**
   * Updates the new fitness for the solution contribution.
   * @param fitness The new fitness of the contribution.
   */
  public void updateContributionFitness(Fitness fitness) {
	  //TODO: this may not be what you want, change as desired
      getBestParticle().setFitness(fitness);
  }

  /**
   * Finds the best particle in the subswarm and in the mainswarm.
   * @return The best particle.
   */
  public Particle getBestParticle()
  {
    // assume that the best particle is found in the mainswarm.
    Particle result = mainSwarm.getBestParticle();

    // try to find a better particle in the subswarms.
    Iterator iterator = subswarms.iterator();
    while (iterator.hasNext())
    {
      // get the gcpso
      GCPSO gcpso = (GCPSO)iterator.next();

      // get the best particle.
      Particle particle = gcpso.getBestParticle();

      // determine if this particle is better.
      if (particle.getBestFitness().compareTo(result.getBestFitness()) < 0)
      {
        // update the result
        result = particle;
      }
    }
    // return the result.
    return result;
  }

@Override
public int getPopulationSize() {
	// TODO Auto-generated method stub
	return 0;
}

@Override
public void setPopulationSize(int populationSize) {
	// TODO Auto-generated method stub
	
}

@Override
public Topology<? extends Entity> getTopology() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void setTopology(Topology topology) {
	// TODO Auto-generated method stub
	
}

@Override
public double getDiameter() {
	// TODO Auto-generated method stub
	return 0;
}

@Override
public double getRadius() {
	// TODO Auto-generated method stub
	return 0;
}
}