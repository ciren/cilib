/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.comparator;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.problem.solution.MaximisationFitness;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;

import org.junit.Test;

/**
 *
 */
public class DescendingFitnessComparatorTest {

    /**
     *
     */
    @Test
    public void minimisationFitnesses() {
        Entity entity1 = new Individual();
        Entity entity2 = new Individual();
        Entity entity3 = new Individual();

        entity1.getProperties().put(EntityType.FITNESS, new MinimisationFitness(0.0));
        entity2.getProperties().put(EntityType.FITNESS, new MinimisationFitness(1.0));
        entity3.getProperties().put(EntityType.FITNESS, new MinimisationFitness(2.0));

        List<Entity> entities = Arrays.asList(entity1, entity2, entity3);
        Collections.sort(entities, new DescendingFitnessComparator());

        assertThat(entity3, is(entities.get(2)));
    }

    /**
     * With the case of MaximisationFitness values, a descending ordering will be
     * from the most fit (largest value) to the least fit (smallest value).
     */
    @Test
    public void maximisationFitnesses() {
        Entity entity1 = new Individual();
        Entity entity2 = new Individual();
        Entity entity3 = new Individual();

        entity1.getProperties().put(EntityType.FITNESS, new MaximisationFitness(0.0));
        entity2.getProperties().put(EntityType.FITNESS, new MaximisationFitness(1.0));
        entity3.getProperties().put(EntityType.FITNESS, new MaximisationFitness(2.0));

        List<Entity> entities = Arrays.asList(entity1, entity2, entity3);
        Collections.sort(entities, new DescendingFitnessComparator());

        assertThat(entity3, is(entities.get(0)));
    }

}
