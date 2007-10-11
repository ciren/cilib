package net.sourceforge.cilib.pso.niching;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.visitor.TopologyVisitor;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.MixedVector;
import net.sourceforge.cilib.type.types.container.Vector;

public class AveragePositionVisitor extends TopologyVisitor {
	
	private Vector averageVector;
	
	public AveragePositionVisitor() {
		this.averageVector = new MixedVector();
	}

	public void visit(Topology<? extends Entity> topology) {
		averageVector.initialise(topology.get(0).getDimension(), new Real(0.0));
		
		for (Entity entity : topology)
			averageVector = averageVector.plus((Vector) entity.getContents());
		
		averageVector.divide(topology.get(0).getDimension());
	}

	public Vector getAveragePosition() {
		return averageVector;
	}

	
}
