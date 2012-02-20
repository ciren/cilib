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
package net.sourceforge.cilib.entity.initialization;

import net.sourceforge.cilib.math.Maths;
import junit.framework.Assert;
import net.sourceforge.cilib.controlparameter.BoundedModifiableControlParameter;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.functions.continuous.unconstrained.Spherical;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.pso.particle.ParametizedParticle;
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
public class ParameterInclusiveInitializationStrategyTest {
    
    public ParameterInclusiveInitializationStrategyTest() {
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
     * Test of initialize method, of class ParameterInclusiveInitializationStrategy.
     */
    @Test
    public void testInitialize() {
        System.out.println("initialize");
        
        //Set up test
        FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
        problem.setDomain("R(-5.12, 5.12)^10");
        problem.setFunction(new Spherical());
        
        Vector vector = Vector.of(1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0);
        ParametizedParticle individual = new ParametizedParticle();
        individual.getProperties().put(EntityType.CANDIDATE_SOLUTION, vector.getClone());
        
        ControlParameter modifiableParameter = new BoundedModifiableControlParameter();
        ControlParameter constantParameter = new ConstantControlParameter(0.55);
        
        ParameterInclusiveInitializationStrategy initializationStrategy = new ParameterInclusiveInitializationStrategy();
        initializationStrategy.setInertia(modifiableParameter);
        initializationStrategy.setSocialAcceleration(modifiableParameter);
        initializationStrategy.setCognitiveAcceleration(constantParameter);
        initializationStrategy.setVmax(constantParameter);
        
        initializationStrategy.initialize(EntityType.CANDIDATE_SOLUTION, individual);
        
        Vector particle = (Vector) individual.getCandidateSolution();
        
        //Test that the candidate solution was actually initialized to something different from the one created above
        for(int i = 0; i < particle.size(); i++) {
            Assert.assertNotSame(vector.doubleValueOf(i), particle.get(i));
        }
        
        //Test that the parameters are not the default ones
        Assert.assertNotSame(individual.getInertia().getParameter(), 0.1);
        Assert.assertNotSame(individual.getSocialAcceleration().getParameter(), 0.1);
        
        //Test that the constants remained the same
        Assert.assertEquals(individual.getCognitiveAcceleration().getParameter(), 0.55);
        Assert.assertNotSame(individual.getVmax().getParameter(), 0.55);
    }
    
     @Test
    public void testInitializeWhenSetFails() {
        System.out.println("initializeFails");
        
        //Set up test
        FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
        problem.setDomain("R(-5.12, 5.12)^10");
        problem.setFunction(new Spherical());
        
        Vector vector = Vector.of(1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0);
        ParametizedParticle individual = new ParametizedParticle();
        individual.getProperties().put(EntityType.CANDIDATE_SOLUTION, vector.getClone());
        
        ControlParameter modifiableParameter = new BoundedModifiableControlParameter();
        modifiableParameter.setParameter(0.12);
        ControlParameter constantParameter = new ConstantControlParameter(0.55);
        
        ParameterInclusiveInitializationStrategy initializationStrategy = new ParameterInclusiveInitializationStrategy();
        initializationStrategy.setInertia(modifiableParameter);
        initializationStrategy.setSocialAcceleration(modifiableParameter);
        initializationStrategy.setCognitiveAcceleration(constantParameter);
        initializationStrategy.setVmax(constantParameter);
        
        initializationStrategy.initialize(EntityType.CANDIDATE_SOLUTION, individual);
        
        Vector particle = (Vector) individual.getCandidateSolution();
        
        //Test that the candidate solution was actually initialized to something different from the one created above
        for(int i = 0; i < particle.size(); i++) {
            Assert.assertNotSame(vector.doubleValueOf(i), particle.get(i));
        }
        
        //Test that the parameters are not the default ones
        Assert.assertEquals(individual.getInertia().getParameter(), 0.12);
        Assert.assertEquals(individual.getSocialAcceleration().getParameter(), 0.12);
        
        //Test that the constants remained the same
        Assert.assertEquals(individual.getCognitiveAcceleration().getParameter(), 0.55);
        Assert.assertEquals(individual.getVmax().getParameter(), 0.55);
    }

    /**
     * Test of getEntityInitializationStrategy method, of class ParameterInclusiveInitializationStrategy.
     */
    @Test
    public void testGetEntityInitializationStrategy() {
        System.out.println("getEntityInitializationStrategy");
        ParameterInclusiveInitializationStrategy instance = new ParameterInclusiveInitializationStrategy();
        InitializationStrategy result = instance.getEntityInitializationStrategy();
        InitializationStrategy expResult = new RandomBoundedInitializationStrategy();
        assertEquals(expResult.getClass(), result.getClass());
    }

    /**
     * Test of setEntityInitializationStrategy method, of class ParameterInclusiveInitializationStrategy.
     */
    @Test
    public void testSetEntityInitializationStrategy() {
        System.out.println("setEntityInitializationStrategy");
        InitializationStrategy initializationStrategy = new DomainPercentageInitializationStrategy();
        ParameterInclusiveInitializationStrategy instance = new ParameterInclusiveInitializationStrategy();
        instance.setEntityInitializationStrategy(initializationStrategy);
        assertEquals(instance.getEntityInitializationStrategy().getClass(), initializationStrategy.getClass());
    }

    /**
     * Test of setInertia method, of class ParameterInclusiveInitializationStrategy.
     */
    @Test
    public void testSetInertia() {
        System.out.println("setInertia");
        ParameterInclusiveInitializationStrategy instance = new ParameterInclusiveInitializationStrategy();
        
        //Test setting a constant parameter works
        ControlParameter parameter = new ConstantControlParameter(0.1);
        instance.setInertia(parameter);
        Assert.assertEquals(instance.getInertia().getParameter(), parameter.getParameter());
        
        //Test setting a modifiable parameter results in the correct values
        parameter = new BoundedModifiableControlParameter();
        parameter.setParameter(0.1);
        instance.setInertia(parameter);
        Assert.assertEquals(instance.getInertia().getParameter(), parameter.getParameter());
    }

    /**
     * Test of setSocial method, of class ParameterInclusiveInitializationStrategy.
     */
    @Test
    public void testSetSocial() {
        System.out.println("setSocial");
        ParameterInclusiveInitializationStrategy instance = new ParameterInclusiveInitializationStrategy();
        
        //Test setting a constant parameter works
        ControlParameter parameter = new ConstantControlParameter(0.1);
        instance.setSocialAcceleration(parameter);
        Assert.assertEquals(instance.getSocialAcceleration().getParameter(), parameter.getParameter());
        
        //Test setting a modifiable parameter results in the correct values
        parameter = new BoundedModifiableControlParameter();
        parameter.setParameter(0.1);
        instance.setSocialAcceleration(parameter);
        Assert.assertEquals(instance.getSocialAcceleration().getParameter(), parameter.getParameter());
    }

    /**
     * Test of setPersonal method, of class ParameterInclusiveInitializationStrategy.
     */
    @Test
    public void testSetPersonal() {
        System.out.println("setPersonal");
        ParameterInclusiveInitializationStrategy instance = new ParameterInclusiveInitializationStrategy();
        
        //Test setting a constant parameter works
        ControlParameter parameter = new ConstantControlParameter(0.1);
        instance.setCognitiveAcceleration(parameter);
        Assert.assertEquals(instance.getPersonal().getParameter(), parameter.getParameter());
        
        //Test setting a modifiable parameter results in the correct values
        parameter = new BoundedModifiableControlParameter();
        parameter.setParameter(0.1);
        instance.setCognitiveAcceleration(parameter);
        Assert.assertEquals(instance.getPersonal().getParameter(), parameter.getParameter());
    }

    /**
     * Test of setVmax method, of class ParameterInclusiveInitializationStrategy.
     */
    @Test
    public void testSetVmax() {
        ParameterInclusiveInitializationStrategy instance = new ParameterInclusiveInitializationStrategy();
        
        //Test setting a constant parameter works
        ControlParameter parameter = new ConstantControlParameter(0.1);
        instance.setVmax(parameter);
        Assert.assertEquals(instance.getVmax().getParameter(), parameter.getParameter());
        
        //Test setting a modifiable parameter results in the correct values
        parameter = new BoundedModifiableControlParameter();
        parameter.setParameter(0.1);
        instance.setVmax(parameter);
        Assert.assertEquals(instance.getVmax().getParameter(), parameter.getParameter());
    }

    /**
     * Test of getLowerBoundInertia method, of class ParameterInclusiveInitializationStrategy.
     */
    @Test
    public void testGetLowerBoundInertia() {
        System.out.println("getLowerBoundInertia");
        ParameterInclusiveInitializationStrategy instance = new ParameterInclusiveInitializationStrategy();
        ControlParameter expResult = new ConstantControlParameter(0.1); //default value
        ControlParameter result = instance.getLowerBoundInertia();
        
        Assert.assertEquals(expResult.getParameter(), result.getParameter());
        
    }

    /**
     * Test of getUpperBoundInertia method, of class ParameterInclusiveInitializationStrategy.
     */
    @Test
    public void testGetUpperBoundInertia() {
        System.out.println("getUpperBoundInertia");
        ParameterInclusiveInitializationStrategy instance = new ParameterInclusiveInitializationStrategy();
        ControlParameter expResult = new ConstantControlParameter(0.9); //default value
        ControlParameter result = instance.getUpperBoundInertia();
        
        Assert.assertEquals(expResult.getParameter(), result.getParameter());
        
    }

    /**
     * Test of getLowerBoundSocial method, of class ParameterInclusiveInitializationStrategy.
     */
    @Test
    public void testGetLowerBoundSocial() {
        System.out.println("getLowerBoundSocial");
       ParameterInclusiveInitializationStrategy instance = new ParameterInclusiveInitializationStrategy();
        ControlParameter expResult = new ConstantControlParameter(0.1); //default value
        ControlParameter result = instance.getLowerBoundSocialAcceleration();
        
        Assert.assertEquals(expResult.getParameter(), result.getParameter());
        
    }

    /**
     * Test of getUpperBoundSocial method, of class ParameterInclusiveInitializationStrategy.
     */
    @Test
    public void testGetUpperBoundSocial() {
        System.out.println("getUpperBoundSocial");
        ParameterInclusiveInitializationStrategy instance = new ParameterInclusiveInitializationStrategy();
        ControlParameter expResult = new ConstantControlParameter(0.9); //default value
        ControlParameter result = instance.getUpperBoundSocialAcceleration();
        
        Assert.assertEquals(expResult.getParameter(), result.getParameter());
    }

    /**
     * Test of getLowerBoundPersonal method, of class ParameterInclusiveInitializationStrategy.
     */
    @Test
    public void testGetLowerBoundPersonal() {
        System.out.println("getLowerBoundPersonal");
        ParameterInclusiveInitializationStrategy instance = new ParameterInclusiveInitializationStrategy();
        ControlParameter expResult = new ConstantControlParameter(0.1); //default value
        ControlParameter result = instance.getLowerBoundCognitiveAcceleration();
        
        Assert.assertEquals(expResult.getParameter(), result.getParameter());
    }

    /**
     * Test of getUpperBoundPersonal method, of class ParameterInclusiveInitializationStrategy.
     */
    @Test
    public void testGetUpperBoundPersonal() {
        System.out.println("getUpperBoundPersonal");
        ParameterInclusiveInitializationStrategy instance = new ParameterInclusiveInitializationStrategy();
        ControlParameter expResult = new ConstantControlParameter(0.9); //default value
        ControlParameter result = instance.getUpperBoundCognitiveAcceleration();
        
        Assert.assertEquals(expResult.getParameter(), result.getParameter());
    }

    /**
     * Test of getLowerBoundVmax method, of class ParameterInclusiveInitializationStrategy.
     */
    @Test
    public void testGetLowerBoundVmax() {
        System.out.println("getLowerBoundVmax");
        ParameterInclusiveInitializationStrategy instance = new ParameterInclusiveInitializationStrategy();
        ControlParameter expResult = new ConstantControlParameter(0.1); //default value
        ControlParameter result = instance.getLowerBoundVmax();
        
        Assert.assertEquals(expResult.getParameter(), result.getParameter());
    }

    /**
     * Test of getUpperBoundVmax method, of class ParameterInclusiveInitializationStrategy.
     */
    @Test
    public void testGetUpperBoundVmax() {
        System.out.println("getUpperBoundVmax");
        ParameterInclusiveInitializationStrategy instance = new ParameterInclusiveInitializationStrategy();
        ControlParameter expResult = new ConstantControlParameter(0.9); //default value
        ControlParameter result = instance.getUpperBoundVmax();
        
        Assert.assertEquals(expResult.getParameter(), result.getParameter());
    }

    /**
     * Test of setLowerBoundInertia method, of class ParameterInclusiveInitializationStrategy.
     */
    @Test
    public void testSetLowerBoundInertia() {
        System.out.println("setLowerBoundInertia");
        ControlParameter bound = new ConstantControlParameter(0.3);
        ParameterInclusiveInitializationStrategy instance = new ParameterInclusiveInitializationStrategy();
        instance.setLowerBoundInertia(bound);
        
        Assert.assertEquals(instance.getLowerBoundInertia().getParameter(), bound.getParameter());
    }

    /**
     * Test of setUpperBoundInertia method, of class ParameterInclusiveInitializationStrategy.
     */
    @Test
    public void testSetUpperBoundInertia() {
        System.out.println("setUpperBoundInertia");
        ControlParameter bound = new ConstantControlParameter(0.3);
        ParameterInclusiveInitializationStrategy instance = new ParameterInclusiveInitializationStrategy();
        instance.setUpperBoundInertia(bound);
        
        Assert.assertEquals(instance.getUpperBoundInertia().getParameter(), bound.getParameter());
    }

    /**
     * Test of setLowerBoundSocial method, of class ParameterInclusiveInitializationStrategy.
     */
    @Test
    public void testSetLowerBoundSocial() {
        System.out.println("setLowerBoundSocial");
        ControlParameter bound = new ConstantControlParameter(0.3);
        ParameterInclusiveInitializationStrategy instance = new ParameterInclusiveInitializationStrategy();
        instance.setLowerBoundSocialAcceleration(bound);
        
        Assert.assertEquals(instance.getLowerBoundSocialAcceleration().getParameter(), bound.getParameter());
    }

    /**
     * Test of setUpperBoundSocial method, of class ParameterInclusiveInitializationStrategy.
     */
    @Test
    public void testSetUpperBoundSocial() {
        System.out.println("setUpperBoundSocial");
        ControlParameter bound = new ConstantControlParameter(0.3);
        ParameterInclusiveInitializationStrategy instance = new ParameterInclusiveInitializationStrategy();
        instance.setUpperBoundSocialAcceleration(bound);
        
        Assert.assertEquals(instance.getUpperBoundSocialAcceleration().getParameter(), bound.getParameter());
    }

    /**
     * Test of setLowerBoundPersonal method, of class ParameterInclusiveInitializationStrategy.
     */
    @Test
    public void testSetLowerBoundPersonal() {
        ControlParameter bound = new ConstantControlParameter(0.3);
        ParameterInclusiveInitializationStrategy instance = new ParameterInclusiveInitializationStrategy();
        instance.setLowerBoundCognitiveAcceleration(bound);
        
        Assert.assertEquals(instance.getLowerBoundCognitiveAcceleration().getParameter(), bound.getParameter());
    }

    /**
     * Test of setUpperBoundPersonal method, of class ParameterInclusiveInitializationStrategy.
     */
    @Test
    public void testSetUpperBoundPersonal() {
        System.out.println("setUpperBoundPersonal");
        ControlParameter bound = new ConstantControlParameter(0.3);
        ParameterInclusiveInitializationStrategy instance = new ParameterInclusiveInitializationStrategy();
        instance.setUpperBoundCognitiveAcceleration(bound);
        
        Assert.assertEquals(instance.getUpperBoundCognitiveAcceleration().getParameter(), bound.getParameter());
    }

    /**
     * Test of setLowerBoundVmax method, of class ParameterInclusiveInitializationStrategy.
     */
    @Test
    public void testSetLowerBoundVmax() {
        System.out.println("setLowerBoundVmax");
        ControlParameter bound = new ConstantControlParameter(0.3);
        ParameterInclusiveInitializationStrategy instance = new ParameterInclusiveInitializationStrategy();
        instance.setLowerBoundVmax(bound);
        
        Assert.assertEquals(instance.getLowerBoundVmax().getParameter(), bound.getParameter());
    }

    /**
     * Test of setUpperBoundVmax method, of class ParameterInclusiveInitializationStrategy.
     */
    @Test
    public void testSetUpperBoundVmax() {
        System.out.println("setUpperBoundVmax");
       ControlParameter bound = new ConstantControlParameter(0.3);
        ParameterInclusiveInitializationStrategy instance = new ParameterInclusiveInitializationStrategy();
        instance.setUpperBoundVmax(bound);
        
        Assert.assertEquals(instance.getUpperBoundVmax().getParameter(), bound.getParameter());
    }
}
