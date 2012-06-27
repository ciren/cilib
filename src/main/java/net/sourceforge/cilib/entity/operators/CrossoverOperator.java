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
package net.sourceforge.cilib.entity.operators;

import com.google.common.collect.Lists;
import java.util.List;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.OnePointCrossoverStrategy;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.util.selection.Samples;
import net.sourceforge.cilib.util.selection.recipes.RandomSelector;
import net.sourceforge.cilib.util.selection.recipes.Selector;

public class CrossoverOperator implements Operator {

    private static final long serialVersionUID = -5058325193277909244L;
    
    private CrossoverStrategy crossoverStrategy;
    private ControlParameter crossoverProbability;
    private ProbabilityDistributionFuction randomDistribution;
    private Selector selectionStrategy;

    public CrossoverOperator() {
        this(new OnePointCrossoverStrategy(), ConstantControlParameter.of(0.5), 
                new RandomSelector(), new UniformDistribution());
    }
    
    public CrossoverOperator(CrossoverStrategy strategy, ControlParameter probability, 
            Selector selector, ProbabilityDistributionFuction random) {
        this.crossoverProbability = probability;
        this.randomDistribution = random;
        this.selectionStrategy = selector;
        this.crossoverStrategy = strategy;
    }

    public CrossoverOperator(CrossoverOperator copy) {
        this.crossoverProbability = copy.crossoverProbability.getClone();
        this.randomDistribution = copy.randomDistribution;
        this.selectionStrategy = copy.selectionStrategy;
        this.crossoverStrategy = copy.crossoverStrategy.getClone();
    }

    @Override
    public CrossoverOperator getClone() {
        return new CrossoverOperator(this);
    }

    public <E extends Entity> List<E> crossover(List<E> parentCollection) {
        if (randomDistribution.getRandomNumber() < crossoverProbability.getParameter()) {
            return crossoverStrategy.crossover(selectionStrategy
                    .on(parentCollection).select(Samples
                    .first(crossoverStrategy.getNumberOfParents())));
        }
        
        return Lists.<E>newArrayList();
    }

    public ControlParameter getCrossoverProbability() {
        return crossoverProbability;
    }

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

    public CrossoverStrategy getCrossoverStrategy() {
        return crossoverStrategy;
    }

    public void setCrossoverStrategy(CrossoverStrategy crossoverStrategy) {
        this.crossoverStrategy = crossoverStrategy;
    }
}
