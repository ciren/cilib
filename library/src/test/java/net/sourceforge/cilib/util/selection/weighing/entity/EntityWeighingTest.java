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
package net.sourceforge.cilib.util.selection.weighing.entity;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.problem.solution.MaximisationFitness;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.util.selection.WeightedObject;
import net.sourceforge.cilib.util.selection.weighting.EntityWeighting;
import org.junit.Assert;
import org.junit.Test;

/**
 */
public class EntityWeighingTest {

    @Test
    public void entityWeighingMaximise() {
        Individual i1 = createIndividual(new MaximisationFitness(1.0));
        Individual i2 = createIndividual(new MaximisationFitness(2.0));
        Individual i3 = createIndividual(new MaximisationFitness(3.0));

        List<Individual> individuals = Arrays.asList(i1, i2, i3);
        EntityWeighting weighing = new EntityWeighting();
        List<WeightedObject> result = Lists.newArrayList(weighing.weigh(individuals));

        Assert.assertEquals(0.0, result.get(0).getWeight(), 0.0001);
        Assert.assertEquals(0.5, result.get(1).getWeight(), 0.0001);
        Assert.assertEquals(1.0, result.get(2).getWeight(), 0.0001);
    }

    @Test
    public void entityWeighingMinimise() {
        Individual i1 = createIndividual(new MinimisationFitness(1.0));
        Individual i2 = createIndividual(new MinimisationFitness(2.0));
        Individual i3 = createIndividual(new MinimisationFitness(3.0));

        List<Individual> individuals = Arrays.asList(i1, i2, i3);
        EntityWeighting weighing = new EntityWeighting();
        List<WeightedObject> result = Lists.newArrayList(weighing.weigh(individuals));

        Assert.assertEquals(1.0, result.get(0).getWeight(), 0.0001);
        Assert.assertEquals(0.5, result.get(1).getWeight(), 0.0001);
        Assert.assertEquals(0.0, result.get(2).getWeight(), 0.0001);
    }

    @Test
    public void entityWeighingAllNaN() {
        Individual i1 = createIndividual(InferiorFitness.instance());
        Individual i2 = createIndividual(InferiorFitness.instance());
        Individual i3 = createIndividual(InferiorFitness.instance());

        List<Individual> individuals = Arrays.asList(i1, i2, i3);
        EntityWeighting weighing = new EntityWeighting();
        List<WeightedObject> result = Lists.newArrayList(weighing.weigh(individuals));

        Assert.assertEquals(1.0, result.get(0).getWeight(), 0.0001);
        Assert.assertEquals(1.0, result.get(1).getWeight(), 0.0001);
        Assert.assertEquals(1.0, result.get(2).getWeight(), 0.0001);
    }

    @Test
    public void entityWeighingSomeNaN() {
        Individual i1 = createIndividual(new MinimisationFitness(1.0));
        Individual i2 = createIndividual(new MinimisationFitness(2.0));
        Individual i3 = createIndividual(new MinimisationFitness(3.0));
        Individual i4 = createIndividual(InferiorFitness.instance());

        List<Individual> individuals = Arrays.asList(i1, i2, i3, i4);
        EntityWeighting weighing = new EntityWeighting();
        List<WeightedObject> result = Lists.newArrayList(weighing.weigh(individuals));

        Assert.assertEquals(1.0, result.get(0).getWeight(), 0.0001);
        Assert.assertEquals(0.5, result.get(1).getWeight(), 0.0001);
        Assert.assertEquals(0.0, result.get(2).getWeight(), 0.0001);
        Assert.assertEquals(0.0, result.get(3).getWeight(), 0.0001);
    }

    private Individual createIndividual(Fitness fitness) {
        Individual i = new Individual();
        i.getProperties().put(EntityType.FITNESS, fitness);
        return i;
    }
}
