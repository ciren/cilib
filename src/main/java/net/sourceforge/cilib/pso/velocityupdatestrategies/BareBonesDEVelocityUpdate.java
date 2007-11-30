package net.sourceforge.cilib.pso.velocityupdatestrategies;

import java.util.List;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.RandomizingControlParameter;
import net.sourceforge.cilib.ec.iterationstrategies.DifferentialEvolutionIterationStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.RandomNumber;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *  The <tt>VelocityUpdateStrategy</tt> strategy which uses a DE strategy
 *  where the trial vector is the bare bones attractor point.
 * 
 *  TODO: To be published by Omran and Engelbrecht
 * 
 *  @author Andries Engelbrecht
 */

public class BareBonesDEVelocityUpdate implements VelocityUpdateStrategy {
	private static final long serialVersionUID = -8781011210069055197L;
	
	private RandomNumber rand1;
	private RandomNumber rand2;
	private RandomNumber rand3;
	private ControlParameter cognitive;
	private ControlParameter social;

	private ControlParameter crossoverProbability;
	

	public BareBonesDEVelocityUpdate() {
		
		rand1 = new RandomNumber();
		rand2 = new RandomNumber();
		rand3 = new RandomNumber();
		cognitive = new RandomizingControlParameter();
		social = new RandomizingControlParameter();
		crossoverProbability = new ConstantControlParameter(0.5);
		
		cognitive.setParameter(1);
		social.setParameter(1);
	}
	
	
	public BareBonesDEVelocityUpdate(BareBonesDEVelocityUpdate copy) {
		this();
		
		cognitive.setParameter(copy.cognitive.getParameter());
		social.setParameter(copy.social.getParameter());
		crossoverProbability.setParameter(copy.crossoverProbability.getParameter());
	}
	
	
	public BareBonesDEVelocityUpdate getClone() {
		return new BareBonesDEVelocityUpdate(this);
	}
	
	
	public void updateVelocity(Particle particle) {
		Vector personalBestPosition = (Vector) particle.getBestPosition();
		Vector nBestPosition = (Vector) particle.getNeighbourhoodBest().getBestPosition();
		Vector velocity = (Vector) particle.getVelocity();
		
		PSO pso = (PSO) Algorithm.get();
		List<Entity> positions = DifferentialEvolutionIterationStrategy.getRandomParentEntities(pso.getTopology());
		
		//select three random individuals, all different and different from particle
		RandomNumber r1 = new RandomNumber();
        
        Vector position1 = (Vector) positions.get(0).getContents();
        Vector position2 = (Vector) positions.get(1).getContents();
//        Vector position3 = (Vector) positions.get(2).getContents();
        for (int i = 0; i < particle.getDimension(); ++i) {
        	double r = r1.getUniform(0,1);
        	double attractor = r*personalBestPosition.getReal(i)+(1-r)*nBestPosition.getReal(i);
        	double stepSize = rand3.getUniform(0,1) * (position1.getReal(i)-position2.getReal(i));
        	
        	if (rand2.getUniform(0,1) > crossoverProbability.getParameter())
        		velocity.setReal(i,attractor + stepSize);
        	else
        		velocity.setReal(i,((Vector) particle.getPosition()).getReal(i));//position3.getReal(i));
        }
	}


	public void updateControlParameters(Particle particle) {
		// TODO Auto-generated method stub
		
	}
	
	
	public RandomNumber getRand1() {
		return rand1;
	}


	public void setRand1(RandomNumber rand1) {
		this.rand1 = rand1;
	}


	public RandomNumber getRand2() {
		return rand2;
	}


	public void setRand2(RandomNumber rand2) {
		this.rand2 = rand2;
	}


	public RandomNumber getRand3() {
		return rand3;
	}


	public void setRand3(RandomNumber rand3) {
		this.rand3 = rand3;
	}


	public ControlParameter getCognitive() {
		return cognitive;
	}


	public void setCognitive(ControlParameter cognitive) {
		this.cognitive = cognitive;
	}


	public ControlParameter getSocial() {
		return social;
	}


	public void setSocial(ControlParameter social) {
		this.social = social;
	}


	public ControlParameter getCrossoverProbability() {
		return crossoverProbability;
	}


	public void setCrossoverProbability(ControlParameter crossoverProbability) {
		this.crossoverProbability = crossoverProbability;
	}

}
