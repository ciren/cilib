package net.sourceforge.cilib.entity.visitor;

import java.util.Iterator;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.type.types.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

public class RadiusVisitor extends TopologyVisitor {
	
	private DistanceMeasure distanceMeasure;
	
	public RadiusVisitor() {
		this.distanceMeasure = new EuclideanDistanceMeasure();
	}

	@Override
	public void visit(Topology topology) {
		double maxDistance = 0.0;
    	
		PopulationBasedAlgorithm algorithm = (PopulationBasedAlgorithm) Algorithm.get();
    	Vector swarmBestParticlePosition = (Vector) algorithm.getBestSolution().getPosition();
    	Iterator swarmIterator = topology.iterator();
    	    	
    	while(swarmIterator.hasNext()) {
    		Entity swarmParticle = (Entity) swarmIterator.next();
    		Vector swarmParticlePosition = (Vector) swarmParticle.get();
    			
    		double actualDistance = distanceMeasure.distance(swarmBestParticlePosition, swarmParticlePosition);
    	
    		if (actualDistance > maxDistance)
    			maxDistance = actualDistance;
    	}
    	
    	result = maxDistance;
	}

	public DistanceMeasure getDistanceMeasure() {
		return distanceMeasure;
	}

	public void setDistanceMeasure(DistanceMeasure distanceMeasure) {
		this.distanceMeasure = distanceMeasure;
	}

}
