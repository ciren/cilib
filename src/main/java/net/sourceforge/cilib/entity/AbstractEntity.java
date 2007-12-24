package net.sourceforge.cilib.entity;

import java.util.Map;

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.type.types.Blackboard;
import net.sourceforge.cilib.type.types.Type;

public abstract class AbstractEntity implements Entity, CandidateSolution {
	
	protected Blackboard<String, Type> properties = new Blackboard<String, Type>();
	private final CandidateSolution candidateSolution;
	
	public AbstractEntity() {
		this.candidateSolution = new CandidateSolutionMixin(properties);		
	}
	
	public AbstractEntity(AbstractEntity copy) {
		this();
		
		for (Map.Entry<String, Type> entry : copy.properties.entrySet()) {
        	String key = entry.getKey().toString();
    		this.properties.put(key, entry.getValue().getClone());
        }
	}
	
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

	public Type getContents() {
		return this.candidateSolution.getContents();
	}

	public Fitness getFitness() {
		return this.candidateSolution.getFitness();
	}

	public void setContents(Type contents) {
		this.candidateSolution.setContents(contents);
	}
	
}
