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
package net.sourceforge.cilib.ec;

import net.sourceforge.cilib.algorithm.Algorithm;
import junit.framework.Assert;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.functions.continuous.unconstrained.Spherical;
import net.sourceforge.cilib.measurement.generic.Iterations;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.pso.particle.ParameterizedParticle;
import net.sourceforge.cilib.pso.velocityprovider.StandardVelocityProvider;
import net.sourceforge.cilib.stoppingcondition.Maximum;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.controlparameter.ParameterAdaptingControlParameter;
import net.sourceforge.cilib.ec.iterationstrategies.DifferentialEvolutionParameterizedIterationStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.operators.crossover.DifferentialEvolutionExponentialCrossover;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.types.container.StructuredType;
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
public class ParameterizedDEIndividualTest {
    
    public ParameterizedDEIndividualTest() {
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
     * Test of calculateFitness method, of class ParameterizedDEIndividual.
     */
    @Test
    public void testCalculateFitness() {
        System.out.println("calculateFitness");
        FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
        problem.setDomain("R(-5.12, 5.12)^5");
        problem.setFunction(new Spherical());

        ParameterizedDEIndividual instance = new ParameterizedDEIndividual();
        ParameterAdaptingControlParameter parameter = ConstantControlParameter.of(0);
        instance.setScalingFactor(parameter);
        instance.setRecombinationProbability(parameter);
        instance.initialise(problem);
        
        ParameterizedDEIndividual instance2 = new ParameterizedDEIndividual();
        ParameterAdaptingControlParameter parameter2 = ConstantControlParameter.of(0);
        instance2.setScalingFactor(parameter2);
        instance2.setRecombinationProbability(parameter2);
        instance2.initialise(problem);
        
        ParameterizedDEIndividual instance3 = new ParameterizedDEIndividual();
        ParameterAdaptingControlParameter parameter3 = ConstantControlParameter.of(0);
        instance3.setScalingFactor(parameter3);
        instance3.setRecombinationProbability(parameter3);
        instance3.initialise(problem);
        
        ParameterizedDEIndividual instance4 = new ParameterizedDEIndividual();
        ParameterAdaptingControlParameter parameter4 = ConstantControlParameter.of(0);
        instance4.setScalingFactor(parameter4);
        instance4.setRecombinationProbability(parameter4);
        instance4.initialise(problem);

        EC de = new EC();
        DifferentialEvolutionParameterizedIterationStrategy strategy = new DifferentialEvolutionParameterizedIterationStrategy();
        de.setIterationStrategy(strategy);
        
        Vector candidateSolution = Vector.of(1,2,3,4,5);
        instance.setCandidateSolution(candidateSolution);
        
        Vector candidateSolution2 = Vector.of(3,4,5,6,7);
        instance2.setCandidateSolution(candidateSolution2);
        
        Vector candidateSolution3 = Vector.of(5,6,7,8,9);
        instance3.setCandidateSolution(candidateSolution3);
        
        Vector candidateSolution4 = Vector.of(4,5,6,7,8);
        instance4.setCandidateSolution(candidateSolution4);
        
        de.setOptimisationProblem(problem);
        de.addStoppingCondition(new MeasuredStoppingCondition(new Iterations(), new Maximum(), 1));
        de.initialise();
        Topology<ParameterizedDEIndividual> topology = new GBestTopology();
        topology.add(instance);
        topology.add(instance2);
        topology.add(instance3);
        topology.add(instance4);
        de.setTopology(topology);
        de.run();

        double expectedValue = 55;

        Assert.assertEquals(expectedValue, de.getBestSolution().getFitness().getValue());
    }

    /**
     * Test of resetFitness method, of class ParameterizedDEIndividual.
     */
    @Test
    public void testResetFitness() {
        System.out.println("resetFitness");
        ParameterizedDEIndividual individual = new ParameterizedDEIndividual();
        individual.getProperties().put(EntityType.FITNESS, new MinimisationFitness(12.3));
        Assert.assertEquals(12.3, individual.getFitness().getValue());
        
        individual.resetFitness();
        Assert.assertTrue(individual.getFitness() instanceof InferiorFitness);
    }

    /**
     * Test of initialise method, of class ParameterizedDEIndividual.
     */
    @Test
    public void testInitialise() {
        System.out.println("initialise");
        FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
        problem.setDomain("R(-5.12, 5.12)^5");
        problem.setFunction(new Spherical());
        
        ParameterizedDEIndividual individual =  new ParameterizedDEIndividual();
        individual.initialise(problem);
        
        Assert.assertEquals(5, individual.getCandidateSolution().size());
        Assert.assertNotSame(0, individual.getScalingFactor());
        Assert.assertNotSame(0, individual.getRecombinationProbability());
    }

    /**
     * Test of getDimension method, of class ParameterizedDEIndividual.
     */
    @Test
    public void testGetDimension() {
        System.out.println("getDimension");
        ParameterizedDEIndividual individual = new ParameterizedDEIndividual();
        FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
        problem.setDomain("R(-5.12, 5.12)^5");
        problem.setFunction(new Spherical());
        individual.initialise(problem);
        
        Assert.assertEquals(5, individual.getDimension());
        
    }

    /**
     * Test of setCandidateSolution method, of class ParameterizedDEIndividual.
     */
    @Test
    public void testSetCandidateSolution() {
        System.out.println("setCandidateSolution");
        ParameterizedDEIndividual individual = new ParameterizedDEIndividual();
        Vector candidateSolution = Vector.of(1,2,3,4,5);
        individual.setCandidateSolution(candidateSolution);
        
        Assert.assertEquals(candidateSolution, individual.getCandidateSolution());
    }

    /**
     * Test of setScalingFactor method, of class ParameterizedDEIndividual.
     */
    @Test
    public void testSetScalingFactor() {
        System.out.println("setScalingFactor");
        ParameterizedDEIndividual individual = new ParameterizedDEIndividual();
        individual.setScalingFactor(ConstantControlParameter.of(0.65));
        
        Assert.assertEquals(0.65, individual.getScalingFactor().getParameter());
    }

    /**
     * Test of setRecombinationProbability method, of class ParameterizedDEIndividual.
     */
    @Test
    public void testSetRecombinationProbability() {
        System.out.println("setRecombinationProbability");
        ParameterizedDEIndividual individual = new ParameterizedDEIndividual();
        individual.setRecombinationProbability(ConstantControlParameter.of(0.65));
        
        Assert.assertEquals(0.65, individual.getRecombinationProbability().getParameter());
    }

    /**
     * Test of getScalingFactor method, of class ParameterizedDEIndividual.
     */
    @Test
    public void testGetScalingFactor() {
        System.out.println("getScalingFactor");
        ParameterizedDEIndividual individual = new ParameterizedDEIndividual();
        individual.setScalingFactor(ConstantControlParameter.of(0.65));
        
        Assert.assertEquals(0.65, individual.getScalingFactor().getParameter());
    }

    /**
     * Test of getRecombinationProbability method, of class ParameterizedDEIndividual.
     */
    @Test
    public void testGetRecombinationProbability() {
        System.out.println("getRecombinationProbability");
        ParameterizedDEIndividual individual = new ParameterizedDEIndividual();
        individual.setRecombinationProbability(ConstantControlParameter.of(0.65));
        
        Assert.assertEquals(0.65, individual.getRecombinationProbability().getParameter());
    }
}
