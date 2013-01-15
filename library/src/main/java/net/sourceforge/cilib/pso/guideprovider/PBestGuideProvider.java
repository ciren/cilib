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
 * A concrete implementation of {@link GuideProvider} where the personal best
 * position of a particle gets selected as a guide (usually local guide).
 * </p>
 *
 */
public class PBestGuideProvider implements GuideProvider {

    private static final long serialVersionUID = -4639979213818995377L;

    public PBestGuideProvider() {
    }

    public PBestGuideProvider(PBestGuideProvider copy) {
    }

    @Override
    public PBestGuideProvider getClone() {
        return new PBestGuideProvider(this);
    }

    @Override
    public StructuredType get(Particle particle) {
        return particle.getBestPosition();
    }
}
