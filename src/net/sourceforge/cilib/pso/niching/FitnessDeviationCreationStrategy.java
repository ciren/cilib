package net.sourceforge.cilib.pso.niching;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.cilib.algorithm.PopulationBasedAlgorithm;
import net.sourceforge.cilib.pso.NichePSO;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

public class FitnessDeviationCreationStrategy<E extends PopulationBasedAlgorithm> implements SwarmCreationStrategy<E> {
	
	private List< List<Double> > mainSwarmParticleFitness;
	private int iterationCount;
	
	public FitnessDeviationCreationStrategy() {
		this.mainSwarmParticleFitness = new ArrayList< List<Double> >(3);
	}

	/*
	@SuppressWarnings("unchecked")
	public Collection<E> create(E mainSwarm, Collection<? extends E> subSwarms) {
		int iterationNumber = mainSwarm.getIterations();
		int fitnessCheckInterval = 3;
		
		List<Double> [] mainSwarmParticleFitnesses = new ArrayList[fitnessCheckInterval];
		
		int listIndex = iterationNumber % fitnessCheckInterval;
		
		Iterator mainSwarmIterator = mainSwarm.getTopology().iterator();
			
		while(mainSwarmIterator.hasNext()) {
			Particle mainSwarmParticle = (Particle)mainSwarmIterator.next();
				
			mainSwarmParticleFitnesses[listIndex].add(mainSwarmParticle.getFitness().getValue());
		}
		
		if((iterationNumber % fitnessCheckInterval) == 0) {
			//Iterator mainSwarmIterator = mainSwarm.getTopology().iterator();
			
			//while(mainSwarmIterator.hasNext()) {
				
			//}
		}
		
		else {
			// do nothing (for the time being)
		}
		
		return null;
	}
	*/
	
//	 TODO: Check generics
	public void create(E mainSwarm, Collection<PSO> subSwarms,List<Double> [] mainSwarmParticleFitnesses)
	{
		int mainSwarmIteration = mainSwarm.getIterations();
		
		if( mainSwarmIteration < 3 )
		{
			// populate fitnesses
			Iterator mainSwarmIterator = mainSwarm.getTopology().iterator();
	        
	        while(mainSwarmIterator.hasNext())
	        {
	            Particle mainSwarmParticle = (Particle)mainSwarmIterator.next();
	            mainSwarmParticleFitnesses[mainSwarmIteration].add(mainSwarmParticle.getFitness().getValue());
	        }
		}
		else // = 3
		{
			mainSwarmIteration = 0;
			for( int i = 0; i < 3; i++ )
				mainSwarmParticleFitnesses[i].clear();
				
			// perform creation of subwarms
						
			for( int j = 0; j < mainSwarm.getTopology().size(); j++ )
			{
				double total = 0.0d;
				for( int i = 0; i < 3; i++ )
					total += mainSwarmParticleFitnesses[i].get(j);
				
				double ave = total / 3.0d;
								
				double stdDev = 0.0d;
				for( int x = 0; x < 3; x++ )
					stdDev += Math.pow(mainSwarmParticleFitnesses[x].get(j) - ave, 2.0d) / 3.0d;
				
				//	 check if last iteration value is correct
				if( mainSwarmParticleFitnesses[2].get(j) < stdDev )
				{
					// find nearest neighbor
					Particle p = (Particle)mainSwarm.getTopology().get(j);
					double minDistance = Double.MAX_VALUE;
					Particle minDistanceParticle = null;
					
					Iterator mainSwarmIterator = mainSwarm.getTopology().iterator();
			        while(mainSwarmIterator.hasNext())
			        {
			            Particle mainSwarmParticle = (Particle)mainSwarmIterator.next();
			            if( p.getId() != mainSwarmParticle.getId() )
			            {
			            	DistanceMeasure distanceMeasure = new EuclideanDistanceMeasure();
			                double distance = distanceMeasure.distance((Vector)p.getPosition(), (Vector)mainSwarmParticle.getPosition());
			                
			                if( distance <= minDistance )
			                {
			                	minDistance = distance;
			                	minDistanceParticle = mainSwarmParticle;
			                }
			            }
			        }
			        
			        // create new subswarm
			        PSO newSubSwarm = new PSO();
			        newSubSwarm.getTopology().add(p);
			        newSubSwarm.getTopology().add(minDistanceParticle);
			        newSubSwarm.addStoppingCondition(mainSwarm.getStoppingConditions().elementAt(0));
			        newSubSwarm.setOptimisationProblem(mainSwarm.getOptimisationProblem());
			        newSubSwarm.initialise();
			        
			        subSwarms.add(newSubSwarm);
				}
			}
				
			
		}
	}

