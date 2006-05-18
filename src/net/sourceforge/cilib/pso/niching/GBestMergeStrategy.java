/*
 * GBestMergeStrategy.java
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

import java.util.List;

import net.sourceforge.cilib.algorithm.PopulationBasedAlgorithm;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;

/**
 * 
 * @author Edrich van Loggerenberg
 * @author Shegen
 * @author Gary Pampara
 */
public class GBestMergeStrategy<E extends PopulationBasedAlgorithm> implements MergeStrategy<E> {
	
	private double threshold;
	
	
	public GBestMergeStrategy() {
		this.threshold = 0.1;
	}

	
	public void merge(List<? extends E> subSwarms)
	{
		//for (ListIterator<? extends E> i = subSwarms.listIterator(); i.hasNext(); )	{
		for (int i = 0; i < subSwarms.size(); i++) {
			System.out.println("subSwarmsire: " + subSwarms.size());
			PSO subSwarm1 = (PSO) subSwarms.get(i);
			Particle gBestParticle1 = subSwarm1.getBestParticle();
			
			//for (ListIterator<? extends E> j = subSwarms.listIterator(); j.hasNext(); )	{
			for (int j = 0; j < subSwarms.size(); j++) {
				PSO subSwarm2 = (PSO) subSwarms.get(j);
				
				if (!subSwarm1.equals(subSwarm2) && subSwarm1 != subSwarm2) // do not compare with itself
				{
					Particle gBestParticle2 = subSwarm2.getBestParticle();
					
					if(Math.abs(gBestParticle1.getFitness().getValue() - gBestParticle2.getFitness().getValue()) < threshold)
					{
						subSwarm1.getTopology().addAll(subSwarm2.getTopology());
						//j.remove();
						subSwarms.remove(j);
						subSwarm2 = null; // the two swarms are now merged, so delete the one
					}
				}				
			}	
		}
	}
	
	
	public void setThreshold(double t) {
		this.threshold = t;
	}
		
	public double getThreshold() {
		return this.threshold;
	}

}
