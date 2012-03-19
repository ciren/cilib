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
package net.sourceforge.cilib.pso.positionprovider;

import junit.framework.Assert;
import net.sourceforge.cilib.controlparameter.BoundedModifiableControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.util.Vectors;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.pso.particle.ParameterizedParticle;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kristina
 */
public class StandardPositionProviderTest {
    
    public StandardPositionProviderTest() {
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
     * Test of get method, of class StandardPositionProvider.
     */
    @Test
    public void testGet() {
        System.out.println("get");
        StandardPositionProvider instance = new StandardPositionProvider();
        Particle particle = new StandardParticle();
        Vector position = Vector.of(1.0,1.0,1.0,1.0);
        particle.setCandidateSolution(position);
        particle.getProperties().put(EntityType.Particle.VELOCITY, Vector.of(2.0,2.0,2.0,2.0));
        Vector expectedResult = Vectors.sumOf((Vector) particle.getCandidateSolution(), (Vector) particle.getVelocity());
        Vector result = instance.get(particle);
        
        Assert.assertEquals(expectedResult, result);
    }

    /**
     * Test of getInertia method, of class StandardPositionProvider.
     */
    @Test
    public void testGetInertia() {
        System.out.println("getInertia");
        StandardPositionProvider instance = new StandardPositionProvider();
        ParameterizedParticle particle = new ParameterizedParticle();
        double position = 5.0;
        BoundedModifiableControlParameter parameter = new BoundedModifiableControlParameter();
        parameter.setParameter(position);
        particle.setInertia(parameter);
        particle.getInertia().setVelocity(2.0);
        double expectedResult = particle.getInertia().getParameter() + particle.getInertia().getVelocity();
        double result = instance.getInertia(particle);
        
        Assert.assertEquals(expectedResult, result);
        
        parameter.setLowerBound(3.0);
        parameter.setUpperBound(5.0);
        particle.setInertia(parameter);
        expectedResult = particle.getInertia().getParameter();
        result = instance.getInertia(particle);
        
        Assert.assertEquals(expectedResult, result);
    }

    /**
     * Test of getSocial method, of class StandardPositionProvider.
     */
    @Test
    public void testGetSocial() {
        System.out.println("getSocial");
        StandardPositionProvider instance = new StandardPositionProvider();
        ParameterizedParticle particle = new ParameterizedParticle();
        double position = 5.0;
        BoundedModifiableControlParameter parameter = new BoundedModifiableControlParameter();
        parameter.setParameter(position);
        particle.setSocialAcceleration(parameter);
        particle.getSocialAcceleration().setVelocity(2.0);
        double expectedResult = particle.getSocialAcceleration().getParameter() + particle.getSocialAcceleration().getVelocity();
        double result = instance.getSocialAcceleration(particle);
        
        Assert.assertEquals(expectedResult, result);
        
        parameter.setLowerBound(3.0);
        parameter.setUpperBound(5.0);
        particle.setSocialAcceleration(parameter);
        expectedResult = particle.getSocialAcceleration().getParameter();
        result = instance.getSocialAcceleration(particle);
        
        Assert.assertEquals(expectedResult, result);
    }

    /**
     * Test of getPersonal method, of class StandardPositionProvider.
     */
    @Test
    public void testGetPersonal() {
        System.out.println("getPersonal");
        StandardPositionProvider instance = new StandardPositionProvider();
        ParameterizedParticle particle = new ParameterizedParticle();
        double position = 5.0;
        BoundedModifiableControlParameter parameter = new BoundedModifiableControlParameter();
        parameter.setParameter(position);
        particle.setCognitiveAcceleration(parameter);
        particle.getCognitiveAcceleration().setVelocity(2.0);
        double expectedResult = particle.getCognitiveAcceleration().getParameter() + particle.getCognitiveAcceleration().getVelocity();
        double result = instance.getCognitiveAcceleration(particle);
        
        Assert.assertEquals(expectedResult, result);
        
        parameter.setLowerBound(3.0);
        parameter.setUpperBound(5.0);
        particle.setCognitiveAcceleration(parameter);
        expectedResult = particle.getCognitiveAcceleration().getParameter();
        result = instance.getCognitiveAcceleration(particle);
        
        Assert.assertEquals(expectedResult, result);
    }

    /**
     * Test of getVmax method, of class StandardPositionProvider.
     */
    @Test
    public void testGetVmax() {
        System.out.println("getVmax");
        StandardPositionProvider instance = new StandardPositionProvider();
        ParameterizedParticle particle = new ParameterizedParticle();
        double position = 5.0;
        BoundedModifiableControlParameter parameter = new BoundedModifiableControlParameter();
        parameter.setParameter(position);
        particle.setVmax(parameter);
        particle.getVmax().setVelocity(2.0);
        double expectedResult = particle.getVmax().getParameter() + particle.getVmax().getVelocity();
        double result = instance.getVmax(particle);
        
        Assert.assertEquals(expectedResult, result);
        
        parameter.setLowerBound(3.0);
        parameter.setUpperBound(5.0);
        particle.setVmax(parameter);
        expectedResult = particle.getVmax().getParameter();
        result = instance.getVmax(particle);
        
        Assert.assertEquals(expectedResult, result);
    }
    
    /*
     * Test isWithinBounds, of class StandardPositionProvider
     */
    public void testIsWithinBounds() {
       BoundedModifiableControlParameter parameter = new BoundedModifiableControlParameter();
       parameter.setLowerBound(0.3);
       parameter.setUpperBound(0.6);
       parameter.setParameter(0.44);
       
       StandardPositionProvider instance = new StandardPositionProvider();
       
       Assert.assertTrue(instance.isWithinBounds(parameter.getParameter(), parameter));
       
       parameter = new BoundedModifiableControlParameter();
       parameter.setLowerBound(0.3);
       parameter.setUpperBound(0.6);
       parameter.setParameter(0.9);
       
       Assert.assertFalse(instance.isWithinBounds(parameter.getParameter(), parameter));
    }
}
