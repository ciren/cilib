package net.sourceforge.cilib.entity;

import java.io.Serializable;

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.util.Cloneable;

public interface CandidateSolution extends Serializable, Cloneable {
	
	public Type getContents();
	
	public void setContents(Type contents);
	
	public Fitness getFitness();

}
