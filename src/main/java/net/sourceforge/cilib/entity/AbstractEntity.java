package net.sourceforge.cilib.entity;

import net.sourceforge.cilib.type.types.Blackboard;
import net.sourceforge.cilib.type.types.Type;

public abstract class AbstractEntity implements Entity {
	
	protected Blackboard<String, Type> properties = new Blackboard<String, Type>();
	
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
