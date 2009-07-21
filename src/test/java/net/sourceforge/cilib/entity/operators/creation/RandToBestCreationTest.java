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
/**
 *
 */
package net.sourceforge.cilib.entity.operators.creation;

import static org.hamcrest.CoreMatchers.is;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.math.random.generator.SeedSelectionStrategy;
import net.sourceforge.cilib.math.random.generator.Seeder;
import net.sourceforge.cilib.math.random.generator.ZeroSeederStrategy;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author leo
 *
 */
public class RandToBestCreationTest {

    @Test
    public void randToBestCreationTest(){
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

            testTopology.add((Individual)current);
            testTopology.add((Individual)entityBest);
            testTopology.add((Individual)entityRandom);
            testTopology.add((Individual)entity1);
            testTopology.add((Individual)entity2);

            entityBest.getProperties().put(EntityType.FITNESS, new MinimisationFitness(0.0));
            entityBest.getProperties().put(EntityType.CANDIDATE_SOLUTION, new Vector(1, new Real(0.1)));
            entityRandom.getProperties().put(EntityType.FITNESS, new MinimisationFitness(1.0));
            entityRandom.getProperties().put(EntityType.CANDIDATE_SOLUTION, new Vector(1, new Real(0.2)));
            entity1.getProperties().put(EntityType.FITNESS, new MinimisationFitness(2.0));
            entity1.getProperties().put(EntityType.CANDIDATE_SOLUTION, new Vector(1, new Real(0.3)));
            entity2.getProperties().put(EntityType.FITNESS, new MinimisationFitness(3.0));
            entity2.getProperties().put(EntityType.CANDIDATE_SOLUTION, new Vector(1, new Real(0.4)));

            Entity resultEntity = creation.create(entityRandom, current, testTopology);

            Assert.assertThat((Double)((Vector)resultEntity.getCandidateSolution()).get(0).getReal(), is(0.2));
        } finally {
            Seeder.setSeederStrategy(seedStrategy);
        }
    }

}
