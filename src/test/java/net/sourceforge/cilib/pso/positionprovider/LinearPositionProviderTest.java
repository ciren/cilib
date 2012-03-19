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
import net.sourceforge.cilib.pso.particle.ParameterizedParticle;

import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.entity.Particle;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class LinearPositionProviderTest {
    @Test
    public void testGet() {
        Particle particle = new StandardParticle();
        particle.getProperties().put(EntityType.CANDIDATE_SOLUTION,
                    Vector.of(Real.valueOf(1.0, new Bounds(-5.0, 5.0)),
                              Real.valueOf(2.0, new Bounds(-5.0, 5.0))));
        
        particle.getProperties().put(EntityType.Particle.VELOCITY,
                    Vector.of(Real.valueOf(0.0, new Bounds(-10.0, 10.0)),
                              Real.valueOf(0.0, new Bounds(-10.0, 10.0))));
        
        Vector updatedVector = new LinearPositionProvider().get(particle);
        
        //must have velocity's vector components and position's bounds
        assertEquals(updatedVector, Vector.of(Real.valueOf(0.0, new Bounds(-5.0, 5.0)),
                                              Real.valueOf(0.0, new Bounds(-5.0, 5.0))));
    }

    /**
     * Test of getInertia method, of class LinearPositionProvider.
     */
    @Test
    public void testGetInertia() {
        System.out.println("getInertia");
        ParameterizedParticle particle = new ParameterizedParticle();
        BoundedModifiableControlParameter parameter = new BoundedModifiableControlParameter();
        parameter.setParameter((Real.valueOf(1.0, new Bounds(-5.0, 5.0))).doubleValue());
        parameter.setVelocity((Real.valueOf(0.0, new Bounds(-10.0, 10.0))).doubleValue());
        particle.setInertia(parameter);
        
        double updatedPosition = new LinearPositionProvider().getInertia(particle);
        
        Assert.assertEquals(updatedPosition, Real.valueOf(0.0, new Bounds(-5.0, 5.0)).doubleValue());
        
    }

    /**
     * Test of getSocial method, of class LinearPositionProvider.
     */
    @Test
    public void testGetSocial() {
        System.out.println("getSocial");
        ParameterizedParticle particle = new ParameterizedParticle();
        BoundedModifiableControlParameter parameter = new BoundedModifiableControlParameter();
        parameter.setParameter((Real.valueOf(1.0, new Bounds(-5.0, 5.0))).doubleValue());
        parameter.setVelocity((Real.valueOf(0.0, new Bounds(-10.0, 10.0))).doubleValue());
        particle.setSocialAcceleration(parameter);
        
        double updatedPosition = new LinearPositionProvider().getSocialAcceleration(particle);
        
        Assert.assertEquals(updatedPosition, Real.valueOf(0.0, new Bounds(-5.0, 5.0)).doubleValue());
    }

    /**
     * Test of getPersonal method, of class LinearPositionProvider.
     */
    @Test
    public void testGetPersonal() {
        System.out.println("getPersonal");
        ParameterizedParticle particle = new ParameterizedParticle();
        BoundedModifiableControlParameter parameter = new BoundedModifiableControlParameter();
        parameter.setParameter((Real.valueOf(1.0, new Bounds(-5.0, 5.0))).doubleValue());
        parameter.setVelocity((Real.valueOf(0.0, new Bounds(-10.0, 10.0))).doubleValue());
        particle.setCognitiveAcceleration(parameter);
        
        double updatedPosition = new LinearPositionProvider().getCognitiveAcceleration(particle);
        
        Assert.assertEquals(updatedPosition, Real.valueOf(0.0, new Bounds(-5.0, 5.0)).doubleValue());
    }

    /**
     * Test of getVmax method, of class LinearPositionProvider.
     */
    @Test
    public void testGetVmax() {
        System.out.println("getVmax");
        ParameterizedParticle particle = new ParameterizedParticle();
        BoundedModifiableControlParameter parameter = new BoundedModifiableControlParameter();
        parameter.setParameter((Real.valueOf(1.0, new Bounds(-5.0, 5.0))).doubleValue());
        parameter.setVelocity((Real.valueOf(0.0, new Bounds(-10.0, 10.0))).doubleValue());
        particle.setVmax(parameter);
        
        double updatedPosition = new LinearPositionProvider().getVmax(particle);
        
        Assert.assertEquals(updatedPosition, Real.valueOf(0.0, new Bounds(-5.0, 5.0)).doubleValue());
    }
    
    /*
     * Test isWithinBounds, of class LinearPositionProvider
     */
    public void testIsWithinBounds() {
       BoundedModifiableControlParameter parameter = new BoundedModifiableControlParameter();
       parameter.setLowerBound(0.3);
       parameter.setUpperBound(0.6);
       parameter.setParameter(0.44);
       
       LinearPositionProvider instance = new LinearPositionProvider();
       
       Assert.assertTrue(instance.isWithinBounds(parameter.getParameter(), parameter));
       
       parameter = new BoundedModifiableControlParameter();
       parameter.setLowerBound(0.3);
       parameter.setUpperBound(0.6);
       parameter.setParameter(0.9);
       
       Assert.assertFalse(instance.isWithinBounds(parameter.getParameter(), parameter));
    }
}
