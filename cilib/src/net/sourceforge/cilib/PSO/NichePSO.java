/* 
 * NichePSO.java
 *
 *
 * Copyright (C) 2003 - Clive Naicker
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

package net.sourceforge.cilib.PSO;

import net.sourceforge.cilib.Problem.*;
import net.sourceforge.cilib.Algorithm.*;
import net.sourceforge.cilib.Indicator.*;
import java.util.*;

/**
 * <p>
 * An implementation of the NichePSO algorithm
 * </p><p>
 * References
 * </p><p><ul><li>
 * R. Brits, A.P. Engelbrecht and F. van den Bergh. "A Niching Particle Swarm
 * Optimizer" in Proceedings of the Conference on Simulated Evolution and 
 * Learning. (Singapore) November 2002.
 * </li><li>
 * R.Brits. "Niching Strategies for particle swarm opmtimization" Masters Thesis.
 * Department of Computer Science. University of Pretoria. South Africa. 2002.
 * </li></ul></p>
 */

public class NichePSO extends Algorithm implements OptimisationAlgorithm, ParticipatingAlgorithm {
  private NicheTopology topology;
  private OptimisationProblem problem;
  private Particle bestParticle;
  private int fitnessEvaluations;
  private Class particleClass;
  private GCPSO MainSwarm;
  private Vector SubSwarms;
  private int topologySize;
  private double mergeThreshold;
  private double nicheThreshold;

  public NichePSO() {
    super();
    //System.out.println("NichePSO()...");
    setParticleClass(NicheParticle.class);
    setTopologySize(20);
    mergeThreshold = Math.pow(10.0, -5.0);
    nicheThreshold = Math.pow(10.0, -3.0);
    //this.addProgressIndicator(new MaximumIterations(1000));
  }

  public double getMergeThreshold()
  {
    return mergeThreshold;
  }

  public void setMergeThreshold(double mergeThreshold)
  {
    this.mergeThreshold = mergeThreshold;
  }

  public double getNicheThreshold()
  {
    return nicheThreshold;
  }

  public void setNicheThreshold(double nicheThreshold)
  {
    this.nicheThreshold = nicheThreshold;
  }

  public int getTopologySize()
  {
    return topologySize;
  }

  public void setTopologySize(int topologySize)
  {
    this.topologySize = topologySize;
  }

  public NicheTopology getTopology()
  {
    return topology;
  }

  public void setTopology(NicheTopology topology)
  {
    this.topology = topology;
  }

  public void initialise()
  {
    super.initialise();
    // Create the Main Swarm.
    MainSwarm = new GCPSO();
    // Create a new GBestTopology for the MainSwarm.
    NicheTopology MainSwarmTopology = new NicheTopology();
    MainSwarmTopology.setSize(topologySize);

    //System.out.println("NicheTopology.initialise() : " + getParticleClass().getName());
    //MainSwarmTopology.initialise(getParticleClass());

    // Set the Topology for the Main Swarm.
    // System.out.println("NicheTopology.initialise() : " + "Set the MainSwarm topology...");
    MainSwarm.setTopology(MainSwarmTopology);

    // Set the problem for the Main Swarm.
    // System.out.println("NicheTopology.initialise() : " + "Set the MainSwarm problem...");
    MainSwarm.setOptimisationProblem(problem);

    // Set the particle class for the Main Swarm
    // System.out.println("NicheTopology.initialise() : " + "Set the MainSwarm particle class...");
    MainSwarm.setParticleClass(getParticleClass());

    // Initialise the Main Swarm.
    // System.out.println("NicheTopology.initialise() : " + "Initialise the MainSwarm...");
    MainSwarm.initialise();

    // Initialise the SubSwarms.
    SubSwarms = new Vector();
  }

