/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.guideprovider;

import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.StructuredType;

public class CurrentPositionGuideProvider implements GuideProvider {

    public GuideProvider getClone() {
        return this;
    }

    public StructuredType get(Particle particle) {
        return particle.getPosition();
    }

}
