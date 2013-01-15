/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.crossover.operations;

import fj.F;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.util.Cloneable;

/**
 * These classes perform different functions used with the PSOCrossoverIterationStrategy.
 */
public abstract class PSOCrossoverOperation extends F<PSO, Topology<Particle>> implements Cloneable {
    @Override
    public abstract Topology<Particle> f(PSO pso);

    @Override
    public abstract PSOCrossoverOperation getClone();
}
