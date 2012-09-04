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
package net.sourceforge.cilib.algorithm.initialisation;

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

        final PopulationInitialisationStrategy<Entity> initialisationBuilder = new ClonedPopulationInitialisationStrategy<Entity>();
        initialisationBuilder.setEntityType(entity);
        initialisationBuilder.setEntityNumber(20);

        initialisationBuilder.initialise(problem);

        verify(entity, times(20)).getClone();
    }

}
