/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.problem.solution.MaximisationFitness;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class EntityTest {

    @Test
    public void entitySortingMinimisation() {
        Individual i1 = new Individual();
        Individual i2 = new Individual();
        Individual i3 = new Individual();

        i1.getProperties().put(EntityType.FITNESS, new MinimisationFitness(300.0));
        i2.getProperties().put(EntityType.FITNESS, new MinimisationFitness(200.0));
        i3.getProperties().put(EntityType.FITNESS, new MinimisationFitness(100.0));

        List<Individual> list = Arrays.asList(i1, i2, i3);
        Collections.sort(list);

        Assert.assertEquals(i1, list.get(0));
    }

    @Test
    public void entitySortingMaximisation() {
        Individual i1 = new Individual();
        Individual i2 = new Individual();
        Individual i3 = new Individual();

        i1.getProperties().put(EntityType.FITNESS, new MaximisationFitness(300.0));
        i2.getProperties().put(EntityType.FITNESS, new MaximisationFitness(200.0));
        i3.getProperties().put(EntityType.FITNESS, new MaximisationFitness(100.0));

        List<Individual> list = Arrays.asList(i1, i2, i3);
        Collections.sort(list);

        Assert.assertEquals(i1, list.get(list.size()-1));
    }

    @Test
    public void entityHashCode() {
        Set<Entity> set = new HashSet<Entity>();

        Entity entity1 = new Individual();
        entity1.getProperties().put(EntityType.FITNESS, new MinimisationFitness(2.0));
        set.add(entity1);

        entity1.getProperties().put(EntityType.FITNESS, new MinimisationFitness(3.0));
        set.add(entity1);

        Assert.assertEquals(1, set.size());
    }

}
