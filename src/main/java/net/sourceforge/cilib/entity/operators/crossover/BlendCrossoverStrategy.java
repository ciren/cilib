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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p>Implementation of the blend cross-over strategy.</p>
 *
 * <p>BibTeX entry:</p>
 * <pre>
 * &#64;ARTICLE{00843494,
 * title={Gradual distributed real-coded genetic algorithms},
 * author={{Francisco Herrera} and {Manuel Lozano}},
 * journal={IEEE Trans. Evolutionary Computation},
 * year={2000},
 * month={April},
 * volume={4},
 * number={1},
 * pages={ 43-63},
 * abstract={ A major problem in the use of genetic algorithms is premature
 *   convergence, a premature stagnation of the search caused by the lack of diversity in
 *   the population. One approach for dealing with this problem is the distributed
 *   genetic algorithm model. Its basic idea is to keep, in parallel, several
 *   subpopulations that are processed by genetic algorithms, with each one being
 *   independent of the others.Furthermore, a migration mechanism produces a chromosome
 *   exchange between the subpopulations. Making distinctions between the subpopulations
 *   by applying genetic algorithms with different configurations, we obtain the
 *   so-called heterogeneous distributed genetic algorithms. These algorithms represent
 *   a promising way for introducing a correct exploration/exploitation balance in order
 *   to avoid premature convergence and reach approximate final solutions. This paper
 *   presents the gradual distributed real-coded genetic algorithms, a type of
 *   heterogeneous distributed real-coded genetic algorithms that apply a different
 *   crossover operator to each subpopulation. The importance of this operator on the
 *   genetic algorithm's performance allowed us to differentiate between the
 *   subpopulations in this fashion. Using crossover operators presented
 *   for real-coded genetic algorithms, we implement three instances of gradual
 *   distributed real-coded genetic algorithms. Experimental results show that the
 *   proposals consistently outperform sequential real-coded genetic algorithms and
 *   homogeneous distributed realcoded genetic algorithms, which are equivalent to them
 *   and other mechanisms presented in the literature. These proposals offer two
 *   important advantages at the same time: better reliability and accuracy.},
 * keywords={ Crossover operator, distributed genetic algorithms, multiresolution,
 *   premature convergence, selective pressure},
 * doi={ },
 * ISSN={ }, }
 * </pre>
 *
 */
public class BlendCrossoverStrategy extends CrossoverStrategy {

    private static final long serialVersionUID = -7955031193090240495L;
    private ControlParameter alpha;

    public BlendCrossoverStrategy() {
        alpha = ConstantControlParameter.of(0.5);
    }

    public BlendCrossoverStrategy(BlendCrossoverStrategy copy) {
        super(copy);
        this.alpha = copy.alpha.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BlendCrossoverStrategy getClone() {
        return new BlendCrossoverStrategy(this);
    }

    @Override
    public List<Entity> crossover(List<Entity> parentCollection) {
        List<Entity> offspring = new ArrayList<Entity>(parentCollection.size());

        // How do we handle variable sizes? Resizing the entities?
        Entity offspring1 = parentCollection.get(0).getClone();
        Entity offspring2 = parentCollection.get(1).getClone();

        if (this.getCrossoverProbability().getParameter() >= this.getRandomDistribution().getRandomNumber()) {
            Vector parentChromosome1 = (Vector) parentCollection.get(0).getCandidateSolution();
            Vector parentChromosome2 = (Vector) parentCollection.get(1).getCandidateSolution();
            Vector offspringChromosome1 = (Vector) offspring1.getCandidateSolution();
            Vector offspringChromosome2 = (Vector) offspring2.getCandidateSolution();

            int sizeParent1 = parentChromosome1.size();
            int sizeParent2 = parentChromosome2.size();

            int minDimension = Math.min(sizeParent1, sizeParent2);

            for (int i = 0; i < minDimension; i++) {
                double gamma = (1 + (2 * alpha.getParameter())) * this.getRandomDistribution().getRandomNumber() - alpha.getParameter();
                double value1 = (1 - gamma) * parentChromosome1.doubleValueOf(i) + gamma * parentChromosome2.doubleValueOf(i);
                double value2 = (1 - gamma) * parentChromosome2.doubleValueOf(i) + gamma * parentChromosome1.doubleValueOf(i);
                offspringChromosome1.setReal(i, value1);
                offspringChromosome2.setReal(i, value2);

                /*
                 * if (i%2 == 0) { offspringChromosome1.set(i,
                 * parentChromosome1.get(i)); offspringChromosome2.set(i,
                 * parentChromosome2.get(i)); } else {
                 * offspringChromosome1.set(i, parentChromosome2.get(i));
                 * offspringChromosome2.set(i, parentChromosome1.get(i)); }
                 */
            }

            offspring1.calculateFitness();
            offspring2.calculateFitness();

            offspring.add(offspring1);
            offspring.add(offspring2);
        }

        return offspring;
    }

    /**
     * @return the alpha
     */
    public ControlParameter getAlpha() {
        return alpha;
    }

    /**
     * @param alpha the alpha to set
     */
    public void setAlpha(ControlParameter alpha) {
        this.alpha = alpha;
    }
}
