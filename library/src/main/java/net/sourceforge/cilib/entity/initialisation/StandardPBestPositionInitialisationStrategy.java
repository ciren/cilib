/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.initialisation;

import net.sourceforge.cilib.entity.Property;
import net.sourceforge.cilib.pso.particle.Particle;

/**
 *
 */
public class StandardPBestPositionInitialisationStrategy implements InitialisationStrategy<Particle> {

    private static final long serialVersionUID = 6371324653389143872L;

    public StandardPBestPositionInitialisationStrategy() {
    }

    public StandardPBestPositionInitialisationStrategy(StandardPBestPositionInitialisationStrategy copy) {
    }

    @Override
    public StandardPBestPositionInitialisationStrategy getClone() {
        return new StandardPBestPositionInitialisationStrategy(this);
    }

    @Override
    public void initialise(Property key, Particle entity) {
        entity.put(Property.BEST_POSITION, entity.getPosition().getClone());
    }
}
