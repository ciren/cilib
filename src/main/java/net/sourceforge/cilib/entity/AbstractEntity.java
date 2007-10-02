package net.sourceforge.cilib.entity;

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.types.Blackboard;
import net.sourceforge.cilib.type.types.Type;

public abstract class AbstractEntity implements Entity {
	
	protected Blackboard<String, Type> properties = new Blackboard<String, Type>();
	
	public abstract Entity clone();

	public abstract void calculateFitness();
	
	public abstract void calculateFitness(boolean count);
	
	public abstract int compareTo(Entity o);

	public abstract Type getContents();

	public abstract int getDimension();

	public abstract Fitness getFitness();

	public abstract void initialise(OptimisationProblem problem);

	public abstract void reinitialise();
	
	public abstract void setContents(Type type);
	
	/**
	 * Get the properties associate with the <code>Entity</code>
	 * @return
	 */
	public Blackboard<String, Type> getProperties() {
		return properties;
	}

	/**
	 * Set the properties for the current <code>Entity</code>
	 * @param properties
	 */
	public void setProperties(Blackboard<String, Type> properties) {
		this.properties = properties;
	}
	
}
