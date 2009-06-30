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
package net.sourceforge.cilib.entity.comparator;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.problem.MaximisationFitness;
import net.sourceforge.cilib.problem.MinimisationFitness;

import org.junit.Test;

/**
 *
 * @author Gary Pampara
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
