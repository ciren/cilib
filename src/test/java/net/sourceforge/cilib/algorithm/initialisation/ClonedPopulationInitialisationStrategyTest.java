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
import net.sourceforge.cilib.problem.OptimisationProblem;
import org.junit.Test;

import static org.mockito.Mockito.*;


/**
 * Class to test the concept of initialising a <tt>Topology</tt> of
 * <tt>Entity</tt> objects, by cloning a given prototype <tt>Entity</tt>.
 *
 * @author Gary Pampara
 */
public class ClonedPopulationInitialisationStrategyTest {

    /**
     * Test that the initialisation of the entity does indeed mean that the initialised
     * Entity is added to the topology and the Entity is not the same as the original.
     */
    @Test
    public void initialiseClonedTopology() {
        final Entity entity = mock(Entity.class);
        final OptimisationProblem problem = mock(OptimisationProblem.class);

        when(entity.getClone()).thenReturn(entity);

        final PopulationInitialisationStrategy<Entity> initialisationBuilder = new ClonedPopulationInitialisationStrategy<Entity>();
        initialisationBuilder.setEntityType(entity);
        initialisationBuilder.setEntityNumber(20);

        initialisationBuilder.initialise(problem);

        verify(entity, times(20)).getClone();
    }
}
