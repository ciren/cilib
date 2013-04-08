/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.guideprovider;

import fj.P3;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.crossover.operations.CrossoverSelection;
import net.sourceforge.cilib.pso.crossover.operations.RepeatingCrossoverSelection;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.StructuredType;

/**
 * This guide provider generates an offspring particle from random parents.
 * If the offspring is better than the gBest of the swarm then the offspring
 * "replaces" (the gBest's best position and fitness are updated) the gBest.
 * This is done until a better offspring is generated or the retry limit is
 * reached.
 */
public class CrossoverGuideProvider implements GuideProvider {

    private GuideProvider delegate;
    private CrossoverSelection crossoverSelection;
    private Enum positionComponent;
    private Enum fitnessComponent;

    /**
     * Default constructor.
     */
    public CrossoverGuideProvider() {
        this.delegate = new NBestGuideProvider();
        this.crossoverSelection = new RepeatingCrossoverSelection();
        this.positionComponent = EntityType.Particle.BEST_POSITION;
        this.fitnessComponent = EntityType.Particle.BEST_FITNESS;
    }

    /**
     * Copy constructor.
     *
     * @param copy
     */
    public CrossoverGuideProvider(CrossoverGuideProvider copy) {
        this.delegate = copy.delegate.getClone();
        this.crossoverSelection = copy.crossoverSelection.getClone();
        this.positionComponent = copy.positionComponent;
        this.fitnessComponent = copy.fitnessComponent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CrossoverGuideProvider getClone() {
        return new CrossoverGuideProvider(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StructuredType get(Particle particle) {
        PSO pso = (PSO) AbstractAlgorithm.get();
        P3<Boolean, Particle, Particle> result = crossoverSelection.doAction(pso, positionComponent, fitnessComponent);

        if (result._1()) {
            return result._3().getCandidateSolution();
        }

        return delegate.get(particle);
    }

    public void setCrossoverSelection(CrossoverSelection crossoverSelector) {
        this.crossoverSelection = crossoverSelector;
    }

    public CrossoverSelection getCrossoverSelection() {
        return crossoverSelection;
    }

    public void setDelegate(GuideProvider delegate) {
        this.delegate = delegate;
    }

    public GuideProvider getDelegate() {
        return delegate;
    }

    public void setComponent(String type) {
        if ("pbest".equalsIgnoreCase(type)) {
            fitnessComponent = EntityType.Particle.BEST_FITNESS;
            positionComponent = EntityType.Particle.BEST_POSITION;
        } else {
            fitnessComponent = EntityType.FITNESS;
            positionComponent = EntityType.CANDIDATE_SOLUTION;
        }
    }
}
