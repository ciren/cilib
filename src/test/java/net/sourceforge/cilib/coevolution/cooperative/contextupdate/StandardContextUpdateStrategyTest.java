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

import static org.junit.Assert.assertEquals;
import net.sourceforge.cilib.coevolution.cooperative.ContextEntity;
import net.sourceforge.cilib.coevolution.cooperative.problem.DimensionAllocation;
import net.sourceforge.cilib.coevolution.cooperative.problem.SequencialDimensionAllocation;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.calculator.FitnessCalculator;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

public class StandardContextUpdateStrategyTest {
    @SuppressWarnings("unchecked")
    @Test
     public void StandardUpdateTest(){
         final ContextEntity contextEntity = new ContextEntity();

         Mockery context = new Mockery();
         final FitnessCalculator<Entity> test = context.mock(FitnessCalculator.class);
         context.checking(new Expectations() {{
                 oneOf (test).getFitness( with(any(ContextEntity.class)));
                will(returnValue(new MinimisationFitness(1.0)));
            }});
         Vector testContext = new Vector(2);
         testContext.add(new Real(1.0));
         testContext.add(new Real(1.0));

         contextEntity.setCandidateSolution(testContext);
         contextEntity.setFitnessCalculator(test);
         contextEntity.setFitness(new MinimisationFitness(0.0));

         Vector solution = new Vector(1);
         solution.add(new Real(0.0));
         DimensionAllocation allocation = new SequencialDimensionAllocation(0, 1);

         StandardContextUpdateStrategy strategy = new StandardContextUpdateStrategy();
         strategy.updateContext(contextEntity, solution, allocation);

         assertEquals(0.0, contextEntity.getCandidateSolution().get(0).getReal(), 0.0);
         assertEquals(1.0, contextEntity.getFitness().getValue(), 0.0);
     }
}
