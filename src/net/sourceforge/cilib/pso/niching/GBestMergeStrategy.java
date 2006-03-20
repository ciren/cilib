package net.sourceforge.cilib.pso.niching;

import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

import net.sourceforge.cilib.algorithm.PopulationBasedAlgorithm;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;

public class GBestMergeStrategy<E extends PopulationBasedAlgorithm> implements MergeStrategy<E> {

	public void merge(Collection<? extends E> name)
	{
			
		for (Iterator<? extends E> i = name.iterator(); i.hasNext(); )
		{
			PSO subSwarm1 = (PSO)i.next();
			Particle gBestParticle1 = subSwarm1.getBestParticle();
			
			for (Iterator<? extends E> j = name.iterator(); j.hasNext(); )
			{
				PSO subSwarm2 = (PSO)j.next();
				
				if( subSwarm1.equals(subSwarm2) == false )
				{
					Particle gBestParticle2 = subSwarm2.getBestParticle();
					
					if(Math.abs(gBestParticle1.getFitness().getValue() - gBestParticle2.getFitness().getValue()) < 0.0001d)
					{
						subSwarm1.getTopology().addAll(subSwarm2.getTopology().getAll());
						subSwarm2 = null;
						
					}
				}				
			}	
		}
	}

}
