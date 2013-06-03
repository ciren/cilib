/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.topologies;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import static net.sourceforge.cilib.niching.NichingFunctionsTest.createParticle;
import net.sourceforge.cilib.entity.Property;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.Test;

import fj.data.List;

public class SpeciationTopologyTest {

    public static Particle createParticle(Fitness fitness, Vector position) {
        Particle particle = new StandardParticle();

        particle.setPosition(position);
        particle.put(Property.FITNESS, fitness);
        particle.put(Property.BEST_POSITION, position);
        particle.put(Property.BEST_FITNESS, fitness);

        return particle;
    }

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

        List<Particle> particles = List.list(p3,p2,p1,p4,p5,p6,p7,p8,p9,p10);

        Neighbourhood<Particle> s = new SpeciationNeighbourhood<>(ConstantControlParameter.of(2.1), ConstantControlParameter.of(3));
        /*
        * 0 - 2,3
        * 1 - 8,9
        * 4 -
        * 5 - 6,7
        */
        //1
        List<Particle> n1 = s.f(particles, p1);
        assertTrue(n1.length() == 3);

        assertEquals(n1.index(0).getFitness().getValue(), 1.0, 0.0);
        assertEquals(n1.index(1).getFitness().getValue(), 8.0, 0.0);
        assertEquals(n1.index(2).getFitness().getValue(), 9.0, 0.0);

        //2
        List<Particle> n2 = s.f(particles, p2);
        assertEquals(n2.index(0).getFitness().getValue(), 1.0, 0.0);
        assertEquals(n2.index(1).getFitness().getValue(), 8.0, 0.0);
        assertEquals(n2.index(2).getFitness().getValue(), 9.0, 0.0);

        //3
        List<Particle> n3 = s.f(particles, p3);
        assertEquals(n3.index(0).getFitness().getValue(), 1.0, 0.0);
        assertEquals(n3.index(1).getFitness().getValue(), 8.0, 0.0);
        assertEquals(n3.index(2).getFitness().getValue(), 9.0, 0.0);

        //4
        List<Particle> n4 = s.f(particles, p4);
        assertEquals(n4.index(0).getFitness().getValue(), 0.0, 0.0);
        assertEquals(n4.index(1).getFitness().getValue(), 2.0, 0.0);
        assertEquals(n4.index(2).getFitness().getValue(), 3.0, 0.0);

        //5
        List<Particle> n5 = s.f(particles, p5);
        assertEquals(n5.index(0).getFitness().getValue(), 4.0, 0.0);

        //6
        List<Particle> n6 = s.f(particles, p6);
        assertEquals(n6.index(0).getFitness().getValue(), 0.0, 0.0);
        assertEquals(n6.index(1).getFitness().getValue(), 2.0, 0.0);
        assertEquals(n6.index(2).getFitness().getValue(), 3.0, 0.0);

        //7
        List<Particle> n7 = s.f(particles, p7);
        assertEquals(n7.index(0).getFitness().getValue(), 0.0, 0.0);
        assertEquals(n7.index(1).getFitness().getValue(), 2.0, 0.0);
        assertEquals(n7.index(2).getFitness().getValue(), 3.0, 0.0);

        //8
        List<Particle> n8 = s.f(particles, p8);
        assertEquals(n8.index(0).getFitness().getValue(), 5.0, 0.0);
        assertEquals(n8.index(1).getFitness().getValue(), 6.0, 0.0);
        assertEquals(n8.index(2).getFitness().getValue(), 7.0, 0.0);

        //9
        List<Particle> n9 = s.f(particles, p9);
        assertEquals(n9.index(0).getFitness().getValue(), 5.0, 0.0);
        assertEquals(n9.index(1).getFitness().getValue(), 6.0, 0.0);
        assertEquals(n9.index(2).getFitness().getValue(), 7.0, 0.0);

        //10
        List<Particle> n10 = s.f(particles, p10);
        assertEquals(n10.index(0).getFitness().getValue(), 5.0, 0.0);
        assertEquals(n10.index(1).getFitness().getValue(), 6.0, 0.0);
        assertEquals(n10.index(2).getFitness().getValue(), 7.0, 0.0);
    }

//    @Test
//    public void testInRadius() {
//        DistanceMeasure distance = new EuclideanDistanceMeasure();
//        ControlParameter radius = ConstantControlParameter.of(10.0);
//
//        Particle p1 = new StandardParticle();
//        p1.setPosition(Vector.of(10.0, 10.0));
//        Particle p2 = new StandardParticle();
//        p2.setPosition(Vector.of(5.0, 5.0));
//        Particle other = new StandardParticle();
//        other.setPosition(Vector.of(0.0, 0.0));
//
//        assertFalse(new SpeciationNeighbourhood<Particle>(distance, radius, other).f(P.p(p1, 1)));
//        assertTrue(SpeciationTopology.inRadius(distance, radius, other).f(P.p(p2, 1)));
//
//        List<P2<Particle, Integer>> top = List.<Particle>list(p1, p2, other)
//                .zipIndex()
//                .filter(SpeciationTopology.inRadius(distance, radius, other));
//        assertEquals(top.length(), 2);
//    }

//    @Test
//    public void testExists() {
//        Entity p1 = new StandardParticle();
//        p1.setPosition(Vector.of(10.0, 10.0));
//        Entity p2 = new StandardParticle();
//        p2.setPosition(Vector.of(5.0, 5.0));
//        Entity other = new StandardParticle();
//        other.setPosition(Vector.of(0.0, 0.0));
//
//        assertTrue(SpeciationTopology.exists(1).f(P.p(p1, 1)));
//        assertFalse(SpeciationTopology.exists(1).f(P.p(p1, 2)));
//
//        assertTrue(List.<Entity>list(p1, p2, other)
//                .zipIndex()
//                .removeAll(SpeciationTopology.exists(2))
//                .exists(SpeciationTopology.exists(1)));
//        assertFalse(List.<Entity>list(p1, p2, other)
//                .zipIndex()
//                .removeAll(SpeciationTopology.exists(1))
//                .exists(SpeciationTopology.exists(1)));
//    }
}
