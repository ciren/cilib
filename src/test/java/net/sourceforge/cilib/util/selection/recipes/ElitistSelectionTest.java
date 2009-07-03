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
package net.sourceforge.cilib.util.selection.recipes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.operators.selection.ElitistSelectionStrategy;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.problem.MaximisationFitness;
import net.sourceforge.cilib.problem.MinimisationFitness;
import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;

/**
 *
 * @author gpampara
 */
public class ElitistSelectionTest {

    @Test
    public void selectionOfMostFit() {
        Individual indiv1 = new Individual();
        Individual indiv2 = new Individual();
        Individual indiv3 = new Individual();

        indiv1.getProperties().put(EntityType.FITNESS, new MinimisationFitness(99.0));
        indiv2.getProperties().put(EntityType.FITNESS, new MinimisationFitness(8.0));
        indiv3.getProperties().put(EntityType.FITNESS, new MinimisationFitness(9.0));

        Topology<Individual> population = new GBestTopology<Individual>();
        population.add(indiv1);
        population.add(indiv2);
        population.add(indiv3);

        ElitistSelectionStrategy selector = new ElitistSelectionStrategy();
        final Individual selected = selector.select(population);

        List<Individual> list = population.asList();
        Collections.sort(list);

        Assert.assertThat(selected.getFitness(), is(indiv2.getFitness()));
    }

    @Test
    public void selectionOfMostFitMaximisation() {
        Individual indiv1 = new Individual();
        Individual indiv2 = new Individual();
        Individual indiv3 = new Individual();

        indiv1.getProperties().put(EntityType.FITNESS, new MaximisationFitness(99.0));
        indiv2.getProperties().put(EntityType.FITNESS, new MaximisationFitness(8.0));
        indiv3.getProperties().put(EntityType.FITNESS, new MaximisationFitness(9.0));

        Topology<Individual> population = new GBestTopology<Individual>();
        population.add(indiv1);
        population.add(indiv2);
        population.add(indiv3);

        ElitistSelectionStrategy selector = new ElitistSelectionStrategy();
        final Individual selected = selector.select(population);

        List<Individual> list = population.asList();
        Collections.sort(list);

        Assert.assertEquals(indiv1.getFitness().getValue(), selected.getFitness().getValue());
    }

    @Test
    public void elitistSelection() {
         List<Integer> elements = Arrays.asList(9, 8, 7, 6, 5, 4, 3, 2, 1);
         ElitistSelection<Integer> selection = new ElitistSelection<Integer>();
         Integer selected = selection.select(elements);
         Assert.assertEquals(9, selected.intValue());
    }

}
