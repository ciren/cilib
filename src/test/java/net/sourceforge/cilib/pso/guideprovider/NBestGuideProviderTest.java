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
package net.sourceforge.cilib.pso.guideprovider;

import junit.framework.Assert;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.ParameterAdaptingControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.pso.particle.ParameterizedParticle;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Kristina
 */
public class NBestGuideProviderTest {
    
    public NBestGuideProviderTest() {
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
     * Test of get method, of class NBestGuideProvider.
     */
    @Test
    public void testGet() {
        System.out.println("get");
        ParameterizedParticle particle = new ParameterizedParticle();
        NBestGuideProvider instance = new NBestGuideProvider();
        Vector expResult = Vector.of(1.0, 1.0, 1.0);
        
        particle.setCandidateSolution(expResult);
        particle.getProperties().put(EntityType.Particle.BEST_POSITION, expResult);
        particle.setNeighbourhoodBest(particle);
        Vector result = (Vector) instance.get(particle);
        Assert.assertEquals(expResult, result);
        
    }

    /**
     * Test of getInertia method, of class NBestGuideProvider.
     */
    @Test
    public void testGetInertia() {
        System.out.println("getInertia");
        ParameterizedParticle particle = new ParameterizedParticle();
        NBestGuideProvider instance = new NBestGuideProvider();
        particle.setInertia((ParameterAdaptingControlParameter) ConstantControlParameter.of(0.55));
        double expectedResult = 0.12;
        particle.setBestInertia((ParameterAdaptingControlParameter) ConstantControlParameter.of(expectedResult));
        
        particle.setNeighbourhoodBest(particle);
        ControlParameter result = instance.getInertia(particle);
        Assert.assertEquals(expectedResult, result.getParameter());
    }

    /**
     * Test of getSocial method, of class NBestGuideProvider.
     */
    @Test
    public void testGetSocial() {
        System.out.println("getSocial");
        ParameterizedParticle particle = new ParameterizedParticle();
        NBestGuideProvider instance = new NBestGuideProvider();
        particle.setSocialAcceleration((ParameterAdaptingControlParameter) ConstantControlParameter.of(0.55));
        double expectedResult = 0.12;
        particle.setBestSocialAcceleration((ParameterAdaptingControlParameter) ConstantControlParameter.of(expectedResult));
        
        particle.setNeighbourhoodBest(particle);
        ControlParameter result = instance.getSocialAcceleration(particle);
        Assert.assertEquals(expectedResult, result.getParameter());
    }

    /**
     * Test of getPersonal method, of class NBestGuideProvider.
     */
    @Test
    public void testGetPersonal() {
        System.out.println("getPersonal");
        ParameterizedParticle particle = new ParameterizedParticle();
        NBestGuideProvider instance = new NBestGuideProvider();
        particle.setCognitiveAcceleration((ParameterAdaptingControlParameter) ConstantControlParameter.of(0.55));
        double expectedResult = 0.12;
        particle.setBestCognitiveAcceleration((ParameterAdaptingControlParameter) ConstantControlParameter.of(expectedResult));
        
        particle.setNeighbourhoodBest(particle);
        ControlParameter result = instance.getCognitiveAcceleration(particle);
        Assert.assertEquals(expectedResult, result.getParameter());
    }

    /**
     * Test of getVmax method, of class NBestGuideProvider.
     */
    @Test
    public void testGetVmax() {
        System.out.println("getVmax");
       ParameterizedParticle particle = new ParameterizedParticle();
        NBestGuideProvider instance = new NBestGuideProvider();
        particle.setVmax((ParameterAdaptingControlParameter) ConstantControlParameter.of(0.55));
        double expectedResult = 0.12;
        particle.setBestVmax((ParameterAdaptingControlParameter) ConstantControlParameter.of(expectedResult));
        
        particle.setNeighbourhoodBest(particle);
        ControlParameter result = instance.getVmax(particle);
        Assert.assertEquals(expectedResult, result.getParameter());
    }
}