  public void performIteration() {
    // Train the main swarm using the cognition only model
    // System.out.println("NichePSO.performIteration() : " + "MainSwarm.performIteration()...");
    MainSwarm.performIteration();
    // Train the SubSwarm
    Iterator iterator = SubSwarms.iterator();
    while (iterator.hasNext())
    {
      // Train the particles in the SubSwarm
      GCPSO SubSwarm = (GCPSO)iterator.next();
      // System.out.println("NichePSO.performIteration() : " + "Train SubSwarms...");
      SubSwarm.performIteration();
    }
    // Try to merge SubSwarms
    // System.out.println("NichePSO.performIteration() : " + "Merge SubSwarms...");
    mergeSubSwarms(getMergeThreshold());

    // Try to absorb any particles from the MainSwarm into SubSwarms
    // System.out.println("NichePSO.performIteration() : " + "Absorb Particles...");
    absorbParticles();
    // Try to create new niches from the MainSwarm.
    // System.out.println("NichePSO.performIteration() : " + "Identify Niches...");
    if (((NicheTopology)MainSwarm.getTopology()).getSize() > 2)
    {
      identifyNiches(getNicheThreshold());
    }
    double bestFitness = getBestParticle().getFitness();
    //System.out.println("NichePSO.performIteration() : " +
    //                   "Best Particle fitness  = " + bestFitness);
  }

  public void identifyNiches(double dMinDeviation)
  {
    addObservation();
    // System.out.println("NichePSO.identifyNiches(double) : " +
    //                   "MainSwarm.Topology.size = " + ((NicheTopology)MainSwarm.getTopology()).getSize());
    // for each particle in the MainSwarm determine whether the Particle
    // can be created into a new Swarm for a niche.
    Iterator iterator = ((NicheTopology)MainSwarm.getTopology()).particles();
    while (iterator.hasNext() && ((NicheTopology)MainSwarm.getTopology()).getSize() > 2)
    {
      NicheParticle p = (NicheParticle)iterator.next();
      double deviation = p.calculateStandardDeviation();
      if (deviation >= 0 && deviation < dMinDeviation)
      {
        // System.out.println("NichePSO.identifyNiches(double) : " + "Identified Niche...");
        // Create a new SubSwarm
        GCPSO Swarm = new GCPSO();
        // Find the closest neighbor to Particle p.
        NicheTopology topology = (NicheTopology)MainSwarm.getTopology();
        NicheParticle p2 = (NicheParticle)topology.findClosestNeighbor(p);
        // Create a new topology for the new SubSwarm
        NicheTopology topology2 = new NicheTopology();
        topology2.setSize(0);
        topology2.initialise(getParticleClass());
        // add the particles to the topology.
        topology2.addParticle(p);
        topology2.addParticle(p2);
        // remove the particles from the MainSwarm.
        topology.removeParticle(p);
        topology.removeParticle(p2);
        // Set the new topology for the new SubSwarm
        Swarm.setTopology(topology2);
        // Set the problem for the SubSwarm
        Swarm.setOptimisationProblem(problem);
        // Add the new SubSwarm to vector of SubSwarms
        SubSwarms.add(Swarm);
        System.out.println("NichePSO.identifyNiches(double) : " +
                           "Number of SubSwarms = " + SubSwarms.size());
      }
    }
  }

  private void addObservation()
  {
    // Add the observation to the particles within the Main Swarm.
    Iterator iteratorParticles = ((NicheTopology)MainSwarm.getTopology()).particles();
    while (iteratorParticles.hasNext())
    {
      NicheParticle p = (NicheParticle)iteratorParticles.next();
      p.addObservation(p.getFitness());
    }
    // Add the observation to the particles within the SubSwarms.
    Iterator iteratorSubSwarms = SubSwarms.iterator();
    while (iteratorSubSwarms.hasNext())
    {
      GCPSO Swarm = (GCPSO)iteratorSubSwarms.next();
      // Get the particles within the Swarm.
      Iterator iteratorSwarmParticles = ((NicheTopology)Swarm.getTopology()).particles();
      while (iteratorSwarmParticles.hasNext())
      {
        NicheParticle p = (NicheParticle)iteratorSwarmParticles.next();
        p.addObservation(p.getFitness());
      }
    }
  }

