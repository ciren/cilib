/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.guideprovider;

import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.StructuredType;

/**
 * <p>
 * A concrete implementation of {@link GuideProvider} where the neighbourhood
 * best position of a particle gets selected as a guide (usually global guide).
 * </p>
 *
 */
public class NBestGuideProvider implements GuideProvider {

    private static final long serialVersionUID = 6770044000445220658L;

    public NBestGuideProvider() {
    }

    public NBestGuideProvider(NBestGuideProvider copy) {
    }

    @Override
    public NBestGuideProvider getClone() {
        return new NBestGuideProvider(this);
    }

    @Override
    public StructuredType get(Particle particle) {
        return particle.getNeighbourhoodBest().getBestPosition();
    }
}
