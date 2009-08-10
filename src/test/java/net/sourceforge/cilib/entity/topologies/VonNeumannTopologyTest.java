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
package net.sourceforge.cilib.entity.topologies;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.NoSuchElementException;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.pso.particle.AbstractParticle;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Edwin Peer
 */
public class VonNeumannTopologyTest {

    @BeforeClass
    public static void setUp() {
        empty = new VonNeumannTopology<Particle>();
        square = new VonNeumannTopology<Particle>();

        for (int i = 0; i < 9; ++i) {
            Particle dumbParticle = new DumbParticle(String.valueOf(i + 1));
            square.add(dumbParticle);
        }

        irregular = new VonNeumannTopology<Particle>();

        for (int i = 0; i < 10; ++i) {
            Particle dumbParticle = new DumbParticle(String.valueOf(i + 1));
            irregular.add(dumbParticle);
        }
    }

    @Test
    public void testIteration() {
        Iterator<Particle> i = empty.iterator();
        assertFalse(i.hasNext());

        try {
            i.next();
            fail("NoSuchElementException should have been thown");
        }
        catch (NoSuchElementException ex) { }

        i = square.iterator();
        int count = 0;
        while (i.hasNext()) {
            DumbParticle particle = (DumbParticle) i.next();
            assertEquals(String.valueOf(id[count]), particle.getParticleName());
            ++count;
        }
        assertEquals(9, count);

        i = irregular.iterator();
        count = 0;
        while (i.hasNext()) {
            DumbParticle particle = (DumbParticle) i.next();
            assertEquals(String.valueOf(id[count]), particle.getParticleName());
            ++count;
        }
        assertEquals(10, count);
    }

    @Test
    public void testNeighbourhoodIteration() {
        Iterator<Particle> i = square.iterator();
        DumbParticle p = null;
        for (int c = 0; c < 5; ++c) {
            p = (DumbParticle) i.next();
        }
        assertEquals("4", p.getParticleName());

        Iterator<Particle> j = square.neighbourhood(i);

        int count = 0;
        int nid[] = {4, 3, 8, 6, 2};
        while (j.hasNext()) {
            p = (DumbParticle) j.next();
            assertEquals(String.valueOf(nid[count]), p.getParticleName());
            ++count;
        }
        assertEquals(5, count);

        try {
            j.next();
            fail("NoSuchElementException should have been thown");
        }
        catch (NoSuchElementException ex) { }

        i = irregular.iterator();
        p = (DumbParticle) i.next();
        assertEquals("1", p.getParticleName());

        j = irregular.neighbourhood(i);

        count = 0;
        int nnid[] = {1, 10, 3, 2, 7};
        while (j.hasNext()) {
            p = (DumbParticle) j.next();
            assertEquals(String.valueOf(nnid[count]), p.getParticleName());
            ++count;
        }
        assertEquals(5, count);

        for (int c = 0; c < 8; ++c) {
            p = (DumbParticle) i.next();
        }
        assertEquals("9", p.getParticleName());

        j = irregular.neighbourhood(i);

        count = 0;
        int nnnid[] = {9, 8, 5, 7, 6};
        while (j.hasNext()) {
            p = (DumbParticle) j.next();
            assertEquals(String.valueOf(nnnid[count]), p.getParticleName());
            ++count;
        }
        assertEquals(5, count);

        p = (DumbParticle) i.next();
        assertEquals("10", p.getParticleName());

        j = irregular.neighbourhood(i);

        count = 0;
        int nnnnid[] = {10, 5, 10, 1, 10};
        while (j.hasNext()) {
            p = (DumbParticle) j.next();
            assertEquals(String.valueOf(nnnnid[count]), p.getParticleName());
            ++count;
        }
        assertEquals(5, count);
    }

    @Test
    public void topologyAsList() {
        assertEquals(9, square.asList().size());
        assertEquals(10, irregular.asList().size());
    }

    private static Topology<Particle> empty;
    private static Topology<Particle> square;
    private static Topology<Particle> irregular;

    private int[] id = {1, 3, 7, 2, 4, 8, 5, 6, 9, 10};

    private static class DumbParticle extends AbstractParticle {
        private static final long serialVersionUID = 4273664052866515691L;

        private String name;

        public DumbParticle(String name) {
            this.name = name;
        }

        public String getParticleName() {
            return name;
        }

        public void setId(String id) {
            this.name = id;
        }

        public DumbParticle getClone() {
            throw new UnsupportedOperationException("Mocked object - not allowed");
        }

        public Fitness getBestFitness() {
            throw new UnsupportedOperationException("Mocked object - not allowed");
        }

        public int getDimension() {
            throw new UnsupportedOperationException("Mocked object - not allowed");
        }

        public void initialise(OptimisationProblem problem) {
            throw new UnsupportedOperationException("Mocked object - not allowed");
        }

        public Vector getPosition() {
            throw new UnsupportedOperationException("Mocked object - not allowed");
        }

        public Vector getBestPosition() {
            throw new UnsupportedOperationException("Mocked object - not allowed");
        }

        public Vector getVelocity() {
            throw new UnsupportedOperationException("Mocked object - not allowed");
        }

        public void updateControlParameters() {
            throw new UnsupportedOperationException("Mocked object - not allowed");
        }

        public void setNeighbourhoodBest(Particle particle) {
            throw new UnsupportedOperationException("Mocked object - not allowed");
        }

        public Particle getNeighbourhoodBest() {
            throw new UnsupportedOperationException("Mocked object - not allowed");
        }

        public void updatePosition() {
            throw new UnsupportedOperationException("Mocked object - not allowed");
        }

        public void updateVelocity() {
            throw new UnsupportedOperationException("Mocked object - not allowed");
        }

        public void reinitialise() {
            throw new UnsupportedOperationException("Mocked object - not allowed");
        }

        @Override
        public void calculateFitness() {
            throw new UnsupportedOperationException("Mocked object - not allowed");
        }

        @Override
        public boolean equals(Object object) {
            if (this == object)
                return true;

            if ((object == null) || (this.getClass() != object.getClass()))
                return false;

            DumbParticle other = (DumbParticle) object;
            return this.name.equals(other.name);
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 31 * hash + (this.name == null ? 0 : this.name.hashCode());
            return hash;
        }

    }

}
