/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.initialization;

import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;

/**
 *
 */
public class StandardPBestPositionInitializationStrategy implements InitializationStrategy<Particle> {

    private static final long serialVersionUID = 6371324653389143872L;

    public StandardPBestPositionInitializationStrategy() {
    }

    public StandardPBestPositionInitializationStrategy(StandardPBestPositionInitializationStrategy copy) {
    }

    @Override
    public StandardPBestPositionInitializationStrategy getClone() {
        return new StandardPBestPositionInitializationStrategy(this);
    }

    @Override
    public void initialize(Enum<?> key, Particle entity) {
        entity.getProperties().put(EntityType.Particle.BEST_POSITION, entity.getPosition().getClone());
    }
}