	public void create(NichePSO pso) {
		
		//int mainSwarmIteration = pso.getMainSwarm().getIterations();
		//int mainSwarmIteration = pso.getIterations();
		this.iterationCount += 1;
		//System.out.println("iterationCount: " + iterationCount);
		
		if( iterationCount <= 3 )
		{
			// populate fitnesses
			Iterator mainSwarmIterator = pso.getMainSwarm().getTopology().iterator();
			List<Double> currentIterationFitness = new ArrayList<Double>();
	        
	        while(mainSwarmIterator.hasNext())
	        {
	            Particle mainSwarmParticle = (Particle)mainSwarmIterator.next();
	            //mainSwarmParticleFitnesses[mainSwarmIteration].add(mainSwarmParticle.getFitness().getValue());
	            currentIterationFitness.add(mainSwarmParticle.getFitness().getValue());
	        }
	        
	        this.mainSwarmParticleFitness.add(currentIterationFitness);
	        //System.out.println("mainSwarmfitness: " + this.mainSwarmParticleFitness.size());
		}
		else // = 3
		{
			iterationCount = 0;
			/*mainSwarmIteration = 0;
			for( int i = 0; i < 3; i++ )
				mainSwarmParticleFitness.clear();*/
				
			// perform creation of subwarms
						
			for( int j = 0; j < pso.getMainSwarm().getTopology().size(); j++ )
			{
				double total = 0.0d;
				for( int i = 0; i < 3; i++ ) {
					//System.out.println(i);
					//System.out.println(this.mainSwarmParticleFitness.get(i).get(j));
					total += this.mainSwarmParticleFitness.get(i).get(j);
				}
				
				double ave = total / 3.0d;
								
				double stdDev = 0.0d;
				for( int x = 0; x < 3; x++ )
					stdDev += Math.pow(mainSwarmParticleFitness.get(x).get(j) - ave, 2.0d) / 3.0d;
				
				//	 check if last iteration value is correct
				if( mainSwarmParticleFitness.get(2).get(j) < stdDev )
				{
					// find nearest neighbor
					Particle p = (Particle) pso.getMainSwarm().getTopology().get(j);
					double minDistance = Double.MAX_VALUE;
					Particle minDistanceParticle = null;
					
					Iterator mainSwarmIterator = pso.getMainSwarm().getTopology().iterator();
			        while(mainSwarmIterator.hasNext())
			        {
			            Particle mainSwarmParticle = (Particle)mainSwarmIterator.next();
			            if( p.getId() != mainSwarmParticle.getId() )
			            {
			            	DistanceMeasure distanceMeasure = new EuclideanDistanceMeasure();
			                double distance = distanceMeasure.distance((Vector)p.getPosition(), (Vector)mainSwarmParticle.getPosition());
			                
			                if( distance <= minDistance )
			                {
			                	minDistance = distance;
			                	minDistanceParticle = mainSwarmParticle;
			                }
			            }
			        }
			        
			        // create new subswarm
			        PSO newSubSwarm = new PSO();
			        //System.out.println("Integer.parseInt(p.getId()): " + (pso.getMainSwarm().getTopology().size() - Integer.parseInt(p.getId())));
			        //pso.getMainSwarm().getTopology().remove(Integer.parseInt(p.getId()));
			        //pso.getMainSwarm().getTopology().remove(Integer.parseInt(minDistanceParticle.getId()));
			        pso.getMainSwarm().getTopology().remove(p);
			        pso.getMainSwarm().getTopology().remove(minDistanceParticle);
			        newSubSwarm.getTopology().add(p);
			        newSubSwarm.getTopology().add(minDistanceParticle);
			        //newSubSwarm.addStoppingCondition(pso.getMainSwarm().getStoppingConditions().elementAt(0));
			        newSubSwarm.setOptimisationProblem(pso.getMainSwarm().getOptimisationProblem());
			        //newSubSwarm.initialise();
			        
			        pso.getSubSwarms().add(newSubSwarm);
				}
			}
				
			mainSwarmParticleFitness.clear();
		}
		
	}

}
