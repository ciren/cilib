package net.sourceforge.cilib.entity.operators.general;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.operators.Operator;

public class TopologyLoopingOperator implements Operator {
	
	private Operator operator;
	
	public TopologyLoopingOperator() {
		
	}
	
	public TopologyLoopingOperator(TopologyLoopingOperator copy) {
		this.operator = copy.operator.clone();
	}
	
	public TopologyLoopingOperator clone() {
		return new TopologyLoopingOperator(this);
	}

	public void performOperation(Topology<? extends Entity> topology, Topology<Entity> offspring) {
		if (operator == null)
			throw new RuntimeException("Cannot perform a loop over the topology. The operator to apply has not been defined");
		
		for (int i = 0; i < topology.size(); i++)
			operator.performOperation(topology, offspring);
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}
	
}