  public void absorbParticles()
  {
    // Try to add the particles from the MainSwarm to any of the SubSwarms.
    try
    {
      Iterator iteratorParticles = ((NicheTopology)MainSwarm.getTopology()).particles();
      while (iteratorParticles.hasNext())
      {
        // Get the particle from the MainSwarm.
        NicheParticle p = (NicheParticle)iteratorParticles.next();
        // Get the SubSwarms.
        Iterator iteratorSubSwarms = SubSwarms.iterator();
        while (iteratorSubSwarms.hasNext())
        {
          GCPSO Swarm = (GCPSO)iteratorSubSwarms.next();
          // Get the best particle from the swarm.
          NicheParticle bestParticle = (NicheParticle)Swarm.getBestParticle();
          // Get the distance from the best particle to particle p.
          double distance = distance(bestParticle.getBestPosition(), p.getBestPosition());
          // if the distance is less than the Radius of the swarm, then absorb the particle.
          double numParticles = ((NicheTopology)Swarm.getTopology()).getSize();
          if (distance < Swarm.getSwarmDiameter()/(2.0*numParticles))
          {
            // System.out.println("NichePSO.absorbParticles() : " + "Absorbing Particle...");
            ((NicheTopology)Swarm.getTopology()).addParticle(p);
            ((NicheTopology)MainSwarm.getTopology()).removeParticle(p);
          }
        }
      }
    }
    catch(Exception e)
    {
      System.err.println("NichePSO.absorbParticles() : " + e);
    }
  }

  public void mergeSubSwarms(double dMinRadius) {
    if (SubSwarms.size() == 0)
    {
      return;
    }
    // Loop through the SubSwarms, and determine if the swarms can be merged.
    for (int i=0; i<SubSwarms.size()-1; i++)
    {
      for (int j=i+1; j<SubSwarms.size(); j++)
      {
        GCPSO Swarm_i = (GCPSO)SubSwarms.get(i);
        GCPSO Swarm_j = (GCPSO)SubSwarms.get(j);
        // Get the number of particles in each swarm.
        double numParticles_i = ((NicheTopology)Swarm_i.getTopology()).getSize();
        double numParticles_j = ((NicheTopology)Swarm_j.getTopology()).getSize();
        // Get the radius from each swarm.  This is an average radius.
        double radius_i = Swarm_i.getSwarmDiameter()/(2.0*numParticles_i);
        double radius_j = Swarm_j.getSwarmDiameter()/(2.0*numParticles_j);
        // get the best particles from each swarm.
        Particle bestParticle_i = Swarm_i.getBestParticle();
        Particle bestParticle_j = Swarm_j.getBestParticle();
        // determine the distance between each of the best particle.
        double distance = distance(bestParticle_i.getPosition(), bestParticle_j.getPosition());
        // determine if Swarms[i] and Swarms[j] can be merged.
        if (radius_i > 0 && radius_j > 0)
        {
          if (normalise(distance) < normalise(radius_i+radius_j))
          {
            mergeSubSwarms(Swarm_i, Swarm_j);
            System.out.println("NichePSO.mergeSubSwarms(double) : " +
                               "Number of SubSwarms = " + SubSwarms.size());
          }
        }
        else if(normalise(distance) < dMinRadius)
        {
          mergeSubSwarms(Swarm_i, Swarm_j);
          System.out.println("NichePSO.mergeSubSwarms(double) : " +
                               "Number of SubSwarms = " + SubSwarms.size());
        }
      }
    }
  }

  private void mergeSubSwarms(GCPSO Swarm_i, GCPSO Swarm_j) {
    // Get the topology of the swarms
    NicheTopology topology_i = (NicheTopology)Swarm_i.getTopology();
    NicheTopology topology_j = (NicheTopology)Swarm_j.getTopology();
    // Get the number of particles in each swarm
    int numParticles_i = topology_i.getSize();
    int numParticles_j = topology_j.getSize();
    // Create a new a Swarm
    GCPSO Swarm_ij = new GCPSO();
    // Create a new topology for the Swarm_ij.
    NicheTopology topology_ij = new NicheTopology();
    topology_ij.setSize(0);
    topology_ij.initialise(getParticleClass());
    // Add the particles from topology_i to the topology_ij
    Iterator iterator_i = topology_i.particles();
    while (iterator_i.hasNext())
    {
      NicheParticle p = (NicheParticle)iterator_i.next();
      topology_ij.addParticle(p);
    }
    // Add the particles from topology_j to the topology_ij
    Iterator iterator_j = topology_j.particles();
    while (iterator_j.hasNext())
    {
      NicheParticle p = (NicheParticle)iterator_j.next();
      topology_ij.addParticle(p);
    }
    // Set the topology of the SubSwarm.
    Swarm_ij.setTopology(topology_ij);
    // Set the problem for the SubSwarm.
    Swarm_ij.setOptimisationProblem(problem);
    // Remove Swarm_i and Swarm_j from the Vector of the SubSwarms.
    SubSwarms.remove(Swarm_i);
    SubSwarms.remove(Swarm_j);
    // Add the swarm Swarm_ij to the Vector of SubSwarms
    SubSwarms.add(Swarm_ij);
  }

