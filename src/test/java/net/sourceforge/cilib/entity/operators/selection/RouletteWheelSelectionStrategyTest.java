/*
 * RouletteWheelSelectionStrategyTest.java
 *
 * Copyright (C) 2003 - 2008
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
package net.sourceforge.cilib.entity.operators.selection;

import junit.framework.Assert;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.problem.MaximisationFitness;
import net.sourceforge.cilib.problem.MinimisationFitness;

import org.junit.Before;
import org.junit.Test;

public class RouletteWheelSelectionStrategyTest {
	
	private Topology<Individual> topology;
	private Individual individual1;
	private Individual individual2;
	private Individual individual3;

	@Before
	public void createDummyTopology() {
		topology = new GBestTopology<Individual>();
		
		individual1 = new Individual();
		individual2 = new Individual();
		individual3 = new Individual();
		topology.add(individual1);
		topology.add(individual2);
		topology.add(individual3);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void selectionWithInferiorFitness() {
		RouletteWheelSelectionStrategy rouletteWheelSelectionStrategy = new RouletteWheelSelectionStrategy();
		rouletteWheelSelectionStrategy.select(topology);
	}
	
	@Test
	public void selectionAtMinimum() {
		individual1.getProperties().put("fitness", new MinimisationFitness(0.0));
		individual2.getProperties().put("fitness", new MinimisationFitness(0.0));
		individual3.getProperties().put("fitness", new MinimisationFitness(0.0));
		
		RouletteWheelSelectionStrategy rouletteWheelSelectionStrategy = new RouletteWheelSelectionStrategy();
		Entity entity = rouletteWheelSelectionStrategy.select(topology);
		
		Assert.assertNotNull(entity);
		Assert.assertTrue(topology.contains(entity));
	}
	
	@Test
	public void selectionOfGreatestProportion() {
		individual1.getProperties().put("fitness", new MaximisationFitness(98.0));
		individual2.getProperties().put("fitness", new MaximisationFitness(1.0));
		individual3.getProperties().put("fitness", new MaximisationFitness(1.0));
		
		RouletteWheelSelectionStrategy rouletteWheelSelectionStrategy = new RouletteWheelSelectionStrategy();
		Entity entity = rouletteWheelSelectionStrategy.select(topology);
		
		Assert.assertNotNull(entity);
		Assert.assertTrue(entity.equals(individual1));
	}
	
}
