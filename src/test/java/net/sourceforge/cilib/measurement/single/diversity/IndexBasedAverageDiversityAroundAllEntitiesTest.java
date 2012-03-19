/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.measurement.single.diversity;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.pso.particle.ParameterizedParticle;
import junit.framework.Assert;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.functions.continuous.unconstrained.Spherical;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.stoppingcondition.MaximumIterations;
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
public class IndexBasedAverageDiversityAroundAllEntitiesTest {
    
    PSO pso;
    FunctionMinimisationProblem problem;
    ConstantControlParameter parameter;
    Topology<ParameterizedParticle> topology;
    IndexBasedAverageDiversityAroundAllEntities diversityMeasure;
    
    public IndexBasedAverageDiversityAroundAllEntitiesTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void initialise() {
        
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getValue method, of class IndexBasedAverageDiversityAroundAllEntities.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
         
        problem = new FunctionMinimisationProblem();
        problem.setDomain("R(-5.12, 5.12)^5");
        problem.setFunction(new Spherical());
                
        pso = new PSO();
        
        pso.setOptimisationProblem(problem);
        pso.addStoppingCondition(new MaximumIterations(1));
        //pso.setInitialisationStrategy(newStrategy);
        pso.initialise();
        
        parameter = new ConstantControlParameter(0);
        diversityMeasure = new IndexBasedAverageDiversityAroundAllEntities();
        
        testConvergedDiversity();
        
        testDiversityValues();
        
        
    }
   
    /*
     * This method tests two things:
     * 1- Does the class handle parameter indexes correctly?
     * 2- Is the diversity value 0 if all the particles are the same?
     */
    private void testConvergedDiversity() {
        ParameterizedParticle instance = new ParameterizedParticle();
        instance.setInertia(parameter);
        instance.setSocialAcceleration(parameter);
        instance.setCognitiveAcceleration(parameter);
        instance.setVmax(parameter);
        instance.initialise(problem);
        
        Vector candidateSolution = Vector.of(1,2,3,4,5);
        Vector velocity = Vector.of(0,0,0,0,0);
        instance.setCandidateSolution(candidateSolution);
        instance.getProperties().put(EntityType.Particle.VELOCITY, velocity);
        instance.getProperties().put(EntityType.Particle.BEST_POSITION, candidateSolution);
        
        ParameterizedParticle nBest = new ParameterizedParticle();
        nBest.setCandidateSolution(candidateSolution);
        nBest.getProperties().put(EntityType.Particle.BEST_POSITION, candidateSolution);
        nBest.getProperties().put(EntityType.Particle.BEST_FITNESS, instance.getBestFitness());
        nBest.getProperties().put(EntityType.FITNESS, instance.getFitness());
        instance.setNeighbourhoodBest(nBest);
        
        topology = new GBestTopology();
        topology.add(instance);
        topology.add(instance.getClone());
        pso.setTopology(topology);
        pso.run();
        
        diversityMeasure.setIndex(5); //inertia
        double value = diversityMeasure.getValue(pso).doubleValue();
        
        Assert.assertEquals(value, 0.0);
    }
    
    /*
     * This method tests whether the calculated value for the diversity of index 1 is correct
     */
    private void testDiversityValues() {
        ParameterizedParticle instance = new ParameterizedParticle();
        instance.setInertia(parameter);
        instance.setSocialAcceleration(parameter);
        instance.setCognitiveAcceleration(parameter);
        instance.setVmax(parameter);
        instance.initialise(problem);
        
        Vector candidateSolution = Vector.of(1,2,3,4,5);
        Vector velocity = Vector.of(0,0,0,0,0);
        instance.setCandidateSolution(candidateSolution);
        instance.getProperties().put(EntityType.Particle.VELOCITY, velocity);
        instance.getProperties().put(EntityType.Particle.BEST_POSITION, candidateSolution);
        
        ParameterizedParticle nBest = new ParameterizedParticle();
        nBest.setCandidateSolution(candidateSolution);
        nBest.getProperties().put(EntityType.Particle.BEST_POSITION, candidateSolution);
        nBest.getProperties().put(EntityType.Particle.BEST_FITNESS, instance.getBestFitness());
        nBest.getProperties().put(EntityType.FITNESS, instance.getFitness());
        instance.setNeighbourhoodBest(nBest);
        
        ParameterizedParticle instance2 = instance.getClone();
        
        Vector candidateSolution2 = Vector.of(5,7,3,2,1);
        instance2.setCandidateSolution(candidateSolution2);
        instance2.getProperties().put(EntityType.Particle.BEST_POSITION, candidateSolution2);
        instance2.setNeighbourhoodBest(nBest);
        
        ParameterizedParticle instance3 = instance.getClone();
        
        Vector candidateSolution3 = Vector.of(5,4,3,2,1);
        instance3.setCandidateSolution(candidateSolution3);
        instance3.getProperties().put(EntityType.Particle.BEST_POSITION, candidateSolution3);
        
        instance3.setNeighbourhoodBest(nBest);
        
        topology = new GBestTopology();
        topology.add(instance);
        topology.add(instance2);
        topology.add(instance3);
        pso.setTopology(topology);
        pso.run();
        
        diversityMeasure = new IndexBasedAverageDiversityAroundAllEntities();
        
        diversityMeasure.setIndex(1); //index 1 of particle
        double value = diversityMeasure.getValue(pso).doubleValue();
        Assert.assertEquals(value, -9.251858538542972E-17);
    }

    /**
     * Test of setIndex method, of class IndexBasedAverageDiversityAroundAllEntities.
     */
    @Test
    public void testSetIndex() {
        System.out.println("setIndex");
        IndexBasedAverageDiversityAroundAllEntities instance = new IndexBasedAverageDiversityAroundAllEntities();
        instance.setIndex(10);
        Assert.assertEquals(instance.getIndex(), 10);
    }
    
    /**
     * Test of getIndex method, of class IndexBasedAverageDiversityAroundAllEntities.
     */
    @Test
    public void testGetIndex() {
        System.out.println("getIndex");
        IndexBasedAverageDiversityAroundAllEntities instance = new IndexBasedAverageDiversityAroundAllEntities();
        instance.setIndex(10);
        Assert.assertEquals(instance.getIndex(), 10);
    }
}
