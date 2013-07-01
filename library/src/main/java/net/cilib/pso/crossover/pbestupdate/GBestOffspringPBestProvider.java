/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.pso.crossover.pbestupdate;

import java.util.List;
import net.cilib.algorithm.AbstractAlgorithm;
import net.cilib.pso.particle.Particle;
import net.cilib.type.types.container.StructuredType;
import net.cilib.type.types.container.Vector;

/**
 * This OffspringPBestProvider sets an offspring's pBest to the gBest.
 */
public class GBestOffspringPBestProvider extends OffspringPBestProvider {
    @Override
    public StructuredType f(List<Particle> parent, Particle offspring) {
        return (Vector) AbstractAlgorithm.get().getBestSolution().getPosition();
    }
}
