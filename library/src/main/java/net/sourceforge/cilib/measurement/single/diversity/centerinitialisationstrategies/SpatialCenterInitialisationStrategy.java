/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single.diversity.centerinitialisationstrategies;

import java.util.Iterator;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Returns the center of a given topology where the center is the average position
 * of all entities in the topology.
 */
public class SpatialCenterInitialisationStrategy implements CenterInitialisationStrategy {

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getCenter(fj.data.List<? extends Entity> topology) {
        int numberOfEntities = topology.length();
        Iterator<? extends Entity> averageIterator = topology.iterator();
        Entity entity = averageIterator.next();
        Vector averageEntityPosition = (Vector) entity.getPosition().getClone();

        while (averageIterator.hasNext()) {
            entity = averageIterator.next();
            Vector entityContents = (Vector) entity.getPosition();
            averageEntityPosition = averageEntityPosition.plus(entityContents);
        }

        return averageEntityPosition.divide(numberOfEntities);
    }
}
