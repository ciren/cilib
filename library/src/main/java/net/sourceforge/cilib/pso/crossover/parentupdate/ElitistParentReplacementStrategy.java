/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.crossover.parentupdate;

import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.util.selection.Samples;
import net.sourceforge.cilib.util.selection.recipes.ElitistSelector;

/**
 * This ParentReplacementStrategy selects the best particles from both the parents
 * and the offspring.
 */
public class ElitistParentReplacementStrategy extends ParentReplacementStrategy {
    @Override
    public List<Particle> f(List<Particle> parents, List<Particle> offspring) {
        List<Particle> joined = Lists.newArrayList(parents);
        joined.addAll(offspring);

        return new ElitistSelector<Particle>().on(joined).select(Samples.first(parents.size()));
    }
}
