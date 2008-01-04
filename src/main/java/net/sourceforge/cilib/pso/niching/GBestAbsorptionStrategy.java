/*
 * GBestAbsorptionStrategy.java
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

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.container.Pair;
import net.sourceforge.cilib.container.SortedList;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.RandomizingControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.visitor.RadiusVisitor;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.velocityupdatestrategies.StandardVelocityUpdate;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * 
 * @author Segun
 * 
 */

public class GBestAbsorptionStrategy<E extends PopulationBasedAlgorithm> implements AbsorptionStrategy<E>
{
	private Map<PopulationBasedAlgorithm, Double> subSwarmMinRadius;
	
	public GBestAbsorptionStrategy() {
		this.subSwarmMinRadius = new HashMap<PopulationBasedAlgorithm, Double>();
	}

    public void absorb(E mainSwarm, List<PopulationBasedAlgorithm> subSwarms)
    {
    	if (subSwarms.isEmpty()) return;
    	
    	Iterator<? extends Entity> mainSwarmIterator = mainSwarm.getTopology().iterator();
    	while (mainSwarmIterator.hasNext()) {
    		Entity mainSwarmEntity = mainSwarmIterator.next();
			SortedList<Pair<Real, PopulationBasedAlgorithm>> distances = determineRadiusToSubSwarms(mainSwarmEntity, subSwarms);
    		
    		Pair<Real, PopulationBasedAlgorithm> tmp = distances.get(0);
    		PSO tmpPSO = (PSO) tmp.getValue();
			double subSwarmRadius = getSmallestRadius(tmpPSO);
    		
    		if (tmp.getKey().getReal() < subSwarmRadius) {
    			Particle mainSwarmParticle = (Particle) mainSwarmEntity;
    		    mainSwarmIterator.remove();
    			
    		    tmpPSO.getTopology().add(mainSwarmParticle);
	
			    RandomizingControlParameter socialAcceleration = new RandomizingControlParameter();
			    socialAcceleration.setControlParameter(new ConstantControlParameter(1.2));
			    ((StandardVelocityUpdate) mainSwarmParticle.getVelocityUpdateStrategy()).setSocialAcceleration(socialAcceleration);
	
			    mainSwarm.getInitialisationStrategy().setEntityNumber(mainSwarm.getTopology().size());
			    tmpPSO.getInitialisationStrategy().setEntityNumber(tmpPSO.getTopology().size());
//			    //System.out.println("absorbed - D:" + distance + " , R: " + subSwarmRadius + " , PID: " + mainSwarmParticle.getId());
			    continue;
    		}
    	}
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
/*    	ListIterator<? extends Entity> mainSwarmIterator = mainSwarm.getTopology().listIterator();
    	while (mainSwarmIterator.hasNext())
    	{
    		Particle mainSwarmParticle = (Particle) mainSwarmIterator.next();

    		Iterator<PopulationBasedAlgorithm> subSwarmsIterator = subSwarms.iterator();
		    while (subSwarmsIterator.hasNext())
		    {
				PSO subSwarm = (PSO) subSwarmsIterator.next();
		    	double subSwarmRadius = getSmallestRadius(subSwarm); //subSwarm.getRadiusFromGbest();
		
				Particle subSwarmBestParticle = subSwarm.getBestParticle();
				Vector subSwarmBestParticlePosition = (Vector) subSwarmBestParticle.getPosition();
				Vector mainSwarmParticlePosition = (Vector) mainSwarmParticle.getPosition();
		
				DistanceMeasure distanceMeasure = new EuclideanDistanceMeasure();
				double distance = distanceMeasure.distance(subSwarmBestParticlePosition, mainSwarmParticlePosition);
		
				//System.out.println("Dis: " + distance + " Sub: " + subSwarmRadius);
				if (distance <= subSwarmRadius)
				{
				    mainSwarmIterator.remove();
		
				    subSwarm.getTopology().add(mainSwarmParticle);
		
				    RandomizingControlParameter socialAcceleration = new RandomizingControlParameter();
				    socialAcceleration.setControlParameter(new ConstantControlParameter(1.2));
				    ((StandardVelocityUpdate) mainSwarmParticle.getVelocityUpdateStrategy()).setSocialAcceleration(socialAcceleration);
		
				    mainSwarm.getInitialisationStrategy().setEntityNumber(mainSwarm.getTopology().size());
				    subSwarm.getInitialisationStrategy().setEntityNumber(subSwarm.getTopology().size());
				    //System.out.println("absorbed - D:" + distance + " , R: " + subSwarmRadius + " , PID: " + mainSwarmParticle.getId());
				    break;
				}
	
		    }
	
		}*/

    }

	private SortedList<Pair<Real, PopulationBasedAlgorithm>> determineRadiusToSubSwarms(Entity mainSwarmEntity, List<PopulationBasedAlgorithm> subSwarms) {
		final Comparator<Pair<Real, PopulationBasedAlgorithm>> comparator = new Comparator<Pair<Real,PopulationBasedAlgorithm>>() {
			public int compare(Pair<Real, PopulationBasedAlgorithm> o1,
					Pair<Real, PopulationBasedAlgorithm> o2) {
				return o1.getKey().compareTo(o2.getKey());
			}
		};
		
		SortedList<Pair<Real, PopulationBasedAlgorithm>> list = new SortedList<Pair<Real, PopulationBasedAlgorithm>>(comparator);
		DistanceMeasure distanceMeasure = new EuclideanDistanceMeasure();
		
		for (PopulationBasedAlgorithm subSwarm : subSwarms) {
			PSO pso = (PSO) subSwarm;
			double distance = distanceMeasure.distance((Vector) mainSwarmEntity.getContents(), (Vector) pso.getBestEntity().getPosition());
			Pair<Real, PopulationBasedAlgorithm> pair = new Pair<Real, PopulationBasedAlgorithm>(new Real(distance), subSwarm);
			list.add(pair);
		}
		
		return list;
	}

	private double getSmallestRadius(PSO subSwarm) {
		double currentRadius;
		
		RadiusVisitor radiusVisitor = new RadiusVisitor();
		subSwarm.accept(radiusVisitor);
		
		Double current = this.subSwarmMinRadius.get(subSwarm);
		
		if (current == null) {
			this.subSwarmMinRadius.put(subSwarm, radiusVisitor.getResult());
			currentRadius = Double.MAX_VALUE;
		}
		else
			currentRadius = current;
		
		double smallestRadius = Math.min(currentRadius, radiusVisitor.getResult());
		
		this.subSwarmMinRadius.put(subSwarm, smallestRadius);
		
		return smallestRadius;
	}

}
