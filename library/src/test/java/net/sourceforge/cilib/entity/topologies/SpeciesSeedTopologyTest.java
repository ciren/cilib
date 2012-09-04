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
package net.sourceforge.cilib.entity.topologies;

import fj.P;
import fj.P2;
import fj.data.List;
import java.util.Arrays;
import java.util.Iterator;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import static net.sourceforge.cilib.niching.NichingFunctionsTest.createParticle;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;
import static org.junit.Assert.*;
import org.junit.Test;

public class SpeciesSeedTopologyTest {

    @Test
    public void testNeighbourhood() {
        Particle p1 = createParticle(new MinimisationFitness(9.0), Vector.of(1.0)); //0
        Particle p2 = createParticle(new MinimisationFitness(1.0), Vector.of(2.0)); //1
        Particle p3 = createParticle(new MinimisationFitness(8.0), Vector.of(3.0)); //2
        Particle p4 = createParticle(new MinimisationFitness(3.0), Vector.of(4.0)); //3
        Particle p5 = createParticle(new MinimisationFitness(4.0), Vector.of(5.0)); //4
        Particle p6 = createParticle(new MinimisationFitness(0.0), Vector.of(6.0)); //5
        Particle p7 = createParticle(new MinimisationFitness(2.0), Vector.of(7.0)); //6
        Particle p8 = createParticle(new MinimisationFitness(6.0), Vector.of(8.0)); //7
        Particle p9 = createParticle(new MinimisationFitness(5.0), Vector.of(9.0)); //8
        Particle p10 = createParticle(new MinimisationFitness(7.0), Vector.of(10.0)); //9
        
        SpeciesSeedTopology t = new SpeciesSeedTopology();
        t.setNeighbourhoodSize(ConstantControlParameter.of(3));
        t.setRadius(ConstantControlParameter.of(2.1));
        t.addAll(Arrays.asList(p3,p2,p1,p4,p5,p6,p7,p8,p9,p10));
        /*
         * 0 - 2,3
         * 1 - 8,9
         * 4 - 
         * 5 - 6,7
         */
        //1
        Iterator<Particle> i = t.iterator();
        assertEquals(i.next().getFitness().getValue(), 8.0, 0.0);
        
        Iterator<Particle> n = t.neighbourhood(i);
        assertEquals(n.next().getFitness().getValue(), 1.0, 0.0);
        assertEquals(n.next().getFitness().getValue(), 8.0, 0.0);
        assertEquals(n.next().getFitness().getValue(), 9.0, 0.0);
        assertFalse(n.hasNext());
        
        //2
        assertEquals(i.next().getFitness().getValue(), 1.0, 0.0);
        n = t.neighbourhood(i);
        assertEquals(n.next().getFitness().getValue(), 1.0, 0.0);
        assertEquals(n.next().getFitness().getValue(), 8.0, 0.0);
        assertEquals(n.next().getFitness().getValue(), 9.0, 0.0);
        assertFalse(n.hasNext());
        
        //3
        assertEquals(i.next().getFitness().getValue(), 9.0, 0.0);
        n = t.neighbourhood(i);
        assertEquals(n.next().getFitness().getValue(), 1.0, 0.0);
        assertEquals(n.next().getFitness().getValue(), 8.0, 0.0);
        assertEquals(n.next().getFitness().getValue(), 9.0, 0.0);
        assertFalse(n.hasNext());
        
        //4
        assertEquals(i.next().getFitness().getValue(), 3.0, 0.0);
        n = t.neighbourhood(i);
        assertEquals(n.next().getFitness().getValue(), 0.0, 0.0);
        assertEquals(n.next().getFitness().getValue(), 2.0, 0.0);
        assertEquals(n.next().getFitness().getValue(), 3.0, 0.0);
        assertFalse(n.hasNext());
        
        //5
        assertEquals(i.next().getFitness().getValue(), 4.0, 0.0);
        n = t.neighbourhood(i);
        assertEquals(n.next().getFitness().getValue(), 4.0, 0.0);
        assertFalse(n.hasNext());
        
        //6
        assertEquals(i.next().getFitness().getValue(), 0.0, 0.0);
        n = t.neighbourhood(i);
        assertEquals(n.next().getFitness().getValue(), 0.0, 0.0);
        assertEquals(n.next().getFitness().getValue(), 2.0, 0.0);
        assertEquals(n.next().getFitness().getValue(), 3.0, 0.0);
        assertFalse(n.hasNext());
        
        //7
        assertEquals(i.next().getFitness().getValue(), 2.0, 0.0);
        n = t.neighbourhood(i);
        assertEquals(n.next().getFitness().getValue(), 0.0, 0.0);
        assertEquals(n.next().getFitness().getValue(), 2.0, 0.0);
        assertEquals(n.next().getFitness().getValue(), 3.0, 0.0);
        assertFalse(n.hasNext());
        
        //8
        assertEquals(i.next().getFitness().getValue(), 6.0, 0.0);
        n = t.neighbourhood(i);
        assertEquals(n.next().getFitness().getValue(), 5.0, 0.0);
        assertEquals(n.next().getFitness().getValue(), 6.0, 0.0);
        assertEquals(n.next().getFitness().getValue(), 7.0, 0.0);
        assertFalse(n.hasNext());
        
        //9
        assertEquals(i.next().getFitness().getValue(), 5.0, 0.0);
        n = t.neighbourhood(i);
        assertEquals(n.next().getFitness().getValue(), 5.0, 0.0);
        assertEquals(n.next().getFitness().getValue(), 6.0, 0.0);
        assertEquals(n.next().getFitness().getValue(), 7.0, 0.0);
        assertFalse(n.hasNext());
        
        //10
        assertEquals(i.next().getFitness().getValue(), 7.0, 0.0);
        n = t.neighbourhood(i);
        assertEquals(n.next().getFitness().getValue(), 5.0, 0.0);
        assertEquals(n.next().getFitness().getValue(), 6.0, 0.0);
        assertEquals(n.next().getFitness().getValue(), 7.0, 0.0);
        assertFalse(n.hasNext());
    }

