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

import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.initialisation.PopulationInitialisationStrategy;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.functions.continuous.unconstrained.Spherical;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.stoppingcondition.MaximumIterations;
import java.util.HashMap;
import junit.framework.Assert;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.EntityType;
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
public class FIPSVelocityProviderTest {
    
    public FIPSVelocityProviderTest() {
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
    
    /*
     * This method tests all the methiods in this class.
     * It calls get(), setControlParameters() and getControlParameterVelocity()
     */
    private void checkSettingReturnCorrectResult() {
        FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
        problem.setDomain("R(-5.12, 5.12)^5");
        problem.setFunction(new Spherical());
                
        PSO pso = new PSO();
        
        ParameterizedParticle particle = new ParameterizedParticle();
        particle.initialise(problem);
        
        FIPSVelocityProvider instance = new FIPSVelocityProvider();
        particle.setInertia(new ConstantControlParameter(2.0));
        particle.setSocialAcceleration(new ConstantControlParameter(0.0));
        particle.setCognitiveAcceleration(new ConstantControlParameter(0.0));
        particle.getProperties().put(EntityType.Particle.BEST_POSITION, Vector.of(1.0,1.0,1.0,1.0));
        
        particle.getProperties().put(EntityType.Particle.VELOCITY, Vector.of(2.0,2.0,2.0,2.0));
        particle.setCandidateSolution(Vector.of(1.0,1.0,1.0,1.0));
        
        Vector expectedResult = Vector.of(4.0,4.0,4.0,4.0);
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
        
        Assert.assertEquals(expectedResult, result);
    }

    /**
     * Test of get method, of class FIPSVelocityProvider.
     */
    @Test
    public void testGet() {
        System.out.println("get");
        
        checkSettingReturnCorrectResult();
    }
    
    

    /**
     * Test of setControlParameters method, of class FIPSVelocityProvider.
     */
    @Test
    public void testSetControlParameters() {
        System.out.println("setControlParameters");
        
        checkSettingReturnCorrectResult();
    }

    /**
     * Test of getControlParameterVelocity method, of class FIPSVelocityProvider.
     */
    @Test
    public void testGetControlParameterVelocity() {
        checkSettingReturnCorrectResult();
    }
}
