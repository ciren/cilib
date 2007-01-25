package net.sourceforge.cilib.pso.positionupdatestrategies;

import java.util.ArrayList;
import java.util.Iterator;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.random.RandomNumber;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.Vector;

/* Implementation of the DE PSO of Hendtlass
 * 
 * @author Andries Engelbrecht
 */

/* TO-DO: can the DE strategies be incorporated somehow?
 * 
 */
public class DEPositionUpdateStrategy implements PositionUpdateStrategy {

	private RandomNumber DEProbability; //Make a parameter to set via xml
	private RandomNumber crossoverProbability;
	private RandomNumber scaleParameter;
	private RandomNumber rand1;
	private RandomNumber rand2;
	private RandomNumber rand3;
	private RandomNumber rand4;
	
	public DEPositionUpdateStrategy() {
       DEProbability = new RandomNumber();
       rand1 = new RandomNumber();
       rand2 = new RandomNumber();
       rand3 = new RandomNumber();
       rand4 = new RandomNumber();
       crossoverProbability = new RandomNumber();
       scaleParameter = new RandomNumber();
	}
	
	public DEPositionUpdateStrategy(DEPositionUpdateStrategy copy) {
		this();
	}
		
	public DEPositionUpdateStrategy clone() {
		return new DEPositionUpdateStrategy(this);
	}
	
	public void updatePosition(Particle particle) {
		Vector position = (Vector) particle.getPosition();
		Vector velocity = (Vector) particle.getVelocity();
			
		if (rand1.getUniform() < DEProbability.getGaussian(0.8,0.1)) {
			particle.set(position.plus(velocity));
		}
		else {
			ArrayList<Vector> positions = new ArrayList<Vector>(3);
			
			//select three random individuals, all different and different from particle
			PSO pso = (PSO) Algorithm.get();
			
			/*Iterator k = pso.getTopology().iterator();
			int counter = 0;
			String particleId = particle.getId();
			Vector pos;
			while (k.hasNext() && (counter < 3)) {
				Particle p = (Particle) k.next();
				if ((p.getId() != particleId) && (rand2.getUniform(0,1) <= 0.5)) { 
					pos = (Vector) p.getPosition();
					positions.add(pos);
					counter++;
				}
			}*/
			
			int count = 0;
			
			while (count < 3) {
				int random = rand2.getRandomGenerator().nextInt(pso.getTopology().size());
				Entity parent = pso.getTopology().get(random);
				if (!positions.contains(parent)) {
					positions.add((Vector) parent.get());
					count++;
				}
			}
	        
			Vector position1 = positions.get(0);
			Vector position2 = positions.get(1);
			Vector position3 = positions.get(2);
				
			Vector DEposition = position.clone();
			int j = (int)rand3.getUniform(0,position.getDimension());
			for (int i = 0; i < position.getDimension(); ++i) {
				if ((rand4.getUniform(0,1) < crossoverProbability.getGaussian(0.5,0.3)) || (j == i)) {
					double value = position1.getReal(i);
					value += scaleParameter.getGaussian(0.7,0.3) * (position2.getReal(i) - position3.getReal(i));   
		    	      
					DEposition.setReal(i, value);
				}
					//else
						//DEposition.setReal(i, )add(new Real(position3.getReal(i)));
			}
			
				
			//position should only become the offspring if its fitness is better
			Fitness trialFitness = pso.getOptimisationProblem().getFitness(DEposition, false);
			Fitness currentFitness = pso.getOptimisationProblem().getFitness(particle.get(), false);
				
			if (trialFitness.compareTo(currentFitness) > 0) {
				particle.set(DEposition);
			}			
		}
	}
}
