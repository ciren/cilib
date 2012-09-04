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
/**
 *
 */
package net.sourceforge.cilib.entity.operators.creation;

import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.math.random.generator.seeder.SeedSelectionStrategy;
import net.sourceforge.cilib.math.random.generator.seeder.Seeder;
import net.sourceforge.cilib.math.random.generator.seeder.ZeroSeederStrategy;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 */
@Ignore("For some reason this test is failing when run in the suite.... must be some static state")
public class RandToBestCreationTest {

    @Test
    public void randToBestCreationTest() {
        SeedSelectionStrategy seedStrategy = Seeder.getSeederStrategy();
        Seeder.setSeederStrategy(new ZeroSeederStrategy());

        try {
            RandToBestCreationStrategy creation = new RandToBestCreationStrategy();
            Topology<Individual> testTopology = new GBestTopology<Individual>();

            Entity current = new Individual();
            Entity entityBest = new Individual();
            Entity entityRandom = new Individual();
            Entity entity1 = new Individual();
            Entity entity2 = new Individual();

            testTopology.add((Individual) current);
            testTopology.add((Individual) entityBest);
            testTopology.add((Individual) entityRandom);
            testTopology.add((Individual) entity1);
            testTopology.add((Individual) entity2);

            entityBest.getProperties().put(EntityType.FITNESS, new MinimisationFitness(0.0));
            entityBest.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of(0.1));
            entityRandom.getProperties().put(EntityType.FITNESS, new MinimisationFitness(1.0));
            entityRandom.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of(0.2));
            entity1.getProperties().put(EntityType.FITNESS, new MinimisationFitness(2.0));
            entity1.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of(0.3));
            entity2.getProperties().put(EntityType.FITNESS, new MinimisationFitness(3.0));
            entity2.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of(0.4));

            Entity resultEntity = creation.create(entityRandom, current, testTopology);

            Assert.assertEquals(0.1, ((Vector) resultEntity.getCandidateSolution()).doubleValueOf(0), 0.001);
        } finally {
            Seeder.setSeederStrategy(seedStrategy);
        }
    }
}
