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
package net.sourceforge.cilib.problem.boundaryconstraint;

import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import org.junit.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CentroidBoundaryConstraintTest {
    
    public CentroidBoundaryConstraintTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of enforce method, of class CentroidBoundaryConstraint.
     */
    @Test
    public void testEnforce() {
        System.out.println("enforce");
        Bounds bounds = new Bounds(-5.0, 5.0);
        CentroidHolder candidateSolutionBuilder = new CentroidHolder();
        candidateSolutionBuilder.add(ClusterCentroid.of(Real.valueOf(-6.0, bounds)));
        candidateSolutionBuilder.add(ClusterCentroid.of(Real.valueOf(3.0, bounds)));
        candidateSolutionBuilder.add(ClusterCentroid.of(Real.valueOf(6.0, bounds)));

        ClusterParticle particle = new ClusterParticle();
        particle.setCandidateSolution(candidateSolutionBuilder);
        particle.getProperties().put(EntityType.Particle.VELOCITY, candidateSolutionBuilder);
        particle.getProperties().put(EntityType.Particle.BEST_POSITION, candidateSolutionBuilder);

        CentroidBoundaryConstraint constraint = new CentroidBoundaryConstraint();
        constraint.setDelegate(new ClampingBoundaryConstraint());
        constraint.enforce(particle);

        CentroidHolder solution = (CentroidHolder) particle.getCandidateSolution();
        System.out.println("Value: " + solution.get(0).get(0).doubleValue());
        Assert.assertTrue(solution.get(0).get(0).doubleValue() == -5.0);
        Assert.assertTrue(solution.get(1).get(0).doubleValue() == 3.0);
        Assert.assertTrue(solution.get(2).get(0).doubleValue() == (5.0 - Maths.EPSILON));
    }

    /**
     * Test of setDelegate method, of class CentroidBoundaryConstraint.
     */
    @Test
    public void testSetDelegate() {
        System.out.println("setDelegate");
        BoundaryConstraint constraint = new ReinitialisationBoundary();
        CentroidBoundaryConstraint instance = new CentroidBoundaryConstraint();
        instance.setDelegate(constraint);
        Assert.assertEquals(constraint, instance.delegate);
        
    }
}
