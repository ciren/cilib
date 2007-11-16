/**
 * 
 */
package net.sourceforge.cilib.measurement.single.diversity;

import java.util.Iterator;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @author Olusegun Olorunda
 * 
 * This extends the concept of diversity around the population center by evaluating the average distance
 * around each entity in the population, i.e. each population entity is used as a center, and then
 * calculating the average over all these distances.
 *
 */
public class AverageDiversityAroundAllEntities extends Diversity {
	
	public Type getValue() {
		
		PopulationBasedAlgorithm algorithm = (PopulationBasedAlgorithm) Algorithm.get();
		int numberOfEntities = algorithm.getPopulationSize();
		
		Iterator<? extends Entity> populationCenterIterator = algorithm.getTopology().iterator();
		
		double totalDistanceSum = 0.0;
		
		while (populationCenterIterator.hasNext()) {
			Vector currentCenter = (Vector) (((Entity) populationCenterIterator.next()).getContents());
			Iterator<? extends Entity> populationIterator = algorithm.getTopology().iterator();
			double currentDistanceSum = 0.0;
			
			while (populationIterator.hasNext()) {
				Vector currentEntityPosition = (Vector) (((Entity) populationIterator.next()).getContents());
				currentDistanceSum += distanceMeasure.distance(currentCenter, currentEntityPosition);
			}
			
			totalDistanceSum += currentDistanceSum/numberOfEntities;
		}
		
		totalDistanceSum /= numberOfEntities;
		
		normalisationParameter.setDistanceMeasure(distanceMeasure);
		totalDistanceSum /= normalisationParameter.getValue();
		
		return new Real(totalDistanceSum);
	}

}
