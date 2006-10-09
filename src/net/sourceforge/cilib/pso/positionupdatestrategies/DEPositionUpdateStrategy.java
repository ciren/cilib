package net.sourceforge.cilib.pso.positionupdatestrategies;

import java.util.ArrayList;
import java.util.Iterator;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.math.random.RandomNumber;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.MixedVector;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Vector;

/* Implementation of the DE PSO of Hendtlass
 * 
 * @author Andries Engelbrecht
 */

/* TO-DO: can the DE strategies be incorporated somehow?
 * 
 */
public class DEPositionUpdateStrategy implements PositionUpdateStrategy {

	private double DEProbability; //Make a parameter to set via xml
	private double crossoverProbability;
	private double scaleParameter;
	private RandomNumber rand1;
	private RandomNumber rand2;
	private RandomNumber rand3;
	private RandomNumber rand4;
	
	public DEPositionUpdateStrategy() {
       DEProbability = 0.5;
       rand1 = new RandomNumber();
       rand2 = new RandomNumber();
       rand3 = new RandomNumber();
       rand4 = new RandomNumber();
       crossoverProbability = 0.9;
       scaleParameter = 0.5;
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
			
		RandomNumber rand5 = new RandomNumber();
		if (rand5.getUniform() < DEProbability) {
			particle.set(position.plus(velocity));
		}
		else {
		//	if (rand1.getUniform(0,1) < DEProbability) {
				ArrayList<Vector> positions = new ArrayList<Vector>(3);
			
				//select three random individuals, all different and different from particle
				PSO pso = (PSO) Algorithm.get();
			
				Iterator k = pso.getTopology().iterator();
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
				}
	        
				Vector position1 = positions.get(0);
				Vector position2 = positions.get(1);
				Vector position3 = positions.get(2);
				
				Vector DEposition = new MixedVector(position.getDimension());
				int j = (int)rand3.getUniform(0,position.getDimension());
				for (int i = 0; i < position.getDimension(); ++i) {
					if ((rand4.getUniform(0,1) < crossoverProbability) || (j == i)) {
					   double value = position1.getReal(i);
					   value += scaleParameter * (position2.getReal(i) - position3.getReal(i));   
		    	       
					   //DEposition.setReal(i, value);
					   DEposition.add(new Real(value));
					}
					else
						DEposition.add(new Real(position3.getReal(i)));
				}
				
//				System.out.println(DEposition);
				
				//position should only become the offspring if its fitness is better
				Fitness trialFitness = pso.getOptimisationProblem().getFitness(DEposition, false);
				Fitness currentFitness = pso.getOptimisationProblem().getFitness(particle.get(), false);
				
	//			System.out.println("trial: " + trialFitness.getValue());
		//		System.out.println("current: " + currentFitness.getValue());
				
			//	System.out.println(trialFitness.getClass().getName());
				//System.out.println(currentFitness.getClass().getName());
				if (trialFitness.compareTo(currentFitness) > 0) {
					particle.set(DEposition);
				}
			//}
			
		}
	}
}
