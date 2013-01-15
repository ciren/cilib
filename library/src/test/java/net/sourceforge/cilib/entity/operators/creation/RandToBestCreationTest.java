/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */

package net.sourceforge.cilib.entity.operators.creation;

import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

//@Ignore("For some reason this test is failing when run in the suite.... must be some static state")
public class RandToBestCreationTest {

    @Test
    public void randToBestCreationTest() {
        Rand.setSeed(0);
        RandToBestCreationStrategy creation = new RandToBestCreationStrategy();
        Topology<Entity> testTopology = new GBestTopology<Entity>();

        Entity current = new Individual();
        Entity entityBest = new Individual();
        Entity entityRandom = new Individual();
        Entity entity1 = new Individual();
        Entity entity2 = new Individual();

        testTopology.add(current);
        testTopology.add(entityBest);
        testTopology.add(entityRandom);
        testTopology.add(entity1);
        testTopology.add(entity2);

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
    }
}
