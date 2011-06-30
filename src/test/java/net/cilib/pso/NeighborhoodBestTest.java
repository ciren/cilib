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
package net.cilib.pso;

import fj.data.List;
import fj.data.Option;
import net.cilib.collection.Topology;
import net.cilib.collection.immutable.ImmutableGBestTopology;
import net.cilib.collection.immutable.ImmutableLBestTopology;
import net.cilib.entity.Entity;
import net.cilib.entity.FitnessComparator;
import net.cilib.entity.Individual;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

/**
 * @author gpampara
 */
public class NeighborhoodBestTest {

    private Individual newMockIndividual(double fitness) {
        return new Individual(List.<Double>nil(), Option.some(fitness));
    }

    @Test
    public void lbestNeighborhoodMax() {
        Individual target = newMockIndividual(3.0);
        ImmutableLBestTopology<Individual> topology = ImmutableLBestTopology.topologyOf(3,
                newMockIndividual(1.0),
                newMockIndividual(2.0),
                target,
                newMockIndividual(4.0),
                newMockIndividual(5.0));
        NeighborhoodBest guide = new NeighborhoodBest(FitnessComparator.MAX);
        Entity result = guide.f(target, topology);
        Assert.assertThat(result.fitness().some(), equalTo(4.0));
    }

    @Test
    public void gbestNeighborhoodMax() {
        Individual target = newMockIndividual(3.0);
        Topology<Individual> topology = ImmutableGBestTopology.topologyOf(
                newMockIndividual(1.0),
                newMockIndividual(2.0),
                target,
                newMockIndividual(4.0),
                newMockIndividual(5.0));
        NeighborhoodBest guide = new NeighborhoodBest(FitnessComparator.MAX);
        Entity result = guide.f(target, topology);
        Assert.assertThat(result.fitness().some(), equalTo(5.0));
    }

    @Test
    public void lbestNeighborhoodMin() {
        Individual target = newMockIndividual(3.0);
        ImmutableLBestTopology<Individual> topology = ImmutableLBestTopology.topologyOf(3,
                newMockIndividual(1.0),
                newMockIndividual(2.0),
                target,
                newMockIndividual(4.0),
                newMockIndividual(5.0));
        NeighborhoodBest guide = new NeighborhoodBest(FitnessComparator.MIN);
        Entity result = guide.f(target, topology);
        Assert.assertThat(result.fitness().some(), equalTo(2.0));
    }

    @Test
    public void gbestNeighborhoodMin() {
        Individual target = newMockIndividual(3.0);
        Topology<Individual> topology = ImmutableGBestTopology.topologyOf(
                newMockIndividual(1.0),
                newMockIndividual(2.0),
                target,
                newMockIndividual(4.0),
                newMockIndividual(5.0));
        NeighborhoodBest guide = new NeighborhoodBest(FitnessComparator.MIN);
        Entity result = guide.f(target, topology);
        Assert.assertThat(result.fitness().some(), equalTo(1.0));
    }
}