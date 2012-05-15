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
package net.sourceforge.cilib.entity.operators.crossover;

import java.util.List;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.ParameterAdaptingControlParameter;
import net.sourceforge.cilib.ec.ParameterizedDEIndividual;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.operators.Operator;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.util.selection.recipes.RandomSelector;
import net.sourceforge.cilib.util.selection.recipes.Selector;

/**
 */
public abstract class CrossoverStrategy implements Operator {

    private static final long serialVersionUID = -5058325193277909244L;
    private ControlParameter crossoverProbability;
    private ProbabilityDistributionFuction randomDistribution;
    private Selector selectionStrategy;

    public CrossoverStrategy() {
        crossoverProbability = ConstantControlParameter.of(0.5);
        randomDistribution = new UniformDistribution();
        selectionStrategy = new RandomSelector();
    }

    public CrossoverStrategy(CrossoverStrategy copy) {
        this.crossoverProbability = copy.crossoverProbability.getClone();
        this.randomDistribution = copy.randomDistribution;
        this.selectionStrategy = copy.selectionStrategy;
    }

    /**
     * {@inheritDoc}
     */
    public abstract CrossoverStrategy getClone();

    public abstract List<Entity> crossover(List<Entity> parentCollection);

    /**
     *
     * @return
     */
    public ControlParameter getCrossoverProbability() {
        return crossoverProbability;
    }

    /**
     *
     * @param crossoverProbability
     */
    public void setCrossoverProbability(ControlParameter crossoverProbability) {
        this.crossoverProbability = crossoverProbability;
    }

    public ProbabilityDistributionFuction getRandomDistribution() {
        return randomDistribution;
    }

    public void setRandomDistribution(ProbabilityDistributionFuction randomNumber) {
        this.randomDistribution = randomNumber;
    }

    public Selector getSelectionStrategy() {
        return selectionStrategy;
    }

    public void setSelectionStrategy(Selector selectionStrategy) {
        this.selectionStrategy = selectionStrategy;
    }
    
    /* 
     * Sets the recombination probability to the value held by the individual
     * @param entity The individual holding the new parameter
     */
    public void setControlParameters(Entity entity) {
        if(entity instanceof ParameterizedDEIndividual) {
            crossoverProbability = ((ParameterizedDEIndividual) entity).getRecombinationProbability();
        }
    }
}
