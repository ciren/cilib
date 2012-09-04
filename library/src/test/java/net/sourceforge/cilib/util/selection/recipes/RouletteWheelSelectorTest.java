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
package net.sourceforge.cilib.util.selection.recipes;

import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.problem.solution.MaximisationFitness;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.util.selection.weighting.EntityWeighting;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.Matchers.hasItem;

/**
 * <p>
 * Tests to test the behavior of RouletteWheelSelection, in both the minimization
 * and maximization cases.
 * </p>
 */
public class RouletteWheelSelectorTest {

    @Test(expected = IllegalArgumentException.class)
    public void selectEmpty() {
        List<Integer> elements = Lists.newArrayList();
        RouletteWheelSelector<Integer> selection = new RouletteWheelSelector<Integer>();
        selection.on(elements).select();
    }

    @Test
    public void selectSingle() {
        List<Integer> elements = Lists.newArrayList(1);
        RouletteWheelSelector<Integer> selection = new RouletteWheelSelector<Integer>();
        int selected = selection.on(elements).select();
        Assert.assertThat(selected, is(1));
    }

    private static Topology<Individual> createDummyTopology() {
        Topology<Individual> topology = new GBestTopology<Individual>();
        Individual individual1 = new Individual();
        Individual individual2 = new Individual();
        Individual individual3 = new Individual();
        topology.add(individual1);
        topology.add(individual2);
        topology.add(individual3);
        return topology;
    }

    @Test
    public void minimizationSelection() {
        Topology<Individual> topology = createDummyTopology();
        topology.get(0).getProperties().put(EntityType.FITNESS, new MinimisationFitness(10000.0));
        topology.get(1).getProperties().put(EntityType.FITNESS, new MinimisationFitness(10000.0));
        topology.get(2).getProperties().put(EntityType.FITNESS, new MinimisationFitness(0.00001)); // Should be the best entity

        RouletteWheelSelector<Individual> selection = new RouletteWheelSelector<Individual>(new EntityWeighting());
        Individual selected = selection.on(topology).select();

        Assert.assertThat(selected, is(notNullValue()));
        Assert.assertThat(topology, hasItem(selected));

        Assert.assertThat(selected, is(topology.get(2)));
    }

    @Test
    public void maximizationSelection() {
        Topology<Individual> topology = createDummyTopology();
        topology.get(0).getProperties().put(EntityType.FITNESS, new MaximisationFitness(0.5));
        topology.get(1).getProperties().put(EntityType.FITNESS, new MaximisationFitness(90000.0)); // Should be the best entity
        topology.get(2).getProperties().put(EntityType.FITNESS, new MaximisationFitness(0.5));

        RouletteWheelSelector<Individual> selection = new RouletteWheelSelector<Individual>(new EntityWeighting());
        Individual selected = selection.on(topology).select();

        Assert.assertThat(selected, is(notNullValue()));
        Assert.assertThat(topology, hasItem(selected));
        Assert.assertThat(selected, is(topology.get(1)));
    }

    @Test
    public void someNaNSelection() {
        Topology<Individual> topology = createDummyTopology();
        topology.get(0).getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
        topology.get(1).getProperties().put(EntityType.FITNESS, new MaximisationFitness(90000.0)); // Should be the best entity
        topology.get(2).getProperties().put(EntityType.FITNESS, new MaximisationFitness(0.5));

        RouletteWheelSelector<Individual> selection = new RouletteWheelSelector<Individual>(new EntityWeighting());
        Individual selected = selection.on(topology).select();

        Assert.assertThat(selected, is(notNullValue()));
        Assert.assertThat(topology, hasItem(selected));
        Assert.assertThat(selected, is(topology.get(1)));
    }

    @Test
    public void allNaNSelection() {
        Topology<Individual> topology = createDummyTopology();
        topology.get(0).getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
        topology.get(1).getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
        topology.get(2).getProperties().put(EntityType.FITNESS, InferiorFitness.instance());

        RouletteWheelSelector<Individual> selection = new RouletteWheelSelector<Individual>(new EntityWeighting());
        Individual selected = selection.on(topology).select();

        Assert.assertThat(selected, is(notNullValue()));
        Assert.assertThat(topology, hasItem(selected));
    }
}
