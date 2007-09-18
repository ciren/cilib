package net.sourceforge.cilib.pso.velocityupdatestrategies;

import java.util.ArrayList;
import java.util.Iterator;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.controlparameterupdatestrategies.ControlParameterUpdateStrategy;
import net.sourceforge.cilib.controlparameterupdatestrategies.RandomisedParameterUpdateStrategy;
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
	private ControlParameterUpdateStrategy cognitive;
	private ControlParameterUpdateStrategy social;
	

	public BareBonesDEVelocityUpdate() {
		
		rand1 = new RandomNumber();
		rand2 = new RandomNumber();
		rand3 = new RandomNumber();
		cognitive = new RandomisedParameterUpdateStrategy();
		social = new RandomisedParameterUpdateStrategy();
		
		cognitive.setParameter(1);
		social.setParameter(1);
	}
	
	
	public BareBonesDEVelocityUpdate(BareBonesDEVelocityUpdate copy) {
		this();
		
		cognitive.setParameter(copy.cognitive.getParameter());
		social.setParameter(copy.social.getParameter());
	}
	
	
	public BareBonesDEVelocityUpdate clone() {
		return new BareBonesDEVelocityUpdate(this);
	}
	
	
	public void updateVelocity(Particle particle) {
		Vector personalBestPosition = (Vector) particle.getBestPosition();
		Vector nBestPosition = (Vector) particle.getNeighbourhoodBest().getBestPosition();
		Vector velocity = (Vector) particle.getVelocity();
		ArrayList<Vector> positions = new ArrayList<Vector>(3);
		
		//select three random individuals, all different and different from particle
		RandomNumber r1 = new RandomNumber();
		PSO pso = (PSO) Algorithm.get(); 
		
    	Iterator<Particle> k = pso.getTopology().iterator();
    	int counter = 0;
    	String particleId = particle.getId();
    	Vector position;
        while (k.hasNext() && (counter < 3)) {
            Particle p = k.next();
            if ((p.getId() != particleId) && (rand1.getUniform(0,1) <= 0.5)) { 
            	position = (Vector) p.getPosition();
            	positions.add(counter,position);
            	counter++;
            }
        }
        
        Vector position1 = positions.get(0);
        Vector position2 = positions.get(1);
        Vector position3 = positions.get(2);
        for (int i = 0; i < particle.getDimension(); ++i) {
        	double r = r1.getUniform(0,1);
        	double attractor = r*personalBestPosition.getReal(i)+(1-r)*nBestPosition.getReal(i);
        	double stepSize = rand3.getUniform(0,1) * (position1.getReal(i)-position2.getReal(i));
        	
        	if (rand2.getUniform(0,1) > 0.5)
        		velocity.setReal(i,attractor + stepSize);
        	else
        		velocity.setReal(i,position3.getReal(i));
        }
	}


	public void updateControlParameters() {
		// TODO Auto-generated method stub
		
	}

}
