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
import junit.framework.Assert;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.GammaDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;
import net.sourceforge.cilib.pso.particle.ParameterizedParticle;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unfortunately all that can be tested is whether the velocity values actually change.
 * This is due to the random component involved in changing the velocity. Seeding is 
 * currently a problem in CILib, once that is fixed, this class can be tested properly.
 * 
 * @author Kristina
 */
public class NoisyVelocityProviderTest {
    
    public NoisyVelocityProviderTest() {
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
     * Test of get method, of class NoisyVelocityProvider.
     */
    @Test
    public void testGet() {
        System.out.println("get");
        ParameterizedParticle particle = new ParameterizedParticle();
        particle.setCandidateSolution(Vector.of(1.0,2.0,3.0));
        particle.getProperties().put(EntityType.Particle.VELOCITY, Vector.of(2.0,2.0,2.0));
        particle.getProperties().put(EntityType.Particle.BEST_POSITION, Vector.of(1.0,2.0,3.0));
        particle.setNeighbourhoodBest(particle);
        
        NoisyVelocityProvider instance = new NoisyVelocityProvider();
        Vector result = instance.get(particle);
        
        Assert.assertNotSame(result, Vector.of(2.0,2.0,2.0));
    }

    /**
     * Test of getDelegate method, of class NoisyVelocityProvider.
     */
    @Test
    public void testGetDelegate() {
        System.out.println("getDelegate");
        NoisyVelocityProvider instance = new NoisyVelocityProvider();
        instance.setDelegate(new ClampingVelocityProvider());
        VelocityProvider result = instance.getDelegate();
        
        Assert.assertEquals(result.getClass(), ClampingVelocityProvider.class);
    }

    /**
     * Test of setDelegate method, of class NoisyVelocityProvider.
     */
    @Test
    public void testSetDelegate() {
        System.out.println("setDelegate");
        NoisyVelocityProvider instance = new NoisyVelocityProvider();
        instance.setDelegate(new ClampingVelocityProvider());
        VelocityProvider result = instance.getDelegate();
        
        Assert.assertEquals(result.getClass(), ClampingVelocityProvider.class);
    }

    /**
     * Test of getDistribution method, of class NoisyVelocityProvider.
     */
    @Test
    public void testGetDistribution() {
        System.out.println("getDistribution");
        NoisyVelocityProvider instance = new NoisyVelocityProvider();
        instance.setDistribution(new GammaDistribution());
        ProbabilityDistributionFuction result = instance.getDistribution();
        
        Assert.assertEquals(result.getClass(), GammaDistribution.class);
    }

    /**
     * Test of setDistribution method, of class NoisyVelocityProvider.
     */
    @Test
    public void testSetDistribution() {
        System.out.println("setDistribution");
        NoisyVelocityProvider instance = new NoisyVelocityProvider();
        instance.setDistribution(new GammaDistribution());
        ProbabilityDistributionFuction result = instance.getDistribution();
        
        Assert.assertEquals(result.getClass(), GammaDistribution.class);
    }

    
    /**
     * Test of getControlParameterVelocity method, of class NoisyVelocityProvider.
     */
    @Test
    public void testGetControlParameterVelocity() {
        System.out.println("getControlParameterVelocity");
        ParameterizedParticle particle = new ParameterizedParticle();
        particle.setInertia(ConstantControlParameter.of(1.0));
        particle.setSocialAcceleration(ConstantControlParameter.of(1.0));
        particle.setCognitiveAcceleration(ConstantControlParameter.of(1.0));
        particle.setVmax(ConstantControlParameter.of(1.0));
        
        particle.getInertia().setVelocity(2.0);
        particle.getSocialAcceleration().setVelocity(2.0);
        particle.getCognitiveAcceleration().setVelocity(2.0);
        particle.getVmax().setVelocity(2.0);
        
        particle.setNeighbourhoodBest(particle);
        
        NoisyVelocityProvider instance = new NoisyVelocityProvider();
        
        HashMap<String, Double> result = instance.getControlParameterVelocity(particle);
        
        Assert.assertNotSame(result.get("InertiaVelocity"), 2.0);
        Assert.assertNotSame(result.get("SocialAccelerationVelocity"), 2.0);
        Assert.assertNotSame(result.get("CognitiveAccelerationVelocity"), 2.0);
        Assert.assertNotSame(result.get("VmaxVelocity"), 2.0);
    }
}
