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
package net.sourceforge.cilib.pso.dynamic.responsestrategies;

import java.util.ArrayList;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;

/**
 * This {@link EnvironmentChangeResponseStrategy reaction strategy} constructs a
 * constructs a list of {@link EnvironmentChangeResponseStrategy reaction strategies}.
 * When it is prompted to perform a reaction, it will call the response strategies
 * in the list, in sequence.
 *
 * @param <E> some {@link PopulationBasedAlgorithm population based algorithm}
 */
public class MultiReactionStrategy<E extends PopulationBasedAlgorithm> extends EnvironmentChangeResponseStrategy<E> {

    protected ArrayList< EnvironmentChangeResponseStrategy<E> > responses;

    public MultiReactionStrategy() {
		this.responses = new ArrayList< EnvironmentChangeResponseStrategy<E> >();
    }

    public MultiReactionStrategy(MultiReactionStrategy<E> rhs) {
        super(rhs);
		for (EnvironmentChangeResponseStrategy<E> response : rhs.responses) {
			this.responses.add(response);
		}
    }

    @Override
    public MultiReactionStrategy<E> getClone() {
        return new MultiReactionStrategy<E>(this);
    }

    /**
     * Performs multiple reaction strategies, in the order in which they were
	 * added via the addResponseStrategy method.
     *
     * {@inheritDoc}
     */
    @Override
    public void performReaction(E algorithm) {
        for (EnvironmentChangeResponseStrategy<E> response : responses) {
			response.performReaction(algorithm);
		}
    }

	/**
     * Adds a response strategy to the end of the list of responses that have to
	 * be performed.
     *
     * @param response The response strategy that has to be added.
     */
	public void addResponseStrategy(EnvironmentChangeResponseStrategy<E> response) {
		responses.add(response);
	}
}
