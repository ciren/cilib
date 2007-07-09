package net.sourceforge.cilib.entity.visitor;

import java.util.Iterator;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

public class DiameterVisitor extends TopologyVisitor {
	
	private DistanceMeasure distance;
	
	public DiameterVisitor() {
		this.distance = new EuclideanDistanceMeasure();
	}

	@Override
	public void visit(Topology topology) {
		double maxDistance = 0.0;
		
    	Iterator k1 = topology.iterator();
        while (k1.hasNext()) {
            Entity p1 = (Entity) k1.next();
        	Vector position1 = (Vector) p1.getContents();
           	
        	Iterator k2 = topology.iterator();
        	while (k2.hasNext()) {
        		Entity p2 = (Entity) k2.next();
        		Vector position2 = (Vector) p2.getContents();

        		double actualDistance = distance.distance(position1,position2);
        		if (actualDistance > maxDistance)
        			maxDistance = actualDistance;
        	}
        }
        
        result = maxDistance;
	}

}