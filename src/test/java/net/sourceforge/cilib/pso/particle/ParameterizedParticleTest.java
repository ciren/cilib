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
package net.sourceforge.cilib.pso.particle;

import net.sourceforge.cilib.stoppingcondition.MaximumIterations;
import net.sourceforge.cilib.functions.continuous.unconstrained.Spherical;
import junit.framework.Assert;
import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.initialisation.PopulationInitialisationStrategy;
import net.sourceforge.cilib.controlparameter.BoundedModifiableControlParameter;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.ParameterAdaptingPSOControlParameter;
import net.sourceforge.cilib.entity.CandidateSolution;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.problem.MaximisationFitness;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.velocityprovider.ClampingVelocityProvider;
import net.sourceforge.cilib.pso.velocityprovider.StandardVelocityProvider;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;
import net.sourceforge.cilib.util.calculator.EntityBasedFitnessCalculator;
import net.sourceforge.cilib.util.calculator.FitnessCalculator;
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
public class ParameterizedParticleTest {
    
    public ParameterizedParticleTest() {
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
     * Test of getBestFitness method, of class ParametizedParticle.
     */
    @Test
    public void testGetBestFitness() {
        System.out.println("getBestFitness");
        ParameterizedParticle instance = new ParameterizedParticle();
        Fitness fitness = new MaximisationFitness(5.0);
        instance.getProperties().put(EntityType.Particle.BEST_FITNESS, fitness);
        Fitness result = instance.getBestFitness();
        assertEquals(fitness.getValue(), result.getValue());
    }

    /**
     * Test of getBestPosition method, of class ParametizedParticle.
     */
    @Test
    public void testGetBestPosition() {
        System.out.println("getBestPosition");
        ParameterizedParticle instance = new ParameterizedParticle();
        Vector vector  = Vector.of(1.0, 1.0, 1.0, 1.0);
        instance.getProperties().put(EntityType.Particle.BEST_POSITION, vector);
        Vector result = instance.getBestPosition();
        assertEquals(vector, result);
    }

    /**
     * Test of getDimension method, of class ParametizedParticle.
     */
    @Test
    public void testGetDimension() {
        System.out.println("getDimension");
        ParameterizedParticle instance = new ParameterizedParticle();
        Vector vector  = Vector.of(1.0, 1.0, 1.0, 1.0);
        instance.setCandidateSolution(vector);
        int result = instance.getDimension();
        assertEquals(vector.size(), result);
    }

    /**
     * Test of getNeighbourhoodBest method, of class ParametizedParticle.
     */
    @Test
    public void testGetNeighbourhoodBest() {
        System.out.println("getNeighbourhoodBest");
        ParameterizedParticle instance = new ParameterizedParticle();
        Vector vector = Vector.of(1,2,3,4,5,6);
        ParameterizedParticle neighbourhoodBest = new ParameterizedParticle();
        neighbourhoodBest.setCandidateSolution(vector);
        instance.setNeighbourhoodBest(neighbourhoodBest);
        
        ParameterizedParticle result = instance.getNeighbourhoodBest();
        assertEquals(neighbourhoodBest.getCandidateSolution(), result.getCandidateSolution());
    }

    /**
     * Test of getPosition method, of class ParametizedParticle.
     */
    @Test
    public void testGetPosition() {
        System.out.println("getPosition");
        ParameterizedParticle instance = new ParameterizedParticle();
        Vector position = Vector.of(1,2,3,4,5);
        instance.setCandidateSolution(position);
        Vector result = instance.getPosition();
        assertEquals(position, result);
    }

    /**
     * Test of getVelocity method, of class ParametizedParticle.
     */
    @Test
    public void testGetVelocity() {
        System.out.println("getVelocity");
        ParameterizedParticle instance = new ParameterizedParticle();
        Vector expResult = Vector.of(1,2,3,4,5);
        instance.getProperties().put(EntityType.Particle.VELOCITY, expResult);
        Vector result = instance.getVelocity();
        assertEquals(expResult, result);
    }

    /**
     * Test of initialise method, of class ParametizedParticle.
     */
    @Test
    public void testInitialise() {
        System.out.println("initialise");
        FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
        problem.setDomain("R(-5.12, 5.12)^30");
        problem.setFunction(new Spherical());
            
        ParameterizedParticle instance = new ParameterizedParticle();
        instance.initialise(problem);
        int dimension = instance.getDimension();
        
        Assert.assertNotSame(0, dimension);
        Assert.assertEquals(instance.getInertia().getParameter(), 0.1);
        
        problem = new FunctionMinimisationProblem();
        problem.setDomain("R(-5.12, 5.12)^30");
        problem.setFunction(new Spherical());
        
        instance = new ParameterizedParticle();
        BoundedModifiableControlParameter parameter = new BoundedModifiableControlParameter();
        instance.setInertia(parameter);
        instance.initialise(problem);
        
        Assert.assertNotSame(instance.getInertia().getParameter(), 0.1);
        
    }

    /**
     * Test of updatePosition method, of class ParametizedParticle.
     */
    @Test
    public void testUpdatePosition() {
        System.out.println("updatePosition");
        ParameterizedParticle instance = new ParameterizedParticle();
        
        BoundedModifiableControlParameter parameter = new BoundedModifiableControlParameter();
        parameter.setParameter(0.2);
        instance.setInertia(parameter);
        instance.setSocialAcceleration(parameter);
        instance.setCognitiveAcceleration(parameter);
        instance.setVmax(parameter);
        instance.getInertia().setVelocity(1);
        instance.getSocialAcceleration().setVelocity(1);
        instance.getCognitiveAcceleration().setVelocity(1);
        instance.getVmax().setVelocity(1);
        
        Vector position  = Vector.of(1,2,3,4,5);
        Vector velocity = Vector.of(1,1,1,1,1);
        instance.setCandidateSolution(position);
        instance.getProperties().put(EntityType.Particle.VELOCITY, velocity);
        Vector expectedResult = Vectors.sumOf(position, velocity);
        instance.updatePosition();
        
        Assert.assertEquals(expectedResult, instance.getPosition());
        Assert.assertEquals(1.2, instance.getInertia().getParameter());
        Assert.assertEquals(1.2, instance.getSocialAcceleration().getParameter());
        Assert.assertEquals(1.2, instance.getCognitiveAcceleration().getParameter());
        Assert.assertEquals(1.2, instance.getVmax().getParameter());
    }

    /**
     * Test of calculateFitness method, of class ParametizedParticle.
     */
    @Test
    public void testCalculateFitness() {
        System.out.println("calculateFitness");
        
        FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
        problem.setDomain("R(-5.12, 5.12)^5");
        problem.setFunction(new Spherical());
                
        PSO pso = new PSO();
        
        ParameterizedParticle instance = new ParameterizedParticle();
        ConstantControlParameter parameter = new ConstantControlParameter(0);
        instance.setInertia(parameter);
        instance.setSocialAcceleration(parameter);
        instance.setCognitiveAcceleration(parameter);
        instance.setVmax(parameter);
        instance.initialise(problem);
        
        Vector candidateSolution = Vector.of(1,2,3,4,5);
        Vector velocity = Vector.of(0,0,0,0,0);
        instance.setCandidateSolution(candidateSolution);
        instance.getProperties().put(EntityType.Particle.VELOCITY, velocity);
        //instance.setNeighbourhoodBest(instance);
        /*PopulationInitialisationStrategy newStrategy = new ClonedPopulationInitialisationStrategy();
        newStrategy.setEntityType(instance);
        newStrategy.setEntityNumber(1);*/
        
        pso.setOptimisationProblem(problem);
        pso.addStoppingCondition(new MaximumIterations(1));
        //pso.setInitialisationStrategy(newStrategy);
        pso.initialise();
        Topology<ParameterizedParticle> topology = new GBestTopology();
        topology.add(instance);
        pso.setTopology(topology);
        pso.run();
        
        double expectedValue = 55;
        
        Assert.assertEquals(expectedValue, pso.getBestSolution().getFitness().getValue());
    }

    /**
     * Test of setNeighbourhoodBest method, of class ParametizedParticle.
     */
    @Test
    public void testSetNeighbourhoodBest() {
        System.out.println("setNeighbourhoodBest");
        Vector neighbourhoodBest = Vector.of(1,2,3,4,5);
        Particle particle = new ParameterizedParticle();
        particle.setCandidateSolution(neighbourhoodBest);
        
        ParameterizedParticle instance = new ParameterizedParticle();
        instance.setNeighbourhoodBest(particle);
        
        Assert.assertEquals(neighbourhoodBest, instance.getNeighbourhoodBest().getCandidateSolution());
    }

    /**
     * Test of updateVelocity method, of class ParametizedParticle.
     */
    @Test
    public void testUpdateVelocity() {
        System.out.println("updateVelocity");
        ParameterizedParticle instance = new ParameterizedParticle();
        
        FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
        problem.setDomain("R(-5.12, 5.12)^5");
        problem.setFunction(new Spherical());
        
        instance.initialise(problem);
        Vector particleVelocity = Vector.of(1,1,1,1,1);
        Vector particleSolution = Vector.of(1,1,1,1,1);
        double velocityInertia = 0.1;
        double velocitySocial = 0.1;
        double velocityPersonal = 0.1;
        double velocityVmax = 0.1;
        
        instance.getProperties().put(EntityType.Particle.VELOCITY, particleVelocity);
        instance.setCandidateSolution(particleSolution);
        instance.getInertia().setVelocity(velocityInertia);
        instance.getSocialAcceleration().setVelocity(velocitySocial);
        instance.getCognitiveAcceleration().setVelocity(velocityPersonal);
        instance.getVmax().setVelocity(velocityVmax);
        
        instance.setParameterVelocityProvider(new ClampingVelocityProvider());
        
        instance.updateVelocity();
        
        Assert.assertNotSame(particleVelocity, instance.getVelocity());
        Assert.assertNotSame(velocityInertia, instance.getInertia().getVelocity());
        Assert.assertNotSame(velocitySocial, instance.getSocialAcceleration().getVelocity());
        Assert.assertNotSame(velocityPersonal, instance.getCognitiveAcceleration().getVelocity());
        Assert.assertNotSame(velocityVmax, instance.getVmax().getVelocity());
    }

    /**
     * Test of reinitialise method, of class ParametizedParticle.
     */
    @Test
    public void testReinitialise() {
        System.out.println("reinitialise");
        ParameterizedParticle instance = new ParameterizedParticle();
        
        FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
        problem.setDomain("R(-5.12, 5.12)^5");
        problem.setFunction(new Spherical());
        
        instance.initialise(problem);
        Vector candidateSolution = (Vector) instance.getCandidateSolution();
        double inertia = instance.getInertia().getParameter();
        double social = instance.getSocialAcceleration().getParameter();
        double personal = instance.getCognitiveAcceleration().getParameter();
        double vMax = instance.getVmax().getParameter();
        
        instance.reinitialise();
        
        Assert.assertNotSame(candidateSolution, instance.getCandidateSolution());
        Assert.assertNotSame(inertia, instance.getInertia().getParameter());
        Assert.assertNotSame(social, instance.getSocialAcceleration().getParameter());
        Assert.assertNotSame(personal, instance.getCognitiveAcceleration().getParameter());
        Assert.assertNotSame(vMax, instance.getVmax().getParameter());
    }

    /**
     * Test of setInertia method, of class ParametizedParticle.
     */
    @Test
    public void testSetInertia() {
        System.out.println("setInertia");
        ParameterAdaptingPSOControlParameter parameter = new ConstantControlParameter(0.55);
        ParameterizedParticle instance = new ParameterizedParticle();
        instance.setInertia(parameter);
        
        Assert.assertEquals(parameter.getParameter(), instance.getInertia().getParameter());
    }

    /**
     * Test of getInertia method, of class ParametizedParticle.
     */
    @Test
    public void testGetInertia() {
        System.out.println("getInertia");
        ParameterAdaptingPSOControlParameter parameter = new ConstantControlParameter(0.55);
        ParameterizedParticle instance = new ParameterizedParticle();
        instance.setInertia(parameter);
        
        Assert.assertEquals(parameter.getParameter(), instance.getInertia().getParameter());
    }

    /**
     * Test of setSocial method, of class ParametizedParticle.
     */
    @Test
    public void testSetSocial() {
        System.out.println("setSocial");
        ParameterAdaptingPSOControlParameter parameter = new ConstantControlParameter(0.55);
        ParameterizedParticle instance = new ParameterizedParticle();
        instance.setSocialAcceleration(parameter);
        
        Assert.assertEquals(parameter.getParameter(), instance.getSocialAcceleration().getParameter());
    }

    /**
     * Test of getSocial method, of class ParametizedParticle.
     */
    @Test
    public void testGetSocial() {
        System.out.println("getSocial");
        ParameterAdaptingPSOControlParameter parameter = new ConstantControlParameter(0.55);
        ParameterizedParticle instance = new ParameterizedParticle();
        instance.setSocialAcceleration(parameter);
        
        Assert.assertEquals(parameter.getParameter(), instance.getSocialAcceleration().getParameter());
    }

    /**
     * Test of setPersonal method, of class ParametizedParticle.
     */
    @Test
    public void testSetPersonal() {
        System.out.println("setPersonal");
        ParameterAdaptingPSOControlParameter parameter = new ConstantControlParameter(0.55);
        ParameterizedParticle instance = new ParameterizedParticle();
        instance.setCognitiveAcceleration(parameter);
        
        Assert.assertEquals(parameter.getParameter(), instance.getCognitiveAcceleration().getParameter());
    }

    /**
     * Test of getPersonal method, of class ParametizedParticle.
     */
    @Test
    public void testGetPersonal() {
        System.out.println("getPersonal");
        ParameterAdaptingPSOControlParameter parameter = new ConstantControlParameter(0.55);
        ParameterizedParticle instance = new ParameterizedParticle();
        instance.setSocialAcceleration(parameter);
        
        Assert.assertEquals(parameter.getParameter(), instance.getSocialAcceleration().getParameter());
    }

    /**
     * Test of setVmax method, of class ParametizedParticle.
     */
    @Test
    public void testSetVmax() {
        System.out.println("setVmax");
        ParameterAdaptingPSOControlParameter parameter = new ConstantControlParameter(0.55);
        ParameterizedParticle instance = new ParameterizedParticle();
        instance.setVmax(parameter);
        
        Assert.assertEquals(parameter.getParameter(), instance.getVmax().getParameter());
    }

    /**
     * Test of getVmax method, of class ParametizedParticle.
     */
    @Test
    public void testGetVmax() {
        System.out.println("getVmax");
        ParameterAdaptingPSOControlParameter parameter = new ConstantControlParameter(0.55);
        ParameterizedParticle instance = new ParameterizedParticle();
        instance.setVmax(parameter);
        
        Assert.assertEquals(parameter.getParameter(), instance.getVmax().getParameter());
    }
    
    /**
     * Test of setEntityLowerBound method, of class ParametizedParticle.
     */
    @Test
    public void testSetEntityLowerBound() {
        System.out.println("setEntityLowerBound");
        ParameterAdaptingPSOControlParameter parameter = new ConstantControlParameter(0.55);
        ParameterizedParticle instance = new ParameterizedParticle();
        instance.setEntityLowerBound(parameter.getParameter());
        
        Assert.assertEquals(parameter.getParameter(), instance.getEntityLowerBound().getParameter());
    }

    /**
     * Test of getEntityLowerBound method, of class ParametizedParticle.
     */
    @Test
    public void testGetEntityLowerBound() {
        System.out.println("getVmax");
        ControlParameter parameter = new ConstantControlParameter(0.55);
        ParameterizedParticle instance = new ParameterizedParticle();
        instance.setEntityLowerBound(parameter.getParameter());
        
        Assert.assertEquals(parameter.getParameter(), instance.getEntityLowerBound().getParameter());
    }
    
    /**
     * Test of setEntityUpperBound method, of class ParametizedParticle.
     */
    @Test
    public void testSetUpperLowerBound() {
        System.out.println("setEntityLowerBound");
        ControlParameter parameter = new ConstantControlParameter(0.55);
        ParameterizedParticle instance = new ParameterizedParticle();
        instance.setEntityUpperBound(parameter.getParameter());
        
        Assert.assertEquals(parameter.getParameter(), instance.getEntityUpperBound().getParameter());
    }

    /**
     * Test of getEntityUpperBound method, of class ParametizedParticle.
     */
    @Test
    public void testGetEntityUpperBound() {
        System.out.println("getVmax");
        ControlParameter parameter = new ConstantControlParameter(0.55);
        ParameterizedParticle instance = new ParameterizedParticle();
        instance.setEntityUpperBound(parameter.getParameter());
        
        Assert.assertEquals(parameter.getParameter(), instance.getEntityUpperBound().getParameter());
    }
    
}
