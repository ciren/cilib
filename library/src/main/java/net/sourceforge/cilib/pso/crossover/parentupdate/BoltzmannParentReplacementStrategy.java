/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.crossover.parentupdate;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.util.selection.recipes.BoltzmannSelector;

/**
 * This ParentReplacementStrategy uses Boltzmann selection to determine if an
 * offspring replaces a parent.
 */
public class BoltzmannParentReplacementStrategy extends ParentReplacementStrategy {
    private BoltzmannSelector<Particle> selector;

    public BoltzmannParentReplacementStrategy() {
        this.selector = new BoltzmannSelector<Particle>();
    }

    @Override
    public List<Particle> f(List<Particle> parents, List<Particle> offspring) {
        List<Particle> particles = Lists.newArrayList();

        for (int i = 0; i < Math.min(parents.size(), offspring.size()); i++) {
            particles.add(selector.on(Arrays.asList(parents.get(i), offspring.get(i))).select());
        }

        particles.addAll(parents.subList(particles.size(), parents.size()));

        return particles;
    }

    public void setBoltzmannSelector(BoltzmannSelector<Particle> selector) {
        this.selector = selector;
    }

    public BoltzmannSelector<Particle> getBoltzmannSelector() {
        return selector;
    }
}
