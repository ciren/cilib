package net.sourceforge.cilib.entity;

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.type.types.Blackboard;
import net.sourceforge.cilib.type.types.Type;

/**
 * <p>
 * A <code>CandidateSolution</code> is a potential solution within an optimisation.
 * 
 * <p>
 * <code>CandidateSolution</code> is a base class that all concrete <code>Entity</code>
 * instances inherit from. All <code>Entity</code> objects have their respective contents
 * represented and maintained within the <code>Blackboard</code> as defined by the
 * <code>CandidateSolution</code>.
 */
public class CandidateSolutionMixin implements CandidateSolution {
	private static final long serialVersionUID = 4539668687773346284L;
	private final Blackboard<String, Type> properties;
	
	/**
	 * Create the Mixin class, providing the reference to the shared {@linkplain Blackboard}
	 * data structure.
	 * @param properties The shared {@linkplain Blackboard}
	 */
	public CandidateSolutionMixin(Blackboard<String,Type> properties) {
		this.properties = properties;
	}
	
	/**
	 * Instantiate a new instance, based on the provided <code>CandidateSolutionMixin</code>.
	 * This is a shallow copy instantiation. 
	 * @param copy The template object ot copy.
	 */
	public CandidateSolutionMixin(CandidateSolutionMixin copy) {
		this.properties = copy.properties;
	}
	
	/**
	 * Get a clone of the current {@linkplain CandidateSolutionMixin}
	 * @return A shallow clone of the current object - The {@linkplain Blackboard} is not cloned.
	 */
	public CandidateSolutionMixin getClone() {
		return new CandidateSolutionMixin(this);
	}
	
	/**
	 * Get the associated candidate solution representation that the current {@linkplain Entity}
	 * represents.
	 * @return A {@linkplain Type} representing the solution of the {@linkplain Entity}
	 */
	public Type getContents() {
		return properties.get("content");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setContents(Type content) {
		properties.put("content", content);
	}

	/**
	 * {@inheritDoc}
	 */
	public Fitness getFitness() {
		return (Fitness) properties.get("fitness");
	}
	
}
