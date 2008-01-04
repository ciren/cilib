/*
 * FitnessDeviationCreationStrategy.java
 *
 * Created on 13 May 2006
 *
 * Copyright (C) 2003 - 2006 
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
 *
 */
package net.sourceforge.cilib.pso.niching;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.container.Pair;
import net.sourceforge.cilib.container.SortedList;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.RandomizingControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.pso.NichePSO;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.iterationstrategies.PerElementReinitialisation;
import net.sourceforge.cilib.pso.velocityupdatestrategies.GCVelocityUpdateStrategy;
import net.sourceforge.cilib.pso.velocityupdatestrategies.StandardVelocityUpdate;
import net.sourceforge.cilib.stoppingcondition.MaximumIterations;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;



public class FitnessDeviationCreationStrategy<E extends PopulationBasedAlgorithm> implements SwarmCreationStrategy<E>
{
    private Hashtable<String, LinkedList<Double>> mainSwarmParticleFitness;
    private ControlParameter threshold;
    private int fitnessTraceLength;
    private int minimumSubSwarmSize;

    public FitnessDeviationCreationStrategy()
    {
	this.mainSwarmParticleFitness = new Hashtable<String, LinkedList<Double>>();
	this.threshold = new ConstantControlParameter(0.0001);
	this.fitnessTraceLength = 3;
	this.minimumSubSwarmSize = 2;
    }

