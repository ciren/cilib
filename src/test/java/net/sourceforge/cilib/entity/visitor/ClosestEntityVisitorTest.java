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
package net.sourceforge.cilib.entity.visitor;

import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests based on the usage of the ClosestEntityVisitor.
 * @author gpampara
 */
public class ClosestEntityVisitorTest {

    /**
     * Determine the closest entity to the provided entity.
     */
    @Test
    public void closestEntity() {
        Individual individual1 = new Individual();
        Individual individual2 = new Individual();
        Individual individual3 = new Individual();

        Vector vector1 = new Vector(5, new Real(0.0));
        Vector vector2 = new Vector(5, new Real(1.0));
        Vector vector3 = new Vector(5, new Real(2.0));

        individual1.setCandidateSolution(vector1);
        individual2.setCandidateSolution(vector2);
        individual3.setCandidateSolution(vector3);

        Topology<Individual> topology = new GBestTopology<Individual>();
        topology.add(individual1);
        topology.add(individual2);
        topology.add(individual3);

        ClosestEntityVisitor visitor = new ClosestEntityVisitor();
        visitor.setTargetEntity(individual1);
        topology.accept(visitor);

        Assert.assertTrue(individual2 == visitor.getResult());
    }

}
