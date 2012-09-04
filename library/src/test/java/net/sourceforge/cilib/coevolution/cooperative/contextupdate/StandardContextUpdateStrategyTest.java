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
package net.sourceforge.cilib.coevolution.cooperative.contextupdate;

import net.sourceforge.cilib.coevolution.cooperative.ContextEntity;
import net.sourceforge.cilib.coevolution.cooperative.problem.DimensionAllocation;
import net.sourceforge.cilib.coevolution.cooperative.problem.SequencialDimensionAllocation;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.calculator.FitnessCalculator;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StandardContextUpdateStrategyTest {
    @SuppressWarnings("unchecked")
    @Test
     public void StandardUpdateTest(){
         final ContextEntity contextEntity = new ContextEntity();

         final FitnessCalculator<Entity> test = mock(FitnessCalculator.class);
         when(test.getFitness(any(ContextEntity.class))).thenReturn(new MinimisationFitness(1.0));

         Vector testContext = Vector.of(1, 1);

         contextEntity.setCandidateSolution(testContext);
         contextEntity.setFitnessCalculator(test);
         contextEntity.setFitness(new MinimisationFitness(0.0));

         Vector solution = Vector.of(0);
         DimensionAllocation allocation = new SequencialDimensionAllocation(0, 1);

         StandardContextUpdateStrategy strategy = new StandardContextUpdateStrategy();
         strategy.updateContext(contextEntity, solution, allocation);

         assertEquals(0.0, contextEntity.getCandidateSolution().get(0).doubleValue(), 0.0);
         assertEquals(1.0, contextEntity.getFitness().getValue(), 0.0);
     }
}
