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
package net.sourceforge.cilib.entity.initialization;

import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;
import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

/**
 *
 * @author gpampara
 */
public class ConstantInitializationStrategyTest {

    @Test
    public void testGetClone() {
        ConstantInitializationStrategy strategy = new ConstantInitializationStrategy(1.0);
        ConstantInitializationStrategy clone = strategy.getClone();

        Assert.assertNotSame(strategy, clone);
        Assert.assertEquals(strategy.getConstant(), clone.getConstant(), Maths.EPSILON);
    }

    @Test
    public void initialize() {
        Vector vector = Vectors.create(1.0, 1.0, 1.0);
        Individual individual = new Individual();
        individual.getProperties().put(EntityType.CANDIDATE_SOLUTION, vector.getClone());

        ConstantInitializationStrategy<Individual> initializationStrategy = new ConstantInitializationStrategy<Individual>();
        initializationStrategy.initialize(EntityType.CANDIDATE_SOLUTION, individual);

        Vector chromosome = (Vector) individual.getCandidateSolution();

        for (int i = 0; i < vector.size(); i++) {
            Assert.assertThat(vector.getReal(i), is(not(chromosome.getReal(i))));
        }
    }

    @Test(expected=UnsupportedOperationException.class)
    public void invalidInitialize() {
        Individual individual = new Individual();
        individual.getProperties().put(EntityType.CANDIDATE_SOLUTION, new Real());

        ConstantInitializationStrategy<Individual> initializationStrategy = new ConstantInitializationStrategy<Individual>();

        initializationStrategy.initialize(EntityType.CANDIDATE_SOLUTION, individual);
    }

}