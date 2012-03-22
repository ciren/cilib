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

import junit.framework.Assert;
import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.initialisation.PopulationInitialisationStrategy;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.functions.continuous.unconstrained.Spherical;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.stoppingcondition.MaximumIterations;
import java.util.HashMap;
import net.sourceforge.cilib.controlparameter.ControlParameter;
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
 * Unfortunately all that can be tested is whether the velocity values actually change.
 * This is due to the random component involved in changing the velocity. Seeding is 
 * currently a problem in CILib, once that is fixed, this class can be tested properly.
 * 
 * @author Kristina
 */
public class FDRVelocityProviderTest {
    
    public FDRVelocityProviderTest() {
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
     * Test of get method, of class FDRVelocityProvider.
     */
    @Test
    public void testGet() {
        System.out.println("get");
         FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
        problem.setDomain("R(-5.12, 5.12)^5");
        problem.setFunction(new Spherical());
                
        PSO pso = new PSO();
        
        ParameterizedParticle particle = new ParameterizedParticle();
        particle.initialise(problem);
        
        FDRVelocityProvider instance = new FDRVelocityProvider();
        particle.setInertia(ConstantControlParameter.of(2.0));
        particle.setSocialAcceleration(ConstantControlParameter.of(0.0));
        particle.setCognitiveAcceleration(ConstantControlParameter.of(0.0));
        particle.getProperties().put(EntityType.Particle.BEST_POSITION, Vector.of(1.0,1.0,1.0,1.0));
        
        particle.getProperties().put(EntityType.Particle.VELOCITY, Vector.of(2.0,2.0,2.0,2.0));
        particle.setCandidateSolution(Vector.of(1.0,1.0,1.0,1.0));
        
        instance.setControlParameters(particle);
        
        pso.setOptimisationProblem(problem);
        PopulationInitialisationStrategy newStrategy = new ClonedPopulationInitialisationStrategy();
        newStrategy.setEntityNumber(1);
        
        pso.addStoppingCondition(new MaximumIterations(1));
        pso.initialise();
        particle.setVelocityProvider(instance);
        Topology<ParameterizedParticle> topology = new GBestTopology();
        topology.add(particle);
        pso.setTopology(topology);
        pso.run();
        
        Vector result = particle.getVelocity();
        
        Assert.assertNotSame(result, Vector.of(2.0,2.0,2.0,2.0));
    }

 
    /**
     * Test of getFdrMaximizerAcceleration method, of class FDRVelocityProvider.
     */
    @Test
    public void testGetFdrMaximizerAcceleration() {
        System.out.println("getFdrMaximizerAcceleration");
        FDRVelocityProvider instance = new FDRVelocityProvider();
        instance.setFdrMaximizerAcceleration(ConstantControlParameter.of(2.0));
        
        Assert.assertEquals(instance.getFdrMaximizerAcceleration().getParameter(), 2.0);
    }

    /**
     * Test of setFdrMaximizerAcceleration method, of class FDRVelocityProvider.
     */
    @Test
    public void testSetFdrMaximizerAcceleration() {
        System.out.println("setFdrMaximizerAcceleration");
        FDRVelocityProvider instance = new FDRVelocityProvider();
        instance.setFdrMaximizerAcceleration(ConstantControlParameter.of(2.0));
        
        Assert.assertEquals(instance.getFdrMaximizerAcceleration().getParameter(), 2.0);
    }

    
    /**
     * Test of getControlParameterVelocity method, of class FDRVelocityProvider.
     */
    @Test
    public void testGetControlParameterVelocity() {
        System.out.println("getControlParameterVelocity");
        
        FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
        problem.setDomain("R(-5.12, 5.12)^5");
        problem.setFunction(new Spherical());
                
        PSO pso = new PSO();
        
        ParameterizedParticle particle = new ParameterizedParticle();
        particle.initialise(problem);
        
        FDRVelocityProvider instance = new FDRVelocityProvider();
        particle.setInertia(ConstantControlParameter.of(2.0));
        particle.setSocialAcceleration(ConstantControlParameter.of(2.0));
        particle.setCognitiveAcceleration(ConstantControlParameter.of(2.0));
        particle.setVmax(ConstantControlParameter.of(2.0));
        
        particle.getInertia().setVelocity(1.0);
        particle.getSocialAcceleration().setVelocity(1.0);
        particle.getCognitiveAcceleration().setVelocity(1.0);
        particle.getVmax().setVelocity(1.0);
        
        particle.getProperties().put(EntityType.Particle.BEST_POSITION, Vector.of(1.0,1.0,1.0,1.0));
        
        particle.getProperties().put(EntityType.Particle.VELOCITY, Vector.of(2.0,2.0,2.0,2.0));
        particle.setCandidateSolution(Vector.of(1.0,1.0,1.0,1.0));
        
        instance.setControlParameters(particle);
        
        pso.setOptimisationProblem(problem);
        PopulationInitialisationStrategy newStrategy = new ClonedPopulationInitialisationStrategy();
        newStrategy.setEntityNumber(1);
        
        pso.addStoppingCondition(new MaximumIterations(1));
        pso.initialise();
        particle.setVelocityProvider(instance);
        Topology<ParameterizedParticle> topology = new GBestTopology();
        topology.add(particle);
        pso.setTopology(topology);
        pso.run();
       
        Assert.assertNotSame(particle.getInertia().getVelocity(), 1.0);
        Assert.assertNotSame(particle.getSocialAcceleration().getVelocity(), 1.0);
        Assert.assertNotSame(particle.getCognitiveAcceleration().getVelocity(), 1.0);
        Assert.assertNotSame(particle.getVmax().getVelocity(), 1.0);
    }
}
