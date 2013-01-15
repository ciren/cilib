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
 * This ParentReplacementStrategy always selects the offspring over the parents.
 */
public class AlwaysReplaceParentReplacementStrategy extends ParentReplacementStrategy {
    @Override
    public List<Particle> f(List<Particle> parents, List<Particle> offspring) {
        List<Particle> joined = Lists.newArrayList(offspring);
        joined.addAll(new ElitistSelector<Particle>().on(parents).select(Samples.all()));

        return joined.subList(0, parents.size());
    }
}
