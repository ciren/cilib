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
package net.sourceforge.cilib.util.selection.recipes;

import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.operators.selection.RouletteWheelSelectionStrategy;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.problem.MaximisationFitness;
import net.sourceforge.cilib.problem.MinimisationFitness;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 * Tests to test the behaviour of RouletteWheelSelection, in both the minimization
 * and maximization cases.
 * </p>
 * <p>
 * It should be noted that all the values within the tests are extremely exaggerated
 * so that the tests almost always pass.
 * </p>
 */
public class RouletteWheelSelectionTest {

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
    public void minimizationSelection() {
        individual1.getProperties().put(EntityType.FITNESS, new MinimisationFitness(10000.0));
        individual2.getProperties().put(EntityType.FITNESS, new MinimisationFitness(10000.0));
        individual3.getProperties().put(EntityType.FITNESS, new MinimisationFitness(0.00001)); // Should be the best entity

        RouletteWheelSelectionStrategy rouletteWheelSelectionStrategy = new RouletteWheelSelectionStrategy();
        Entity entity = rouletteWheelSelectionStrategy.select(topology);

        Assert.assertNotNull(entity);
        Assert.assertTrue(topology.contains(entity));
        Assert.assertSame(entity, individual3);
    }

    @Test
    public void maximizationSelection() {
        individual1.getProperties().put(EntityType.FITNESS, new MaximisationFitness(0.5));
        individual2.getProperties().put(EntityType.FITNESS, new MaximisationFitness(90000.0)); // Should be the best entity
        individual3.getProperties().put(EntityType.FITNESS, new MaximisationFitness(0.5));

        RouletteWheelSelectionStrategy rouletteWheelSelectionStrategy = new RouletteWheelSelectionStrategy();
        Entity entity = rouletteWheelSelectionStrategy.select(topology);

        Assert.assertNotNull(entity);
        Assert.assertTrue(entity.equals(individual2));
    }

    @Test
    public void primitiveSelection() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

    }

}
