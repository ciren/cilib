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
package net.sourceforge.cilib.pso.velocityprovider;
import java.util.HashMap;
import net.sourceforge.cilib.controlparameter.BoundedModifiableControlParameter;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;import net.sourceforge.cilib.functions.continuous.unconstrained.Spherical;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.pso.particle.ParametizedParticle;

import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class ClampingVelocityProviderTest {

    private Particle createParticle(Vector vector) {
        Particle particle = new StandardParticle();
        particle.getProperties().put(EntityType.CANDIDATE_SOLUTION, vector);
        particle.getProperties().put(EntityType.Particle.VELOCITY, Vector.of(0.0));
        particle.getProperties().put(EntityType.Particle.BEST_POSITION, vector.getClone());
        return particle;
    }

    /**
     * Test velocity clamping.
     */
    @Test
    public void testGet() {
        Particle particle = createParticle(Vector.of(0.0));
        Particle nBest = createParticle(Vector.of(1.0));
        particle.setNeighbourhoodBest(nBest);
        nBest.setNeighbourhoodBest(nBest);

        ClampingVelocityProvider velocityProvider = new ClampingVelocityProvider();
        velocityProvider.setVMax(ConstantControlParameter.of(0.5));
        Vector velocity = velocityProvider.get(particle);

        for (Numeric number : velocity) {
            Assert.assertTrue(Double.compare(number.doubleValue(), 0.5) <= 0);
            Assert.assertTrue(Double.compare(number.doubleValue(), -0.5) >= 0);
        }    
    }

    /**
     * Test of setVMax method, of class ClampingVelocityProvider.
     */
    @Test
    public void testSetVMax() {
        System.out.println("setVMax");
        ClampingVelocityProvider provider = new ClampingVelocityProvider();
        ControlParameter expectedResult = new ConstantControlParameter(0.55);
        provider.setVMax(expectedResult);
        ControlParameter result = provider.getVMax();
        
        Assert.assertTrue(expectedResult.getParameter() - result.getParameter() == 0);
        
    }

    /**
     * Test of getVMax method, of class ClampingVelocityProvider.
     */
    @Test
    public void testGetVMax() {
        System.out.println("getVMax");
        ClampingVelocityProvider provider = new ClampingVelocityProvider();
        ControlParameter expectedResult = new ConstantControlParameter(0.55);
        provider.setVMax(expectedResult);
        ControlParameter result = provider.getVMax();
        
        Assert.assertTrue(expectedResult.getParameter() - result.getParameter() == 0);
       
    }

    /**
     * Test of setDelegate method, of class ClampingVelocityProvider.
     */
    @Test
    public void testSetDelegate() {
        System.out.println("setDelegate");
        ClampingVelocityProvider provider = new ClampingVelocityProvider();
        VelocityProvider expectedResult = new StandardVelocityProvider();
        provider.setDelegate(expectedResult);
        VelocityProvider result = provider.getDelegate();
        
        Assert.assertSame(expectedResult, result);
        
    }

    /**
     * Test of getDelegate method, of class ClampingVelocityProvider.
     */
    @Test
    public void testGetDelegate() {
        System.out.println("getDelegate");
        ClampingVelocityProvider provider = new ClampingVelocityProvider();
        VelocityProvider expectedResult = new StandardVelocityProvider();
        provider.setDelegate(expectedResult);
        VelocityProvider result = provider.getDelegate();
        
        Assert.assertSame(expectedResult, result);
    }

    /**
     * Test of setControlParameters method, of class ClampingVelocityProvider.
     */
    @Test
    public void testSetControlParameters() {
        System.out.println("setControlParameters");
        ClampingVelocityProvider instance = new ClampingVelocityProvider();
        ControlParameter parameter = new BoundedModifiableControlParameter();
        parameter.setParameter(0.55);
        ParametizedParticle particle = new ParametizedParticle();
        particle.setVmax(parameter);
        
        instance.setControlParameters(particle);
        
        Assert.assertTrue(parameter.getParameter() - instance.getVMax().getParameter() == 0);
    }

    /**
     * Test of getControlParameterVelocity method, of class ClampingVelocityProvider.
     */
    @Test
    public void testGetControlParameterVelocity() {
        System.out.println("getControlParameterVelocity");
        FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
        problem.setDomain("R(-5.12, 5.12)^30");
        problem.setFunction(new Spherical());
            
        ParametizedParticle particle = new ParametizedParticle();
        particle.initialise(problem);
        ControlParameter parameter = new BoundedModifiableControlParameter();
        parameter.setParameter(0.5);
        parameter.setVelocity(0);
        particle.setVmax(parameter);
        double velocityValue = new StandardVelocityProvider().getControlParameterVelocity(particle).get("VmaxVelocity");
        
        ClampingVelocityProvider velocityProvider = new ClampingVelocityProvider();
        StandardVelocityProvider standardProvider = new StandardVelocityProvider();
        velocityProvider.setVMax(particle.getVmax());
        double velocity = standardProvider.getControlParameterVelocity(particle).get("VmaxVelocity");
        HashMap<String, Double> result = velocityProvider.getControlParameterVelocity(particle);
        
        Assert.assertNotSame(velocity, result.get("VmaxVelocity"));
        Assert.assertTrue(Double.compare(result.get("VmaxVelocity"), 0.5) <= 0);
        Assert.assertTrue(Double.compare(result.get("VmaxVelocity"), -0.5) >= 0);

    }
}
