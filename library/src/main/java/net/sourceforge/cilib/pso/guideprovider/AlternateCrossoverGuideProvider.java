/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.guideprovider;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.discrete.UniformCrossoverStrategy;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.StructuredType;

import java.util.Arrays;

/**
 * This guide provider generates an offspring particle from specific parents
 * using a given crossover strategy.
 */
public class AlternateCrossoverGuideProvider implements GuideProvider {

    private CrossoverStrategy crossover;
    private GuideProvider parent1;
    private GuideProvider parent2;

    /**
     * Default constructor.
     */
    public AlternateCrossoverGuideProvider() {
        this.crossover = new UniformCrossoverStrategy();
        this.parent1 = new PBestGuideProvider();
        this.parent2 = new CurrentPositionGuideProvider();
    }

    /**
     * Copy constructor.
     *
     * @param copy
     */
    public AlternateCrossoverGuideProvider(AlternateCrossoverGuideProvider copy) {
        this.crossover = copy.crossover.getClone();
        this.parent1 = copy.parent1.getClone();
        this.parent2 = copy.parent2.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AlternateCrossoverGuideProvider getClone() {
        return new AlternateCrossoverGuideProvider(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StructuredType get(Particle particle) {
        PSO pso = (PSO) AbstractAlgorithm.get();
        Particle p1 = particle.getClone();
        p1.setPosition(parent1.get(particle));
        Particle p2 = particle.getClone();
        p2.setPosition(parent2.get(particle));

        return crossover.crossover(Arrays.asList(p1, p2)).get(0).getPosition();
    }
    
    public void setCrossoverStrategy(CrossoverStrategy crossover) {
        this.crossover = crossover;
    }

    public void setParent2(GuideProvider parent2) {
        this.parent2 = parent2;
    }

    public void setParent1(GuideProvider parent1) {
        this.parent1 = parent1;
    }
}
