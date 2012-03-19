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

import net.sourceforge.cilib.controlparameter.BoundedModifiableControlParameter;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.pso.particle.ParameterizedParticle;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import static org.hamcrest.CoreMatchers.is;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Kristina
 */
public class ParameterBoundaryConstraintTest {
    
    public ParameterBoundaryConstraintTest() {
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
     * Test of enforce method, of class ParameterBoundaryConstraint.
     */
    @Test
    public void testEnforce() {
        System.out.println("enforce");
        
        Bounds bounds = new Bounds(-5.0, 5.0);
        Vector.Builder candidateSolutionBuilder = Vector.newBuilder();
        candidateSolutionBuilder.add(Real.valueOf(-6.0, bounds));
        candidateSolutionBuilder.add(Real.valueOf(3.0, bounds));
        candidateSolutionBuilder.add(Real.valueOf(6.0, bounds));

        ParameterBoundaryConstraint instance = new ParameterBoundaryConstraint();
        ParameterizedParticle particle = new ParameterizedParticle();
        particle.setCandidateSolution(candidateSolutionBuilder.build());
        
        BoundedModifiableControlParameter parameter = new BoundedModifiableControlParameter();
        parameter.setParameter(5.0);
        parameter.setLowerBound(1.0);
        parameter.setUpperBound(4.0);
        particle.setInertia(parameter);
        
        parameter = new BoundedModifiableControlParameter();
        parameter.setParameter(2.0);
        parameter.setLowerBound(1.0);
        parameter.setUpperBound(4.0);
        particle.setSocialAcceleration(parameter);
        
        parameter = new BoundedModifiableControlParameter();
        parameter.setParameter(-1.0);
        parameter.setLowerBound(1.0);
        parameter.setUpperBound(4.0);
        particle.setCognitiveAcceleration(parameter);
        
        ConstantControlParameter constParameter = new ConstantControlParameter();
        constParameter.setParameter(2.0);
        particle.setVmax(constParameter);
        

        ClampingBoundaryConstraint clampingBoundaryConstraint = new ClampingBoundaryConstraint();
        instance.setBoundaryConstraint(clampingBoundaryConstraint);
        ParameterizedParticle enforcedEntity = (ParameterizedParticle) instance.enforce(particle);

        Vector solution = (Vector) enforcedEntity.getCandidateSolution();
        Assert.assertThat(solution.doubleValueOf(0), is(-5.0));
        Assert.assertThat(solution.doubleValueOf(1), is(3.0));
        Assert.assertThat(solution.doubleValueOf(2), is(5.0 - Maths.EPSILON));
        
        Assert.assertThat(enforcedEntity.getInertia().getParameter(), is(4.0 - Maths.EPSILON));
        Assert.assertThat(enforcedEntity.getSocialAcceleration().getParameter(), is(2.0));
        Assert.assertThat(enforcedEntity.getCognitiveAcceleration().getParameter(), is(1.0));
        Assert.assertThat(enforcedEntity.getVmax().getParameter(), is(2.0));
    }

    /**
     * Test of setBoundaryConstraint method, of class ParameterBoundaryConstraint.
     */
    @Test
    public void testSetBoundaryConstraint() {
       System.out.println("setBoundaryConstraint");
       ParameterBoundaryConstraint instance = new ParameterBoundaryConstraint();
       instance.setBoundaryConstraint(new ClampingBoundaryConstraint());
       
       Assert.assertEquals(instance.getBoundaryConstraint().getClass(), ClampingBoundaryConstraint.class);
        
    }
    
     /**
     * Test of setBoundaryConstraint method, of class ParameterBoundaryConstraint.
     */
    @Test
    public void testGetBoundaryConstraint() {
       System.out.println("setBoundaryConstraint");
       ParameterBoundaryConstraint instance = new ParameterBoundaryConstraint();
       instance.setBoundaryConstraint(new ClampingBoundaryConstraint());
       
       Assert.assertEquals(instance.getBoundaryConstraint().getClass(), ClampingBoundaryConstraint.class);
        
    }
}
