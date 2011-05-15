/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.parser.DomainParser;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Gary Pampara
 */
public class FitnessEvaluationsTest {

    @Test
    public void result() {
        final PopulationBasedAlgorithm pba = mock(PopulationBasedAlgorithm.class);
        final OptimisationProblem problem = mock(OptimisationProblem.class);

        when(pba.getOptimisationProblem()).thenReturn(problem);
        when(problem.getFitnessEvaluations()).thenReturn(10, 20);

        Measurement m = new FitnessEvaluations();
        Int i1 = (Int) m.getValue(pba);
        Int i2 = (Int) m.getValue(pba);

        Assert.assertThat(i1.intValue(), is(10));
        Assert.assertThat(i2.intValue(), is(20));
    }

    @Test
    public void testFitnessEvaluationsDomain() {
        Measurement m = new FitnessEvaluations();
        Vector vector = (Vector) DomainParser.parse(m.getDomain());

        assertEquals(1, vector.size());
        assertTrue(vector.get(0) instanceof Int);
    }

}
