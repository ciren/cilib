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
	
	public CandidateSolutionMixin(Blackboard<String,Type> properties) {
		this.properties = properties;
	}
	
	public CandidateSolutionMixin(CandidateSolutionMixin copy) {
		this.properties = copy.properties;
	}
	
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
	
	public void setContents(Type content) {
		properties.put("content", content);
	}

	public Fitness getFitness() {
		return (Fitness) properties.get("fitness");
	}
	
}
