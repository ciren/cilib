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
package net.sourceforge.cilib.measurement.single.moo;

import junit.framework.Assert;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.moo.archive.Archive;
import net.sourceforge.cilib.problem.Fitnesses;
import net.sourceforge.cilib.problem.MOFitness;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import org.junit.After;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 */
public class SolutionDistributionTest {

    @Test
    public void testBiDistribution() {
        Algorithm algorithm = mock(Algorithm.class);

        MOFitness fitness01 = Fitnesses.create(new MinimisationFitness(3.0), new MinimisationFitness(0.0));
        final Type type01 = mock(Type.class, "type2_01");

        MOFitness fitness02 = Fitnesses.create(new MinimisationFitness(2.0), new MinimisationFitness(1.0));
        final Type type02 = mock(Type.class, "type2_02");

        // Cloning returns the mocked objects themselves.
        when(type01.getClone()).thenReturn(type01);
        when(type02.getClone()).thenReturn(type02);

        OptimisationSolution solution01 = new OptimisationSolution(type01, fitness01);
        OptimisationSolution solution02 = new OptimisationSolution(type02, fitness02);

        Archive archive = Archive.Provider.get();
        archive.add(solution01);
        archive.add(solution02);

        Real distribution = new SolutionDistribution().getValue(algorithm);
        Assert.assertEquals(0.0, distribution.doubleValue(), 0.0000000000001);
    }

    @Test
    public void testPerfectDistribution() {
        Algorithm algorithm = mock(Algorithm.class);

        MOFitness fitness01 = Fitnesses.create(new MinimisationFitness(3.0), new MinimisationFitness(0.0));
        final Type type01 = mock(Type.class, "type2_01");

        MOFitness fitness02 = Fitnesses.create(new MinimisationFitness(2.0), new MinimisationFitness(1.0));
        final Type type02 = mock(Type.class, "type2_02");

        MOFitness fitness03 = Fitnesses.create(new MinimisationFitness(1.0), new MinimisationFitness(2.0));
        final Type type03 = mock(Type.class, "type2_03");

        MOFitness fitness04 = Fitnesses.create(new MinimisationFitness(0.0), new MinimisationFitness(3.0));
        final Type type04 = mock(Type.class, "type2_04");

        // Cloning returns the mocked objects themselves.
        when(type01.getClone()).thenReturn(type01);
        when(type02.getClone()).thenReturn(type02);
        when(type03.getClone()).thenReturn(type03);
        when(type04.getClone()).thenReturn(type04);

        OptimisationSolution solution01 = new OptimisationSolution(type01, fitness01);
        OptimisationSolution solution02 = new OptimisationSolution(type02, fitness02);
        OptimisationSolution solution03 = new OptimisationSolution(type03, fitness03);
        OptimisationSolution solution04 = new OptimisationSolution(type04, fitness04);

        Archive archive = Archive.Provider.get();
        archive.add(solution01);
        archive.add(solution02);
        archive.add(solution03);
        archive.add(solution04);

        Real distribution = new SolutionDistribution().getValue(algorithm);
        Assert.assertEquals(0.0, distribution.doubleValue(), 0.0000000000001);
    }

    @Test
    public void testRandomDistribution() {
        Algorithm algorithm = mock(Algorithm.class);

        MOFitness fitness01 = Fitnesses.create(new MinimisationFitness(6.0), new MinimisationFitness(0.0));
        final Type type01 = mock(Type.class, "type1_01");

        MOFitness fitness02 = Fitnesses.create(new MinimisationFitness(5.0), new MinimisationFitness(1.0));
        final Type type02 = mock(Type.class, "type1_02");

        MOFitness fitness03 = Fitnesses.create(new MinimisationFitness(3.0), new MinimisationFitness(3.0));
        final Type type03 = mock(Type.class, "type1_03");

        MOFitness fitness04 = Fitnesses.create(new MinimisationFitness(0.0), new MinimisationFitness(6.0));
        final Type type04 = mock(Type.class, "type1_04");

        // Cloning returns the mocked objects themselves.
        when(type01.getClone()).thenReturn(type01);
        when(type02.getClone()).thenReturn(type02);
        when(type03.getClone()).thenReturn(type03);
        when(type04.getClone()).thenReturn(type04);

        OptimisationSolution solution01 = new OptimisationSolution(type01, fitness01);
        OptimisationSolution solution02 = new OptimisationSolution(type02, fitness02);
        OptimisationSolution solution03 = new OptimisationSolution(type03, fitness03);
        OptimisationSolution solution04 = new OptimisationSolution(type04, fitness04);

        Archive archive = Archive.Provider.get();
        archive.add(solution01);
        archive.add(solution02);
        archive.add(solution03);
        archive.add(solution04);

        Real distribution = new SolutionDistribution().getValue(algorithm);
        Assert.assertEquals(0.293150984989, distribution.doubleValue(), 0.0000000000001);
    }

    @After
    public void reset() {
        Archive.Provider.get().clear();
    }
}
