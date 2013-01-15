/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.coevolution.cooperative.contextupdate;

import net.sourceforge.cilib.coevolution.cooperative.ContextEntity;
import net.sourceforge.cilib.coevolution.cooperative.problem.DimensionAllocation;
import net.sourceforge.cilib.coevolution.cooperative.problem.SequentialDimensionAllocation;
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
        DimensionAllocation allocation = new SequentialDimensionAllocation(0, 1);

        StandardContextUpdateStrategy strategy = new StandardContextUpdateStrategy();
        strategy.updateContext(contextEntity, solution, allocation);

        assertEquals(0.0, contextEntity.getCandidateSolution().get(0).doubleValue(), 0.0);
        assertEquals(1.0, contextEntity.getFitness().getValue(), 0.0);
    }
}
