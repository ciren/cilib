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
package net.sourceforge.cilib.niching.creation;

import fj.Ord;
import fj.Ordering;
import fj.data.List;
import java.util.Arrays;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.niching.NichingSwarms;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;
import static org.junit.Assert.*;
import org.junit.Test;

public class VectorBasedNicheCreationStrategyTest {

    @Test
    public void testDot() {
        Particle nBest = createParticle(new MinimisationFitness(0.0), Vector.of(0.0, 0.0), Vector.of(1.0, 1.0));
        Particle p1 = createParticle(new MinimisationFitness(0.0), Vector.of(1.0, 0.0), Vector.of(2.0, 0.0));
        Particle p2 = createParticle(new MinimisationFitness(0.0), Vector.of(1.0, 0.0), Vector.of(2.0, 1.0));
        Particle p3 = createParticle(new MinimisationFitness(0.0), Vector.of(1.0, 0.0), Vector.of(2.0, 0.5));
        Particle p4 = createParticle(new MinimisationFitness(0.0), Vector.of(1.0, 0.0), Vector.of(2.0, -0.5));
        Particle p5 = createParticle(new MinimisationFitness(0.0), Vector.of(1.0, 0.0), Vector.of(2.0, -1.0));
        Particle p6 = createParticle(new MinimisationFitness(0.0), Vector.of(1.0, 0.0), Vector.of(2.0, -2.0));

        assertEquals(2.0, VectorBasedNicheCreationStrategy.dot(nBest).f(nBest), 0.0);
        assertEquals(1.0, VectorBasedNicheCreationStrategy.dot(nBest).f(p2), 0.0);
        assertEquals(0.5, VectorBasedNicheCreationStrategy.dot(nBest).f(p3), 0.0);
        assertEquals(0.0, VectorBasedNicheCreationStrategy.dot(nBest).f(p1), 0.0);
        assertEquals(-0.5, VectorBasedNicheCreationStrategy.dot(nBest).f(p4), 0.0);
        assertEquals(-1.0, VectorBasedNicheCreationStrategy.dot(nBest).f(p5), 0.0);
        assertEquals(-2.0, VectorBasedNicheCreationStrategy.dot(nBest).f(p6), 0.0);
    }

    @Test
    public void ltZero() {
        assertTrue(VectorBasedNicheCreationStrategy.ltZero.f(-1.0));
        assertTrue(VectorBasedNicheCreationStrategy.ltZero.f(-2.0));
        assertFalse(VectorBasedNicheCreationStrategy.ltZero.f(0.0));
        assertFalse(VectorBasedNicheCreationStrategy.ltZero.f(1.0));
        assertFalse(VectorBasedNicheCreationStrategy.ltZero.f(2.0));
    }

    @Test
    public void testFilter() {
        DistanceMeasure distanceMeasure = new EuclideanDistanceMeasure();
        Particle nBest = createParticle(new MinimisationFitness(0.0), Vector.of(0.0, 0.0), Vector.of(1.0, 1.0));
        Particle p1 = createParticle(new MinimisationFitness(0.0), Vector.of(1.0, 0.0), Vector.of(2.0, 1.0));
        Particle p6 = createParticle(new MinimisationFitness(0.0), Vector.of(1.0, 0.0), Vector.of(2.0, -2.0));

        assertTrue(VectorBasedNicheCreationStrategy.filter(distanceMeasure, nBest, 10.0).f(p1));
        assertFalse(VectorBasedNicheCreationStrategy.filter(distanceMeasure, nBest, 10.0).f(p6));
        assertFalse(VectorBasedNicheCreationStrategy.filter(distanceMeasure, nBest, 0.5).f(p1));
        assertFalse(VectorBasedNicheCreationStrategy.filter(distanceMeasure, nBest, 0.5).f(p6));
    }

    @Test
    public void testSortByDistance() {
        DistanceMeasure distanceMeasure = new EuclideanDistanceMeasure();
        Particle nBest = createParticle(new MinimisationFitness(0.0), Vector.of(0.0, 0.0), Vector.of(1.0, 0.0));
        Particle p1 = createParticle(new MinimisationFitness(0.0), Vector.of(1.0, 1.0), Vector.of(2.0, 1.0));
        Particle p2 = createParticle(new MinimisationFitness(0.0), Vector.of(1.0, -1.0), Vector.of(2.0, -2.0));
        Particle p3 = createParticle(new MinimisationFitness(0.0), Vector.of(0.5, 1.0), Vector.of(2.0, 1.0));

        assertEquals(Ordering.EQ, VectorBasedNicheCreationStrategy.sortByDistance(nBest, distanceMeasure).f(p1, p2));
        assertEquals(Ordering.LT, VectorBasedNicheCreationStrategy.sortByDistance(nBest, distanceMeasure).f(p1, p3));
        assertEquals(Ordering.GT, VectorBasedNicheCreationStrategy.sortByDistance(nBest, distanceMeasure).f(p3, p1));
        assertEquals(List.list(p2, p3).minimum(Ord.ord(VectorBasedNicheCreationStrategy.sortByDistance(nBest, distanceMeasure).curry())).getCandidateSolution(), Vector.of(1.0, -1.0));
    }

    @Test
    public void testEqualParticle() {
        Particle p1 = createParticle(new MinimisationFitness(0.0), Vector.of(1.0, 1.0), Vector.of(2.0, 1.0));
        Particle p2 = createParticle(new MinimisationFitness(0.0), Vector.of(1.0, 1.0), Vector.of(2.0, 1.0));
        Particle p3 = createParticle(new MinimisationFitness(0.0), Vector.of(0.5, 1.0), Vector.of(2.0, 1.0));
        Particle p4 = createParticle(new MinimisationFitness(0.0), Vector.of(1.0, 1.0), Vector.of(2.0, 2.0));
        Particle p5 = createParticle(new MinimisationFitness(1.0), Vector.of(1.0, 1.0), Vector.of(2.0, 1.0));

        assertTrue(VectorBasedNicheCreationStrategy.equalParticle.f(p1, p2));
        assertFalse(VectorBasedNicheCreationStrategy.equalParticle.f(p1, p3));
        assertFalse(VectorBasedNicheCreationStrategy.equalParticle.f(p1, p4));
        assertFalse(VectorBasedNicheCreationStrategy.equalParticle.f(p1, p5));
    }

    public static Particle createParticle(Fitness fitness, Vector position, Vector pBest) {
        Particle particle = new StandardParticle();

        particle.setCandidateSolution(position);
        particle.getProperties().put(EntityType.FITNESS, fitness);
        particle.getProperties().put(EntityType.Particle.BEST_POSITION, pBest);
        particle.getProperties().put(EntityType.Particle.BEST_FITNESS, fitness);

        return particle;
    }
}