    public void create(NichePSO pso)
    {

	// remove all particle fitness storage for particles removed from the
	// main swarm
	Enumeration<String> e = this.mainSwarmParticleFitness.keys();
	while (e.hasMoreElements())
	{
	    String id = (String) e.nextElement();
	    Particle p = getParticleWidID(pso.getMainSwarm(), id);

	    if (p == null)
		this.mainSwarmParticleFitness.remove(id);
	}

	// manage the fitness queue representing the last X fitness values for each particle
	Iterator<Particle> mainSwarmIterator = pso.getMainSwarm().getTopology().iterator();
	while (mainSwarmIterator.hasNext())
	{
	    Particle mainSwarmParticle = (Particle) mainSwarmIterator.next();

	    if (this.mainSwarmParticleFitness.containsKey(mainSwarmParticle.getId()))
	    {
		LinkedList<Double> particleFitnessQueue = (LinkedList<Double>) this.mainSwarmParticleFitness.get(mainSwarmParticle.getId());

		if (particleFitnessQueue.size() < fitnessTraceLength)
		{
		    particleFitnessQueue.add(mainSwarmParticle.getFitness().getValue());
		}
		else
		{
		    particleFitnessQueue.remove();
		    particleFitnessQueue.add(mainSwarmParticle.getFitness().getValue());
		}
	    }
	    else
	    {
		LinkedList<Double> particleFitnessQueue = new LinkedList<Double>();
		particleFitnessQueue.add(mainSwarmParticle.getFitness().getValue());
		this.mainSwarmParticleFitness.put(mainSwarmParticle.getId(), particleFitnessQueue);
	    }
	}

	Enumeration<String> n = this.mainSwarmParticleFitness.keys();
	while (n.hasMoreElements())
	{
	    String id = (String) n.nextElement();
	    LinkedList<Double> particleFitnessQueue = this.mainSwarmParticleFitness.get(id);

	    if (particleFitnessQueue.size() == fitnessTraceLength)
	    {
		// fetch the standard deviation
		Double total = 0.0d;
		for (Iterator<Double> i = particleFitnessQueue.iterator(); i.hasNext();)
		    total += i.next();

		Double ave = total / fitnessTraceLength;
		Double stdDev = 0.0d;
		for (Iterator<Double> i = particleFitnessQueue.iterator(); i.hasNext();)
		    stdDev += Math.pow((i.next() - ave), 2);

//		stdDev = Math.sqrt(stdDev / fitnessTraceLength);
//		Vector v = new Vector();
//		for (Double d : particleFitnessQueue) {
//			v.add(new Real(d));
//		}
//			
//		stdDev = StatUtils.stdDeviation(v);

		// if the standard deviation is lower than the threshold, create a subswarm
		if (stdDev.compareTo(Double.NaN) != 0)
		{
		    if (stdDev < this.threshold.getParameter())
		    {

//			Particle p = (Particle) pso.getMainSwarm().getParticleWithID(id);
			Particle p = getParticleWidID(pso.getMainSwarm(), id);
			if (p != null)
			{

			    // find the nearest neighbors
			    DistanceMeasure distanceMeasure = new EuclideanDistanceMeasure();
			    mainSwarmIterator = pso.getMainSwarm().getTopology().iterator();

			    SortedList<Pair<Double, Entity>> sortedDistanceList = new SortedList<Pair<Double, Entity>>(new PairDistanceAndParticleComparator());
			    
			    while (mainSwarmIterator.hasNext())
			    {
				Particle mainSwarmParticle = (Particle) mainSwarmIterator.next();

				if (p.getId().compareToIgnoreCase(mainSwarmParticle.getId()) != 0)
				{

				    double distance = distanceMeasure.distance((Vector) p.getPosition(), (Vector) mainSwarmParticle.getPosition());

				    Pair<Double, Entity> distanceAndParticle = new Pair<Double, Entity>(distance, mainSwarmParticle);
				    sortedDistanceList.add(distanceAndParticle);
				}
			    }

			    while (sortedDistanceList.size() >= minimumSubSwarmSize)
				sortedDistanceList.removeLast();			     

			    // create the subswarm
			    if (sortedDistanceList.size() > 0)
			    {
//				Pair<Double, Entity> topDistanceAndParticle = sortedDistanceList.getLast();
//				double SubSwarmRadius = topDistanceAndParticle.getKey(); //(Double) distances.get(newNeighbours.size() - 1);
				pso.getMainSwarm().getTopology().remove(p);

				RandomizingControlParameter socialAcceleration = new RandomizingControlParameter();
				RandomizingControlParameter cognitiveAcceleration = new RandomizingControlParameter();
				socialAcceleration.setControlParameter(new ConstantControlParameter(1.2));
				cognitiveAcceleration.setControlParameter(new ConstantControlParameter(1.2));

				((StandardVelocityUpdate) p.getVelocityUpdateStrategy()).setSocialAcceleration(socialAcceleration.getClone());
				((StandardVelocityUpdate) p.getVelocityUpdateStrategy()).setCognitiveAcceleration(cognitiveAcceleration.getClone());

				PSO newSubSwarm = new PSO();
				newSubSwarm.setOptimisationProblem(pso.getMainSwarm().getOptimisationProblem());
				newSubSwarm.addStoppingCondition(new MaximumIterations(Integer.MAX_VALUE));
				// impossible to initialize an empty swarm..
				newSubSwarm.getInitialisationStrategy().setEntityNumber(5);
				newSubSwarm.initialise();
				//newSubSwarm.setIterationStrategy(pso.getSubSwarmIterationStrategy().clone());
				newSubSwarm.getInitialisationStrategy().setEntityNumber(0);
				newSubSwarm.getIterationStrategy().setBoundaryConstraint(new PerElementReinitialisation());
				newSubSwarm.getTopology().clear();
				newSubSwarm.getTopology().add(p);
				
				p.setVelocityUpdateStrategy(new GCVelocityUpdateStrategy()); //pso.getSubSwarmVelocityUpdateStrategy().clone());

				for (int i = 0; i < sortedDistanceList.size(); i++)
				{
				    Particle current = (Particle) sortedDistanceList.get(i).getValue();
				    pso.getMainSwarm().getTopology().remove(current);

				    ((StandardVelocityUpdate) current.getVelocityUpdateStrategy()).setSocialAcceleration(socialAcceleration.getClone());
				    ((StandardVelocityUpdate) current.getVelocityUpdateStrategy()).setCognitiveAcceleration(cognitiveAcceleration.getClone());

				    current.setVelocityUpdateStrategy(new GCVelocityUpdateStrategy());
				    
				    newSubSwarm.getTopology().add(current);
				}

				pso.getSubSwarms().add(newSubSwarm);
				pso.getMainSwarm().getInitialisationStrategy().setEntityNumber(pso.getMainSwarm().getTopology().size());
				newSubSwarm.getInitialisationStrategy().setEntityNumber(sortedDistanceList.size() + 1);

				ListIterator<Particle> mi = pso.getMainSwarm().getTopology().listIterator();
				while (mi.hasNext())
				{
				    Particle mainP = (Particle) mi.next();
				    mainP.setNeighbourhoodBest(mainP);
				}

				mi = newSubSwarm.getTopology().listIterator();
				while (mi.hasNext())
				{
				    Particle mainP = (Particle) mi.next();
				    mainP.setNeighbourhoodBest(mainP);
				}
			    }
			}
		    }
		}
	    }
	}
    }

    private Particle getParticleWidID(PSO mainSwarm, String id) {
    	for (Particle particle : mainSwarm.getTopology()) {
    		if (particle.getId().trim().equalsIgnoreCase(id.trim()))
    			return particle;
    	}
    	
    	return null;
	}

	public ControlParameter getThreshold()
    {
	return threshold;
    }

    public void setThreshold(ControlParameter threshold)
    {
	this.threshold = threshold;
    }

    public void setTraceLength(int length)
    {
	this.fitnessTraceLength = length;
    }

    public void setMinimumSubSwarmSize(int size)
    {
	this.minimumSubSwarmSize = size;
    }
}


