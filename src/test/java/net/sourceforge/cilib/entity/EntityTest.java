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
package net.sourceforge.cilib.entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.problem.MaximisationFitness;
import net.sourceforge.cilib.problem.MinimisationFitness;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author gpampara
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
