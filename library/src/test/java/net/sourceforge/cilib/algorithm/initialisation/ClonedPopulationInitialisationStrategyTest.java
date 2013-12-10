/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.algorithm.initialisation;

import fj.F;
import fj.data.Array;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.Problem;
import org.junit.Ignore;
import org.junit.Test;
import static org.mockito.Mockito.*;

@Ignore
public class ClonedPopulationInitialisationStrategyTest {

    @Test
    public void initialiseClonedTopology() {
        final Entity entity = mock(Entity.class);
        final Problem problem = mock(Problem.class);
        
        final PopulationInitialisationStrategy initialisationBuilder = new ClonedPopulationInitialisationStrategy();
        initialisationBuilder.setEntityType(entity);
        initialisationBuilder.setEntityNumber(20);

        initialisationBuilder.initialise(problem);

        verify(entity, times(20)).getClone();
    }

}
