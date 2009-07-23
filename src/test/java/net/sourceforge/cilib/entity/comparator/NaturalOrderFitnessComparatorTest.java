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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.MaximisationFitness;
import net.sourceforge.cilib.problem.MinimisationFitness;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

/**
 *
 * @author gpampara
 */
public class NaturalOrderFitnessComparatorTest {

    @Test
    public void minimisation() {
        Individual i1 = createIndividual(new MinimisationFitness(1.0));
        Individual i2 = createIndividual(new MinimisationFitness(2.0));
        Individual i3 = createIndividual(new MinimisationFitness(3.0));
        Individual i4 = createIndividual(new MinimisationFitness(4.0));
        Individual i5 = createIndividual(new MinimisationFitness(5.0));

        List<Individual> list = Arrays.asList(i1, i2, i3, i4, i5);
        Collections.shuffle(list);
        Collections.sort(list, new NaturalOrderFitnessComparator());

        Assert.assertThat(list.get(4).getFitness().getValue(), is(i1.getFitness().getValue()));
    }

    @Test
    public void maximisation() {
        Individual i1 = createIndividual(new MaximisationFitness(1.0));
        Individual i2 = createIndividual(new MaximisationFitness(2.0));
        Individual i3 = createIndividual(new MaximisationFitness(3.0));
        Individual i4 = createIndividual(new MaximisationFitness(4.0));
        Individual i5 = createIndividual(new MaximisationFitness(5.0));

        List<Individual> list = Arrays.asList(i1, i2, i3, i4, i5);
        Collections.shuffle(list);
        Collections.sort(list, new NaturalOrderFitnessComparator());

        Assert.assertThat(list.get(4).getFitness().getValue(), is(i5.getFitness().getValue()));
    }

    private Individual createIndividual(Fitness fitness) {
        Individual i = new Individual();
        i.getProperties().put(EntityType.FITNESS, fitness);
        return i;
    }
}
