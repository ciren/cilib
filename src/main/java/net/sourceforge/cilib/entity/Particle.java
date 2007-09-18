package net.sourceforge.cilib.entity;

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.pso.positionupdatestrategies.PositionUpdateStrategy;
import net.sourceforge.cilib.pso.velocityupdatestrategies.VelocityUpdateStrategy;
import net.sourceforge.cilib.type.types.Type;

public interface Particle extends Entity {
	
	public Particle clone();
	
	public Type getPosition();
	
	public Type getBestPosition();
	
	public Type getVelocity();
	
	public Particle getNeighbourhoodBest();
	
	public void setNeighbourhoodBest(Particle particle);
	
	public int getDimension();
	
	public Fitness getFitness();
	
	public Fitness getBestFitness();
	
	public Fitness getSocialBestFitness();
	
	public void updatePosition();
	
	public void updateVelocity();
	
	public VelocityUpdateStrategy getVelocityUpdateStrategy();
	
	public void setVelocityUpdateStrategy(VelocityUpdateStrategy velocityUpdateStrategy);
	
	public PositionUpdateStrategy getPositionUpdateStrategy();
	
	public void setPositionUpdateStrategy(PositionUpdateStrategy positionUpdateStrategy);
	
	// I don't like this mehtod / idea
	public String getId();
	
	// Don't like this method
	public Particle getDecorator(Class<?> decorator);

}
