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
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.ParameterAdaptingControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.pso.particle.ParameterizedParticle;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Kristina
 */
public class PBestGuideProviderTest {
    
    public PBestGuideProviderTest() {
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
     * Test of get method, of class PBestGuideProvider.
     */
    @Test
    public void testGet() {
        System.out.println("get");
        Particle particle = new StandardParticle();
        PBestGuideProvider instance = new PBestGuideProvider();
        Vector expectedResult = Vector.of(1.0,1.0,1.0,1.0);
        particle.getProperties().put(EntityType.Particle.BEST_POSITION, expectedResult);
        Vector result = (Vector) instance.get(particle);
        Assert.assertEquals(expectedResult, result);
    }

    /**
     * Test of getInertia method, of class PBestGuideProvider.
     */
    @Test
    public void testGetInertia() {
        System.out.println("getInertia");
        ParameterizedParticle particle = new ParameterizedParticle();
        PBestGuideProvider instance = new PBestGuideProvider();
        double expectedResult = 0.55;
        particle.setBestInertia((ParameterAdaptingControlParameter) ConstantControlParameter.of(expectedResult));
        ControlParameter result =  instance.getInertia(particle);
        Assert.assertEquals(expectedResult, result.getParameter());
    }

    /**
     * Test of getSocial method, of class PBestGuideProvider.
     */
    @Test
    public void testGetSocial() {
        System.out.println("getSocial");
        ParameterizedParticle particle = new ParameterizedParticle();
        PBestGuideProvider instance = new PBestGuideProvider();
        double expectedResult = 0.55;
        particle.setBestSocialAcceleration((ParameterAdaptingControlParameter) ConstantControlParameter.of(expectedResult));
        ControlParameter result =  instance.getSocialAcceleration(particle);
        Assert.assertEquals(expectedResult, result.getParameter());
    }

    /**
     * Test of getPersonal method, of class PBestGuideProvider.
     */
    @Test
    public void testGetPersonal() {
        System.out.println("getPersonal");
        ParameterizedParticle particle = new ParameterizedParticle();
        PBestGuideProvider instance = new PBestGuideProvider();
        double expectedResult = 0.55;
        particle.setBestCognitiveAcceleration((ParameterAdaptingControlParameter) ConstantControlParameter.of(expectedResult));
        ControlParameter result =  instance.getCognitiveAcceleration(particle);
        Assert.assertEquals(expectedResult, result.getParameter());
    }

    /**
     * Test of getVmax method, of class PBestGuideProvider.
     */
    @Test
    public void testGetVmax() {
        System.out.println("getVmax");
        ParameterizedParticle particle = new ParameterizedParticle();
        PBestGuideProvider instance = new PBestGuideProvider();
        double expectedResult = 0.55;
        particle.setBestVmax((ParameterAdaptingControlParameter) ConstantControlParameter.of(expectedResult));
        ControlParameter result =  instance.getVmax(particle);
        Assert.assertEquals(expectedResult, result.getParameter());
    }
}
