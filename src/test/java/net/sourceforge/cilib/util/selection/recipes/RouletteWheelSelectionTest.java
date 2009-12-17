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

import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.math.random.generator.Random;
import net.sourceforge.cilib.problem.MaximisationFitness;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.util.selection.weighing.entity.EntityWeighing;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.Matchers.hasItem;

/**
 * <p>
 * Tests to test the behaviour of RouletteWheelSelection, in both the minimization
 * and maximization cases.
 * </p>
 */
public class RouletteWheelSelectionTest {

    @Test(expected = IllegalArgumentException.class)
    public void selectEmpty() {
        List<Integer> elements = Lists.newArrayList();
        RouletteWheelSelection<Integer> selection = new RouletteWheelSelection<Integer>();
        selection.select(elements);
    }

    @Test
    public void selectSingle() {
        List<Integer> elements = Lists.newArrayList(1);
        RouletteWheelSelection<Integer> selection = new RouletteWheelSelection<Integer>();
        int selected = selection.select(elements);
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

    @Test(expected = IllegalArgumentException.class)
    public void selectionWithInferiorFitness() {
        RouletteWheelSelection<Entity> rouletteWheelSelection = new RouletteWheelSelection<Entity>(new EntityWeighing<Entity>());
        rouletteWheelSelection.select(new GBestTopology<Individual>());
    }

    @Test
    public void minimizationSelection() {
        Topology<Individual> topology = createDummyTopology();
        topology.get(0).getProperties().put(EntityType.FITNESS, new MinimisationFitness(10000.0));
        topology.get(1).getProperties().put(EntityType.FITNESS, new MinimisationFitness(10000.0));
        topology.get(2).getProperties().put(EntityType.FITNESS, new MinimisationFitness(0.00001)); // Should be the best entity

        RouletteWheelSelection<Individual> selection = new RouletteWheelSelection<Individual>(new EntityWeighing<Individual>());
        selection.setRandom(new ConstantRandomNumber());
        Individual selected = selection.select(topology);

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

        RouletteWheelSelection<Individual> selection = new RouletteWheelSelection<Individual>(new EntityWeighing<Individual>());
        selection.setRandom(new ConstantRandomNumber());
        Individual selected = selection.select(topology);

        Assert.assertThat(selected, is(notNullValue()));
        Assert.assertThat(topology, hasItem(selected));
        Assert.assertThat(selected, is(topology.get(1)));
    }

    private static class ConstantRandomNumber extends Random {

        private static final long serialVersionUID = 3019387660938987850L;

        public ConstantRandomNumber() {
            super(0);
        }

        @Override
        public Random getClone() {
            return this;
        }

        @Override
        public int nextInt(int n) {
            return super.nextInt(n);
        }
    }
}
