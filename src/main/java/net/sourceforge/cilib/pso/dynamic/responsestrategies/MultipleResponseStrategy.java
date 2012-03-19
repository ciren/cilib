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
import java.util.List;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;

/**
 * This {@link EnvironmentChangeResponseStrategy reaction strategy} wraps a list
 * of EnvironmentChangeResponseStrategy reaction strategies. When a change occurs
 * it iterates through the list of response strategies and perform then one after
 * another.
 *
 * @author Marde Greeff
 * @param <E> some {@link PopulationBasedAlgorithm population based algorithm}
 */

public class MultipleResponseStrategy <E extends PopulationBasedAlgorithm> extends
        EnvironmentChangeResponseStrategy<E> {

	private static final long serialVersionUID = -6669872304769439786L;

	/**
	 * Collection of response strategies. The order is important - when a change
	 * occurs the list is iterated and the strategies' response method is executed
	 * one after another
	 */
	private List<EnvironmentChangeResponseStrategy<E>> responseStrategies;
	
	/**
	 * Creates a new instance of MultipleResponseStrategy.
	 */
	public MultipleResponseStrategy() {
		this.responseStrategies = new ArrayList<EnvironmentChangeResponseStrategy<E>>();
		//System.out.println("here in multiple response strategy");
	}
	
	/**
	 * Creates a new instance of MultipleResponseStrategy, with the provided
         * response strategies.
	 * @param mrs Response strategies that are used when a change occurs.
	 */
	public MultipleResponseStrategy(MultipleResponseStrategy<E> mrs) {
		super(mrs);
		this.responseStrategies = new ArrayList<EnvironmentChangeResponseStrategy<E>>(mrs.responseStrategies);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
        public MultipleResponseStrategy<E> getClone() {
            return new MultipleResponseStrategy<E>(this);
        }
	
	/**
	 * Sets the list of response strategies that will be used 
	 * when a change occurs in the environment.
	 * @param rs Response strategies.
	 */
	public void setResponseStrategies(List<EnvironmentChangeResponseStrategy<E>> rs) {
		this.responseStrategies = rs;
	}
	
	/**
	 * Returns the list of response strategies.
	 * @return responseStrategies Response strategies used when a change occurs.
	 */
	public List<EnvironmentChangeResponseStrategy<E>> getResponseStrategies() {
		return this.responseStrategies;
	}
	
	/**
        * Respond by iterating through the list of response strategies and
        * responding accordingly one way after another.
        *
        * @param algorithm The algorithm to perform the response on.
        */
        @Override
        public void performReaction(E algorithm) {
    	    //iterate through the list of response strategies
            for (EnvironmentChangeResponseStrategy<E> ecrs: this.responseStrategies){
    		//performing the response
    		ecrs.respond(algorithm); 
            }
        }
    
        /**
        * Adds a response strategy to the list of response strategies. The order
        * is important - when a change occurs the list is iterated and the
        * strategies' response method is executed one after another.
        * @param strategy EnvironmentChangeResponseStrategy.
        */
        public void addResponseStrategy(EnvironmentChangeResponseStrategy<E> strategy) {
            this.responseStrategies.add(strategy);
        }
    
}
