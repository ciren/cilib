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
package net.sourceforge.cilib.pso.crossover;

import fj.P;
import fj.P3;
import java.util.List;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.ParentCentricCrossoverStrategy;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.util.Cloneable;
import net.sourceforge.cilib.util.selection.Samples;
import net.sourceforge.cilib.util.selection.recipes.ElitistSelector;
import net.sourceforge.cilib.util.selection.recipes.RandomSelector;

/**
 * An operation used in the PSOCrossoverIterationStrategy which is responsible for performing the crossover and
 * performing other actions depending on the outcome of the crossover.
 */
public abstract class CrossoverSelection implements Cloneable {

    private CrossoverStrategy crossoverStrategy;
    private RandomSelector selector;
    private ControlParameter numberOfParents;

    public CrossoverSelection() {
        this.crossoverStrategy = new ParentCentricCrossoverStrategy();
        this.selector = new RandomSelector();
        this.numberOfParents = ConstantControlParameter.of(3);
    }

    public CrossoverSelection(CrossoverSelection copy) {
        this.crossoverStrategy = copy.crossoverStrategy.getClone();
        this.selector = copy.selector;
        this.numberOfParents = copy.numberOfParents.getClone();
    }

    public P3<Boolean, Particle, Particle> select(PSO algorithm) {
        boolean isBetter = false;

        // get 3 random particles
        List<Entity> parents = selector.on(algorithm.getTopology()).select(Samples.first((int) numberOfParents.getParameter()).unique());

        //perform crossover and compute offspring's fitness
        Particle offspring = (Particle) crossoverStrategy.crossover(parents).get(0);
        offspring.calculateFitness();

        //get worst parent
        Particle worstParent = (Particle) new ElitistSelector().on(parents).select(Samples.all()).get((int) numberOfParents.getParameter() - 1);

        //replace worst parent with offspring if offspring is better
        if (offspring.getFitness().compareTo(worstParent.getFitness()) > 0) {
            isBetter = true;
            int i = algorithm.getTopology().indexOf(worstParent);
            algorithm.getTopology().set(i, offspring);
            offspring.setNeighbourhoodBest(worstParent.getNeighbourhoodBest());
        }

        return P.p(isBetter, worstParent, offspring);
    }

    public void setNumberOfParents(ControlParameter numberOfParents) {
        this.numberOfParents = numberOfParents;
    }

    public ControlParameter getNumberOfParents() {
        return numberOfParents;
    }

    public void setCrossoverStrategy(CrossoverStrategy crossoverStrategy) {
        this.crossoverStrategy = crossoverStrategy;
    }

    public abstract void doAction(PSO algorithm);

    @Override
    public abstract CrossoverSelection getClone();
}
