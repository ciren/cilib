/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.entity.operators.crossover;

import java.util.List;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.operators.Operator;
import net.sourceforge.cilib.entity.operators.selection.RandomSelectionStrategy;
import net.sourceforge.cilib.entity.operators.selection.SelectionStrategy;
import net.sourceforge.cilib.math.random.RandomNumber;

/**
 * @author Andries Engelbrecht
 */
public abstract class CrossoverStrategy implements Operator {
    private static final long serialVersionUID = -5058325193277909244L;

    private ControlParameter crossoverProbability;
    private RandomNumber randomNumber;
    private SelectionStrategy selectionStrategy;

    public CrossoverStrategy() {
        crossoverProbability = new ConstantControlParameter(0.5);
        randomNumber = new RandomNumber();
        selectionStrategy = new RandomSelectionStrategy();
    }

    public CrossoverStrategy(CrossoverStrategy copy) {
        this.crossoverProbability = copy.crossoverProbability.getClone();
        this.randomNumber = copy.randomNumber.getClone();
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

    public RandomNumber getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(RandomNumber randomNumber) {
        this.randomNumber = randomNumber;
    }

    public SelectionStrategy getSelectionStrategy() {
        return selectionStrategy;
    }

    public void setSelectionStrategy(SelectionStrategy selectionStrategy) {
        this.selectionStrategy = selectionStrategy;
    }

}
