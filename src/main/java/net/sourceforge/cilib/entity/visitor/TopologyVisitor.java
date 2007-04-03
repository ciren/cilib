package net.sourceforge.cilib.entity.visitor;

import net.sourceforge.cilib.container.visitor.Visitor;
import net.sourceforge.cilib.entity.Topology;

public abstract class TopologyVisitor extends Visitor<Topology> {
	
	protected double result = 0.0;

	public abstract void visit(Topology algorithm);
	
	public double getResult() {
		return result;
	}

}