  private double normalise(double dValue) {
    return 1.0/(1.0 + Math.exp(-dValue));
  }

  private double distance(double p1[], double p2[]) {
    // Calculate the sum of the squares of the difference of each dimension.
    double sum = 0.0;
    for (int i=0; i<p1.length; i++)
    {
      sum += Math.pow(p1[i]-p2[i], 2.0);
    }
    // The distance is the square root of the of the sum.
    double distance = Math.sqrt(sum);
    return distance;
  }

  public Class getParticleClass() {
    return particleClass;
  }

  /**
   * Find the best particle in all the SubSwarms (and MainSwarm).
   * @return The best particle.
   */
  public Particle getBestParticle() {
    // Get the best particle from the MainSwarm.
    Particle MainSwarmBestParticle = null;
    if (((NicheTopology)MainSwarm.getTopology()).getSize() > 0)
    {
      MainSwarmBestParticle = MainSwarm.getBestParticle();
    }
    Particle BestParticle = MainSwarmBestParticle;
    // Compare the BestParticle to the Best Particles found in the SubSwarm
    // and update the BestParticle.
    int iSwarm = 0;
    Iterator iterator = SubSwarms.iterator();
    while (iterator.hasNext()) {
      GCPSO SubSwarm = (GCPSO)iterator.next();
      Particle SubSwarmBestParticle = SubSwarm.getBestParticle();
//      System.out.println("Swarm [" + iSwarm + "] best particle fitness = " +
//                         SubSwarmBestParticle.getBestFitness());
      if (BestParticle == null)
      {
        BestParticle = SubSwarmBestParticle;
      }
      if (BestParticle.getBestFitness() < SubSwarmBestParticle.getBestFitness())
      {
        BestParticle = SubSwarmBestParticle;
      }
    }

    // return the BestParticle found
    return BestParticle;
  }

  public void setParticleClass(Class particleClass)
  {
    this.particleClass = particleClass;
  }

  /**
   * Implementation of the OptimisationAlgorithm interface.
   * @param problem
   */
  public void setOptimisationProblem(OptimisationProblem problem) {
    this.problem = problem;
  }

  /**
   * Implementation of the OptimisationAlgorithm interface.
   * @return
   */
  public OptimisationProblem getOptimisationProblem() {
    return problem;
  }

  /**
   * Implementation of the OptimisationAlgorithm interface.
   * @return
   */
  public double[] getSolution() {
    return this.getBestParticle().getBestPosition();
  }

  /**
   * Implementation of the OptimisationAlgorithm interface.
   * @return
   */
  public double getSolutionFitness() {
    return this.getBestParticle().getBestFitness();
  }

  /**
   * Implementation of the OptimisationAlgorithm interface.
   * @return
   */
  public int getFitnessEvaluations() {
    return fitnessEvaluations;
  }

  /**
   * Implementation of the ParticipatingAlgorithm interface.
   * @return
   */
  public double[] getContribution() {
    return this.getBestParticle().getBestPosition();
  }

  /**
   * Implementation of the ParticipatingAlgorithm interface.
   * @return
   */
  public double getContributionFitness() {
    return this.getBestParticle().getBestFitness();
  }

  /**
   * Implementation of the ParticipatingAlgorithm interface.
   * @param fitness
   */
  public void updateContributionFitness(double fitness) {
  }
}