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
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
*
*/
public class DimensionBoundViolationsPerParticleTest {

    @Test
    public void testDimensionBoundViolationsPerParticle() {
        Particle p1 = new StandardParticle();
        Particle p2 = new StandardParticle();
        Particle p3 = new StandardParticle();
        Particle p4 = new StandardParticle();

        Bounds bounds = new Bounds(0.0, 2.0);
        p1.getProperties().put(EntityType.CANDIDATE_SOLUTION, vectorOf(bounds, 0.5, -1.0, 2.1));
        p2.getProperties().put(EntityType.CANDIDATE_SOLUTION, vectorOf(bounds, 1.0, 2.0, 2.0));
        p3.getProperties().put(EntityType.CANDIDATE_SOLUTION, vectorOf(bounds, -1.0,0.0,1.0));
        p4.getProperties().put(EntityType.CANDIDATE_SOLUTION, vectorOf(bounds, 3.0,2.0,-1.0));

        final Topology<Particle> topology = new GBestTopology<Particle>();
        topology.add(p1);
        topology.add(p2);
        topology.add(p3);
        topology.add(p4);

        final PopulationBasedAlgorithm pba = mock(PopulationBasedAlgorithm.class);
        when(pba.getTopology()).thenReturn((Topology) topology);
             
        Measurement m = new DimensionBoundViolationsPerParticle();
        Assert.assertEquals(Real.valueOf(1.25), m.getValue(pba));
    }

    private Vector vectorOf(Bounds bounds, double... values) {
        Vector.Builder vector = Vector.newBuilder();
        for (int i = 0; i < values.length; i++) {
            vector.add(Real.valueOf(values[i], bounds));
        }
        return vector.build();
    }
}
