/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.crossover.parentupdate;

import fj.F2;
import java.util.List;
import net.sourceforge.cilib.pso.particle.Particle;

/**
 * These classes determine which particles replace the parents after particle crossover.
 */
public abstract class ParentReplacementStrategy extends F2<List<Particle>, List<Particle>, List<Particle>> {
    @Override
    public abstract List<Particle> f(List<Particle> parents, List<Particle> offspring);
}
