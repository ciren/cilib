/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.crossover;

import fj.F2;
import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.pso.guideprovider.GuideProvider;
import net.sourceforge.cilib.pso.guideprovider.NBestGuideProvider;
import net.sourceforge.cilib.type.types.container.Vector;

public class CrossoverReplaceFunction extends F2<Topology<Particle>, Particle, List<Particle>> {

    private GuideProvider parentProvider;
    private CrossoverStrategy crossoverStrategy;

    public CrossoverReplaceFunction() {
        this.parentProvider = new NBestGuideProvider();
        this.crossoverStrategy = new DiscreteVelocityCrossoverStrategy();
    }

    public void setParentProvider(GuideProvider parentProvider) {
        this.parentProvider = parentProvider;
    }

    public GuideProvider getParentProvider() {
        return parentProvider;
    }

    public void setCrossoverStrategy(CrossoverStrategy crossoverStrategy) {
        this.crossoverStrategy = crossoverStrategy;
    }

    public CrossoverStrategy getCrossoverStrategy() {
        return crossoverStrategy;
    }

    @Override
    public List<Particle> f(Topology<Particle> a, Particle p) {
        Particle parent = p.getClone();
        Vector parentSolution = (Vector) parentProvider.get(p);
        parent.setCandidateSolution(parentSolution);

        return crossoverStrategy.crossover(Arrays.asList(p, parent));
    }
}
