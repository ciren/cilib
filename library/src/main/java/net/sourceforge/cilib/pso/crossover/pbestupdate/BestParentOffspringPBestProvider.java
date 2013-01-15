/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.crossover.pbestupdate;

import java.util.List;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.util.selection.recipes.ElitistSelector;

/**
 * This OffspringPBestProvider sets an offspring's pBest to the best parent's pBest.
 */
public class BestParentOffspringPBestProvider extends OffspringPBestProvider {
    @Override
    public StructuredType f(List<Particle> parents, Particle offspring) {
        return new ElitistSelector<Particle>().on(parents).select().getBestPosition();
    }
}
