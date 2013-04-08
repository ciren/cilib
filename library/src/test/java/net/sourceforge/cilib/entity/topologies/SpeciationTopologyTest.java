/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.topologies;

import fj.P;
import fj.P2;
import fj.data.List;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.pso.particle.Particle;
import static net.sourceforge.cilib.niching.NichingFunctionsTest.createParticle;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.distancemeasure.DistanceMeasure;
import net.sourceforge.cilib.util.distancemeasure.EuclideanDistanceMeasure;
import static org.junit.Assert.*;
import org.junit.Test;

public class SpeciationTopologyTest {

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

        SpeciationTopology s = new SpeciationTopology() {
            @Override
            public int getIteration() {
                return 1;
            }            
        };
        
        s.setNeighbourhoodSize(ConstantControlParameter.of(3));
        s.setRadius(ConstantControlParameter.of(2.1));
        s.addAll(Arrays.asList(p3,p2,p1,p4,p5,p6,p7,p8,p9,p10));
        /*
        * 0 - 2,3
        * 1 - 8,9
        * 4 -
        * 5 - 6,7
        */
        //1
        Iterator<Particle> i = s.iterator();
        Particle c = i.next();
        assertEquals(c.getFitness().getValue(), 8.0, 0.0);

        Collection<Particle> n = s.neighbourhood(c);
        Iterator<Particle> nIter = n.iterator();
        assertEquals(nIter.next().getFitness().getValue(), 1.0, 0.0);
        assertEquals(nIter.next().getFitness().getValue(), 8.0, 0.0);
        assertEquals(nIter.next().getFitness().getValue(), 9.0, 0.0);
        assertFalse(nIter.hasNext());

        //2
        c = i.next();
        assertEquals(c.getFitness().getValue(), 1.0, 0.0);
        n = s.neighbourhood(c);
        nIter = n.iterator();
        assertEquals(nIter.next().getFitness().getValue(), 1.0, 0.0);
        assertEquals(nIter.next().getFitness().getValue(), 8.0, 0.0);
        assertEquals(nIter.next().getFitness().getValue(), 9.0, 0.0);
        assertFalse(nIter.hasNext());

        //3
        c = i.next();
        assertEquals(c.getFitness().getValue(), 9.0, 0.0);
        n = s.neighbourhood(c);
        nIter = n.iterator();
        assertEquals(nIter.next().getFitness().getValue(), 1.0, 0.0);
        assertEquals(nIter.next().getFitness().getValue(), 8.0, 0.0);
        assertEquals(nIter.next().getFitness().getValue(), 9.0, 0.0);
        assertFalse(nIter.hasNext());

        //4
        c = i.next();
        assertEquals(c.getFitness().getValue(), 3.0, 0.0);
        n = s.neighbourhood(c);
        nIter = n.iterator();
        assertEquals(nIter.next().getFitness().getValue(), 0.0, 0.0);
        assertEquals(nIter.next().getFitness().getValue(), 2.0, 0.0);
        assertEquals(nIter.next().getFitness().getValue(), 3.0, 0.0);
        assertFalse(nIter.hasNext());

        //5
        c = i.next();
        assertEquals(c.getFitness().getValue(), 4.0, 0.0);
        n = s.neighbourhood(c);
        nIter = n.iterator();
        assertEquals(nIter.next().getFitness().getValue(), 4.0, 0.0);
        assertFalse(nIter.hasNext());

        //6
        c = i.next();
        assertEquals(c.getFitness().getValue(), 0.0, 0.0);
        n = s.neighbourhood(c);
        nIter = n.iterator();
        assertEquals(nIter.next().getFitness().getValue(), 0.0, 0.0);
        assertEquals(nIter.next().getFitness().getValue(), 2.0, 0.0);
        assertEquals(nIter.next().getFitness().getValue(), 3.0, 0.0);
        assertFalse(nIter.hasNext());

        //7
        c = i.next();
        assertEquals(c.getFitness().getValue(), 2.0, 0.0);
        n = s.neighbourhood(c);
        nIter = n.iterator();
        assertEquals(nIter.next().getFitness().getValue(), 0.0, 0.0);
        assertEquals(nIter.next().getFitness().getValue(), 2.0, 0.0);
        assertEquals(nIter.next().getFitness().getValue(), 3.0, 0.0);
        assertFalse(nIter.hasNext());

        //8
        c = i.next();
        assertEquals(c.getFitness().getValue(), 6.0, 0.0);
        n = s.neighbourhood(c);
        nIter = n.iterator();
        assertEquals(nIter.next().getFitness().getValue(), 5.0, 0.0);
        assertEquals(nIter.next().getFitness().getValue(), 6.0, 0.0);
        assertEquals(nIter.next().getFitness().getValue(), 7.0, 0.0);
        assertFalse(nIter.hasNext());

        //9
        c = i.next();
        assertEquals(c.getFitness().getValue(), 5.0, 0.0);
        n = s.neighbourhood(c);
        nIter = n.iterator();
        assertEquals(nIter.next().getFitness().getValue(), 5.0, 0.0);
        assertEquals(nIter.next().getFitness().getValue(), 6.0, 0.0);
        assertEquals(nIter.next().getFitness().getValue(), 7.0, 0.0);
        assertFalse(nIter.hasNext());

        //10
        c = i.next();
        assertEquals(c.getFitness().getValue(), 7.0, 0.0);
        n = s.neighbourhood(c);
        nIter = n.iterator();
        assertEquals(nIter.next().getFitness().getValue(), 5.0, 0.0);
        assertEquals(nIter.next().getFitness().getValue(), 6.0, 0.0);
        assertEquals(nIter.next().getFitness().getValue(), 7.0, 0.0);
        assertFalse(nIter.hasNext());
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

        assertFalse(SpeciationTopology.inRadius(distance, radius, other).f(P.p(p1, 1)));
        assertTrue(SpeciationTopology.inRadius(distance, radius, other).f(P.p(p2, 1)));

        List<P2<Particle, Integer>> top = List.<Particle>list(p1, p2, other)
                .zipIndex()
                .filter(SpeciationTopology.inRadius(distance, radius, other));
        assertEquals(top.length(), 2);
    }

}