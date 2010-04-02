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
package net.sourceforge.cilib.coevolution.cooperative.contextupdate;

import net.sourceforge.cilib.coevolution.cooperative.ContextEntity;
import net.sourceforge.cilib.coevolution.cooperative.problem.DimensionAllocation;
import net.sourceforge.cilib.coevolution.cooperative.problem.SequencialDimensionAllocation;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.calculator.FitnessCalculator;
import static org.junit.Assert.*;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

public class SelectiveContextUpdateStrategyTest {
     @SuppressWarnings("unchecked")
    @Test
     public void SelectiveUpdateTest(){
         final ContextEntity contextEntity = new ContextEntity();

         Mockery context = new Mockery();
         final FitnessCalculator<Entity> test = context.mock(FitnessCalculator.class);
         context.checking(new Expectations() {{
                 atLeast(2).of (test).getFitness( with(any(ContextEntity.class)));
                will(onConsecutiveCalls(
                            returnValue(new MinimisationFitness(1.0)),
                            returnValue(new MinimisationFitness(3.0))));
                allowing (test).getClone(); will(returnValue(test));
            }});
         Vector testContext = new Vector();
         testContext.add(Real.valueOf(1.0));
         testContext.add(Real.valueOf(1.0));

         contextEntity.setCandidateSolution(testContext);
         contextEntity.setFitnessCalculator(test);
         Vector solution = new Vector();
         solution.add(Real.valueOf(0.0));
         DimensionAllocation allocation = new SequencialDimensionAllocation(0, 1);

         SelectiveContextUpdateStrategy strategy = new SelectiveContextUpdateStrategy();
         strategy.updateContext(contextEntity, solution, allocation);

         assertEquals(0.0, contextEntity.getCandidateSolution().get(0).doubleValue(), 0.0);
         assertEquals(1.0, contextEntity.getFitness().getValue(), 0.0);

         Vector otherSolution = new Vector();
         otherSolution.add(Real.valueOf(3.0));
         strategy.updateContext(contextEntity, otherSolution, allocation);

         assertEquals(0.0, contextEntity.getCandidateSolution().get(0).doubleValue(), 0.0);
         assertEquals(1.0, contextEntity.getFitness().getValue(), 0.0);
     }
}
