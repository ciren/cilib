/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */

package net.sourceforge.cilib.entity.operators.creation;

import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Property;
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

        Entity current = new Individual();
        Entity entityBest = new Individual();
        Entity entityRandom = new Individual();
        Entity entity1 = new Individual();
        Entity entity2 = new Individual();

        fj.data.List<Entity> testTopology = fj.data.List.list(current, entityBest, entityRandom, entity1, entity2);

        entityBest.put(Property.FITNESS, new MinimisationFitness(0.0));
        entityBest.put(Property.CANDIDATE_SOLUTION, Vector.of(0.1));
        entityRandom.put(Property.FITNESS, new MinimisationFitness(1.0));
        entityRandom.put(Property.CANDIDATE_SOLUTION, Vector.of(0.2));
        entity1.put(Property.FITNESS, new MinimisationFitness(2.0));
        entity1.put(Property.CANDIDATE_SOLUTION, Vector.of(0.3));
        entity2.put(Property.FITNESS, new MinimisationFitness(3.0));
        entity2.put(Property.CANDIDATE_SOLUTION, Vector.of(0.4));

        Entity resultEntity = creation.create(entityRandom, current, testTopology);

        Assert.assertEquals(0.1, ((Vector) resultEntity.getPosition()).doubleValueOf(0), 0.001);
    }
}
