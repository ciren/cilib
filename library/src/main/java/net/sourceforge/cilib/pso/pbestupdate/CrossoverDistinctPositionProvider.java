/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.pbestupdate;

import java.util.Arrays;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.real.ParentCentricCrossoverStrategy;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.Vector;

public class CrossoverDistinctPositionProvider extends DistinctPositionProvider {

    private CrossoverStrategy crossoverStrategy;

    public CrossoverDistinctPositionProvider() {
        this.crossoverStrategy = new ParentCentricCrossoverStrategy();
    }

    public CrossoverDistinctPositionProvider(CrossoverDistinctPositionProvider copy) {
        this.crossoverStrategy = copy.crossoverStrategy.getClone();
    }

    @Override
    public CrossoverDistinctPositionProvider getClone() {
        return new CrossoverDistinctPositionProvider(this);
    }

    @Override
    public Vector f(Particle particle) {
        Particle p1 = particle.getClone();
        Particle p2 = particle.getClone();
        Particle p3 = particle.getClone();

        p1.setCandidateSolution(particle.getCandidateSolution());
        p2.setCandidateSolution(particle.getBestPosition());
        p3.setCandidateSolution(particle.getNeighbourhoodBest().getBestPosition());

        return (Vector) crossoverStrategy.crossover(Arrays.asList(p1, p2, p3))
                .get(0).getCandidateSolution();
    }

    public void setCrossoverStrategy(CrossoverStrategy crossoverStrategy) {
        this.crossoverStrategy = crossoverStrategy;
    }

    public CrossoverStrategy getCrossoverStrategy() {
        return crossoverStrategy;
    }
}
