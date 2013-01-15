/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.crossover.pbestupdate;

import java.util.List;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.real.ParentCentricCrossoverStrategy;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.StructuredType;

public class CrossoverOffspringPBestProvider extends OffspringPBestProvider {

    private CrossoverStrategy crossoverStrategy;

    public CrossoverOffspringPBestProvider() {
        this.crossoverStrategy = new ParentCentricCrossoverStrategy();
    }

    @Override
    public StructuredType f(List<Particle> parent, Particle offspring) {
        return crossoverStrategy.crossover(parent).get(0).getCandidateSolution();
    }

    public CrossoverStrategy getCrossoverStrategy() {
        return crossoverStrategy;
    }

    public void setCrossoverStrategy(CrossoverStrategy crossoverStrategy) {
        this.crossoverStrategy = crossoverStrategy;
    }
}
