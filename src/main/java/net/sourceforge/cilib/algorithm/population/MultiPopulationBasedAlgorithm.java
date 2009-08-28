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
package net.sourceforge.cilib.algorithm.population;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.initialisation.PopulationInitialisationStrategy;
import net.sourceforge.cilib.cooperative.algorithmiterators.AlgorithmIterator;
import net.sourceforge.cilib.cooperative.algorithmiterators.SequentialAlgorithmIterator;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.visitor.TopologyVisitor;

/**
 * Algorithm class to describe the notion of aggregated {@linkplain PopulationBasedAlgorithm} instances.
 * <p>
 * The objective of this class is to ensure that the manner in which various multi-population based
 * algorithms are interfaced in the same manner.
 * <p>
 * Examples of such algorithms can include:
 * <ul>
 *   <li>Island Genetic Algorithms</li>
 *   <li>Niching Algorithms</li>
 *   <li>etc.</li>
 * </ul>
 *
 * @author Gary Pampara
 */
public abstract class MultiPopulationBasedAlgorithm extends AbstractAlgorithm implements PopulationBasedAlgorithm, Iterable<PopulationBasedAlgorithm> {
    private static final long serialVersionUID = -5311450612897848103L;
    protected List<PopulationBasedAlgorithm> subPopulationsAlgorithms;
    protected AlgorithmIterator<PopulationBasedAlgorithm> algorithmIterator;

    /**
     * Create an instance of {@linkplain MultiPopulationBasedAlgorithm}.
     */
    public MultiPopulationBasedAlgorithm() {
        this.subPopulationsAlgorithms = new ArrayList<PopulationBasedAlgorithm>();
        this.algorithmIterator = new SequentialAlgorithmIterator<PopulationBasedAlgorithm>();
        this.algorithmIterator.setAlgorithms(this.subPopulationsAlgorithms);
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public MultiPopulationBasedAlgorithm(MultiPopulationBasedAlgorithm copy) {
        super(copy);
        subPopulationsAlgorithms = new ArrayList<PopulationBasedAlgorithm>();

        for (PopulationBasedAlgorithm algorithm : copy.subPopulationsAlgorithms) {
            subPopulationsAlgorithms.add(algorithm.getClone());
        }

        algorithmIterator = copy.algorithmIterator;
        algorithmIterator.setAlgorithms(subPopulationsAlgorithms);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<PopulationBasedAlgorithm> iterator() {
        return this.algorithmIterator.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected abstract void algorithmIteration();

    /**
     * Get the {@linkplain List} of current sub-populations.
     * @return The {@linkplain List} of {@linkplain PopulationBasedAlgorithm}.
     */
    public List<PopulationBasedAlgorithm> getPopulations() {
        return subPopulationsAlgorithms;
    }

    /**
     * Set the list of {@linkplain PopulationBasedAlgorithm} instances that the
     * {@linkplain MultiPopulationBasedAlgorithm} should maintain.
     * @param populationBasedAlgorithms The {@linkplain List} of {@linkplain PopulationBasedAlgorithm}s to set.
     */
    public void setPopulations(List<PopulationBasedAlgorithm> populationBasedAlgorithms) {
        this.subPopulationsAlgorithms = populationBasedAlgorithms;
    }

    /**
     * Add a {@linkplain PopulationBasedAlgorithm} to the list of maintained sub-populations.
     * @param algorithm The {@linkplain PopulationBasedAlgorithm} to add to the current collection.
     */
    public void addPopulationBasedAlgorithm(PopulationBasedAlgorithm algorithm) {
        this.subPopulationsAlgorithms.add(algorithm);
    }

    /**
     * Remove the provided {@linkplain PopulationBasedAlgorithm} from the collection of maintained
     * instances.
     * @param algorithm The instance to remove from the collection.
     */
    public void removePopulationBasedalgorithm(PopulationBasedAlgorithm algorithm) {
        this.subPopulationsAlgorithms.remove(algorithm);
    }

    /**
     * Get an {@linkplain AlgorithmIterator} to iterate over the current collection of
     * {@linkplain PopulationBasedAlgorithm}s.
     * @return An {@linkplain AlgorithmIterator} over the current collection.
     */
    public AlgorithmIterator<PopulationBasedAlgorithm> getAlgorithmIterator() {
        return algorithmIterator;
    }

    /**
     * Set the type of iterator to be used.
     * @param algorithmIterator The iterator instance to set.
     */
    public void setAlgorithmIterator(AlgorithmIterator<PopulationBasedAlgorithm> algorithmIterator) {
        this.algorithmIterator = algorithmIterator;
        this.algorithmIterator.setAlgorithms(this.subPopulationsAlgorithms);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object accept(TopologyVisitor visitor) {
        throw new UnsupportedOperationException("Needs an implementation");
    }

    @Override
    public Topology<? extends Entity> getTopology() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PopulationInitialisationStrategy getInitialisationStrategy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setInitialisationStrategy(PopulationInitialisationStrategy initialisationStrategy) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
