/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.pso.guideprovider;

import fj.P3;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.crossover.operations.CrossoverSelection;
import net.sourceforge.cilib.pso.crossover.operations.RepeatingCrossoverSelection;
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

    private enum TempEnums {
        TEMP
    };
    
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
        Particle gBest = particle.getNeighbourhoodBest();

        if (result._1()) {
            gBest.getProperties().put(EntityType.Particle.BEST_POSITION, result._3().getCandidateSolution());
            gBest.getProperties().put(EntityType.Particle.BEST_FITNESS, result._3().getFitness());
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
        if ("pbest".equals(type)) {
            fitnessComponent = EntityType.Particle.BEST_FITNESS;
            positionComponent = EntityType.Particle.BEST_POSITION;
        } else {
            fitnessComponent = EntityType.FITNESS;
            positionComponent = EntityType.CANDIDATE_SOLUTION;
        }
    }
}