    @Test
    public void testInRadius() {
        DistanceMeasure distance = new EuclideanDistanceMeasure();
        ControlParameter radius = ConstantControlParameter.of(10.0);
        
        Particle p1 = new StandardParticle();
        p1.setCandidateSolution(Vector.of(10.0, 10.0));
        Particle p2 = new StandardParticle();
        p2.setCandidateSolution(Vector.of(5.0, 5.0));
        Particle other = new StandardParticle();
        other.setCandidateSolution(Vector.of(0.0, 0.0));
        
        assertFalse(SpeciesSeedTopology.inRadius(distance, radius, other).f(P.p(p1, 1)));
        assertTrue(SpeciesSeedTopology.inRadius(distance, radius, other).f(P.p(p2, 1)));
        
        List<P2<Particle, Integer>> top = List.<Particle>list(p1, p2, other)
                .zipIndex()
                .filter(SpeciesSeedTopology.inRadius(distance, radius, other));
        assertEquals(top.length(), 2);
    }

    @Test
    public void testExists() {
        Entity p1 = new StandardParticle();
        p1.setCandidateSolution(Vector.of(10.0, 10.0));
        Entity p2 = new StandardParticle();
        p2.setCandidateSolution(Vector.of(5.0, 5.0));
        Entity other = new StandardParticle();
        other.setCandidateSolution(Vector.of(0.0, 0.0));
        
        assertTrue(SpeciesSeedTopology.exists(1).f(P.p(p1, 1)));
        assertFalse(SpeciesSeedTopology.exists(1).f(P.p(p1, 2)));
        
        assertTrue(List.<Entity>list(p1, p2, other)
                .zipIndex()
                .removeAll(SpeciesSeedTopology.exists(2))
                .exists(SpeciesSeedTopology.exists(1)));
        assertFalse(List.<Entity>list(p1, p2, other)
                .zipIndex()
                .removeAll(SpeciesSeedTopology.exists(1))
                .exists(SpeciesSeedTopology.exists(1)));
    }
}
