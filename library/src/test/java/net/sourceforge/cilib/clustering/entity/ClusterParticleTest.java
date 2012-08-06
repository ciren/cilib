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
package net.sourceforge.cilib.clustering.entity;

import java.util.ArrayList;
import junit.framework.Assert;
import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.initialisation.DataDependantPopulationInitializationStrategy;
import net.sourceforge.cilib.clustering.DataClusteringPSO;
import net.sourceforge.cilib.clustering.SlidingWindow;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.initialization.StandardCentroidInitializationStrategy;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.measurement.generic.Iterations;
import net.sourceforge.cilib.problem.ClusteringProblem;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.problem.QuantizationErrorMinimizationProblem;
import net.sourceforge.cilib.stoppingcondition.Maximum;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Kristina
 */
public class ClusterParticleTest {
    
    public ClusterParticleTest() {
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
     * Test of getCandidateSolution method, of class ClusterParticle.
     */
    @Test
    public void testGetCandidateSolution() {
        System.out.println("getCandidateSolution");
        ClusterParticle instance = new ClusterParticle();
        CentroidHolder holder = new CentroidHolder();
        ClusterCentroid centroid = ClusterCentroid.of(1,2,3,4,5,6);
        holder.add(centroid);
        holder.add(centroid);
        holder.add(centroid);
        instance.setCandidateSolution(holder);
        
        Assert.assertEquals(instance.getCandidateSolution(), holder);
    }

    /**
     * Test of setCandidateSolution method, of class ClusterParticle.
     */
    @Test
    public void testSetCandidateSolution_CentroidHolder() {
        System.out.println("setCandidateSolution");
        ClusterParticle instance = new ClusterParticle();
        CentroidHolder holder = new CentroidHolder();
        ClusterCentroid centroid = ClusterCentroid.of(1,2,3,4,5,6);
        holder.add(centroid);
        holder.add(centroid);
        holder.add(centroid);
        instance.setCandidateSolution(holder);
        
        Assert.assertEquals(instance.getCandidateSolution(), holder);
    }

    /**
     * Test of calculateFitness method, of class ClusterParticle.
     */
    @Test
    public void testCalculateFitness() {
        System.out.println("calculateFitness");
        ClusterParticle instance = new ClusterParticle();
        CentroidHolder holder = new CentroidHolder();
        instance.getProperties().put(EntityType.FITNESS, new MinimisationFitness(12.0));
        instance.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(12.0));
        ClusterCentroid centroid = ClusterCentroid.of(1,2,3,4);
        centroid.setDataItemDistances(new double[]{2,2,4});
        holder.add(centroid);
        holder.add(centroid);
        holder.add(centroid);
        instance.setCandidateSolution(holder);
        CentroidHolder clearHolder =  new CentroidHolder();
        ClusterCentroid clearCentroid = ClusterCentroid.of(0,0,0,0);
        clearHolder.add(clearCentroid);
        clearHolder.add(clearCentroid);
        clearHolder.add(clearCentroid);
        instance.getProperties().put(EntityType.Particle.VELOCITY, clearHolder);
        
        instance.setNeighbourhoodBest(instance);
        instance.getProperties().put(EntityType.Particle.BEST_POSITION, instance.getCandidateSolution());
        
        DataClusteringPSO pso = new DataClusteringPSO();
        QuantizationErrorMinimizationProblem problem = new QuantizationErrorMinimizationProblem();
        SlidingWindow window = new SlidingWindow();
        pso.setSourceURL("src\\test\\resources\\datasets\\iris2.arff");
        window.setWindowSize(3);
        pso.setWindow(window);
        problem.setDomain("R(-5.12:5.12)");
        
        pso.setOptimisationProblem(problem);
        pso.addStoppingCondition(new MeasuredStoppingCondition(new Iterations(), new Maximum(), 1));
        
        DataDependantPopulationInitializationStrategy init = new DataDependantPopulationInitializationStrategy();
        init.setDelegate(new ClonedPopulationInitialisationStrategy());
        init.setEntityNumber(1);
        init.setEntityType(new ClusterParticle());
        pso.setInitialisationStrategy(init);
        
        pso.performInitialisation();
        Topology topology = new GBestTopology<ClusterParticle>();
        topology.add(instance);
        pso.setTopology(topology);
        pso.run();
        
        Assert.assertEquals(Math.round(instance.getFitness().getValue() * 1e10) / 1e10,  Math.round(3.1291362326128439920284548286519 * 1e10) / 1e10);
    }

    /**
     * Test of getBestFitness method, of class ClusterParticle.
     */
    @Test
    public void testGetBestFitness() {
        System.out.println("getBestFitness");
        ClusterParticle instance = new ClusterParticle();
        instance.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(6.0));
        
        Assert.assertEquals(instance.getBestFitness().getValue(), 6.0);
    }

    /**
     * Test of getDimension method, of class ClusterParticle.
     */
    @Test
    public void testGetDimension() {
        System.out.println("getDimension");
        ClusterParticle instance = new ClusterParticle();
        CentroidHolder holder = new CentroidHolder();
        ClusterCentroid centroid = ClusterCentroid.of(1,2,3,4,5,6);
        centroid.setDataItemDistances(new double[]{1,2,3,4});
        holder.add(centroid);
        holder.add(centroid);
        holder.add(centroid);
        instance.setCandidateSolution(holder);
        
        Assert.assertEquals(instance.getDimension(), 3);
    }

    /**
     * Test of getPosition method, of class ClusterParticle.
     */
    @Test
    public void testGetPosition() {
        System.out.println("getPosition");
        ClusterParticle instance = new ClusterParticle();
        CentroidHolder holder = new CentroidHolder();
        ClusterCentroid centroid = ClusterCentroid.of(1,2,3,4,5,6);
        centroid.setDataItemDistances(new double[]{1,2,3,4});
        holder.add(centroid);
        holder.add(centroid);
        holder.add(centroid);
        instance.setCandidateSolution(holder);
        
        Assert.assertEquals(instance.getPosition(), holder);
    }

    /**
     * Test of getBestPosition method, of class ClusterParticle.
     */
    @Test
    public void testGetBestPosition() {
        System.out.println("getBestPosition");
        ClusterParticle instance = new ClusterParticle();
        CentroidHolder holder = new CentroidHolder();
        ClusterCentroid centroid = ClusterCentroid.of(1,2,3,4,5,6);
        centroid.setDataItemDistances(new double[]{1,2,3,4});
        holder.add(centroid);
        holder.add(centroid);
        holder.add(centroid);
        instance.getProperties().put(EntityType.Particle.BEST_POSITION, holder);
        
        Assert.assertEquals(instance.getBestPosition(), holder);
    }

    /**
     * Test of getVelocity method, of class ClusterParticle.
     */
    @Test
    public void testGetVelocity() {
        System.out.println("getVelocity");
        ClusterParticle instance = new ClusterParticle();
        CentroidHolder holder = new CentroidHolder();
        ClusterCentroid centroid = ClusterCentroid.of(1,2,3,4,5,6);
        centroid.setDataItemDistances(new double[]{1,2,3,4});
        holder.add(centroid);
        holder.add(centroid);
        holder.add(centroid);
        instance.getProperties().put(EntityType.Particle.VELOCITY, holder);
        
        Assert.assertEquals(instance.getVelocity(), holder);
    }

    /**
     * Test of getNeighbourhoodBest method, of class ClusterParticle.
     */
    @Test
    public void testGetNeighbourhoodBest() {
        System.out.println("getNeighbourhoodBest");
        ClusterParticle instance = new ClusterParticle();
        ClusterParticle neighbour = new ClusterParticle();
        CentroidHolder holder = new CentroidHolder();
        ClusterCentroid centroid = ClusterCentroid.of(1,2,3,4,5,6);
        centroid.setDataItemDistances(new double[]{1,2,3,4});
        holder.add(centroid);
        holder.add(centroid);
        holder.add(centroid);
        neighbour.setCandidateSolution(holder);
        instance.setNeighbourhoodBest(neighbour);
        
        Assert.assertEquals(instance.getNeighbourhoodBest().getCandidateSolution(), holder);
    }

    /**
     * Test of updatePosition method, of class ClusterParticle.
     */
    @Test
    public void testUpdatePosition() {
        System.out.println("updatePosition");
        ClusterParticle instance = new ClusterParticle();
        CentroidHolder holder = new CentroidHolder();
        ClusterCentroid centroid = ClusterCentroid.of(1,2,3,4,5,6);
        centroid.setDataItemDistances(new double[]{1,2,3,4});
        holder.add(centroid);
        holder.add(centroid);
        holder.add(centroid);
        instance.setCandidateSolution(holder.getClone());
        instance.getProperties().put(EntityType.Particle.VELOCITY, holder);
        instance.getProperties().put(EntityType.Particle.BEST_POSITION, holder);
        instance.getProperties().put(EntityType.FITNESS, new MinimisationFitness(6.0));
        instance.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(6.0));
        instance.setNeighbourhoodBest(instance);
        instance.updatePosition();
        
        Assert.assertNotSame(instance.getCandidateSolution(), holder);
    }

    /**
     * Test of updateVelocity method, of class ClusterParticle.
     */
    @Test
    public void testUpdateVelocity() {
        System.out.println("updateVelocity");
        ClusterParticle instance = new ClusterParticle();
        CentroidHolder holder = new CentroidHolder();
        ClusterCentroid centroid = ClusterCentroid.of(1,2,3,4,5,6);
        centroid.setDataItemDistances(new double[]{1,2,3,4});
        holder.add(centroid);
        holder.add(centroid);
        holder.add(centroid);
        instance.setCandidateSolution(holder.getClone());
        instance.getProperties().put(EntityType.Particle.VELOCITY, holder);
        instance.getProperties().put(EntityType.Particle.BEST_POSITION, holder);
        instance.getProperties().put(EntityType.FITNESS, new MinimisationFitness(6.0));
        instance.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(6.0));
        instance.setNeighbourhoodBest(instance);
        instance.updateVelocity();
        
        Assert.assertNotSame(instance.getVelocity(), holder);
    }

    /**
     * Test of initialise method, of class ClusterParticle.
     */
    @Test
    public void testInitialise() {
        System.out.println("initialise");
        ClusteringProblem problem = new QuantizationErrorMinimizationProblem();
        problem.setDomain("R(-5.12:5.12)");
        problem.setNumberOfClusters(2);
        problem.setDimension(5);
        
        ClusterParticle instance = new ClusterParticle();
        StandardCentroidInitializationStrategy strategy = new StandardCentroidInitializationStrategy();
        ArrayList bounds = new ArrayList();
        ControlParameter[] params = new ControlParameter[2];
        params[0] = ConstantControlParameter.of(1.0);
        params[1] = ConstantControlParameter.of(3.0);
        bounds.add(params);
        params = new ControlParameter[2];
        params[0] = ConstantControlParameter.of(0.5);
        params[1] = ConstantControlParameter.of(2.1);
        bounds.add(params);       
        params = new ControlParameter[2];
        params[0] = ConstantControlParameter.of(1.0);
        params[1] = ConstantControlParameter.of(3.0);
        bounds.add(params);
        params = new ControlParameter[2];
        params[0] = ConstantControlParameter.of(0.5);
        params[1] = ConstantControlParameter.of(2.1);
        bounds.add(params);
        params = new ControlParameter[2];
        params[0] = ConstantControlParameter.of(0.5);
        params[1] = ConstantControlParameter.of(2.1);
        bounds.add(params);
        strategy.setBounds(bounds);
        
        instance.setCentroidInitialisationStrategy(strategy);
        instance.initialise(problem);
        
        Assert.assertEquals(instance.getDimension(), 2);
        Assert.assertTrue(((CentroidHolder) instance.getCandidateSolution()).get(0).size() == 5);
        Assert.assertEquals(instance.getBestPosition().size(), 2);
        Assert.assertTrue(((CentroidHolder) instance.getBestPosition()).get(0).size() == 5);
    }

    /**
     * Test of reinitialise method, of class ClusterParticle.
     */
    @Test
    public void testReinitialise() {
        System.out.println("reinitialise");
        ClusterParticle instance = new ClusterParticle();
        
        ClusteringProblem problem = new QuantizationErrorMinimizationProblem();
        problem.setDomain("R(-5.12:5.12)");
        problem.setNumberOfClusters(2);
        problem.setDimension(5);
        
        StandardCentroidInitializationStrategy strategy = new StandardCentroidInitializationStrategy();
        ArrayList bounds = new ArrayList();
        ControlParameter[] params = new ControlParameter[2];
        params[0] = ConstantControlParameter.of(1.0);
        params[1] = ConstantControlParameter.of(3.0);
        bounds.add(params);
        params = new ControlParameter[2];
        params[0] = ConstantControlParameter.of(0.5);
        params[1] = ConstantControlParameter.of(2.1);
        bounds.add(params);       
        params = new ControlParameter[2];
        params[0] = ConstantControlParameter.of(1.0);
        params[1] = ConstantControlParameter.of(3.0);
        bounds.add(params);
        params = new ControlParameter[2];
        params[0] = ConstantControlParameter.of(0.5);
        params[1] = ConstantControlParameter.of(2.1);
        bounds.add(params);
        params = new ControlParameter[2];
        params[0] = ConstantControlParameter.of(0.5);
        params[1] = ConstantControlParameter.of(2.1);
        bounds.add(params);
        strategy.setBounds(bounds);
        
        instance.setCentroidInitialisationStrategy(strategy);
        
        instance.initialise(problem);
        
        CentroidHolder holder = (CentroidHolder) instance.getCandidateSolution().getClone();
        
        instance.reinitialise();
        
        Assert.assertNotSame(holder, (CentroidHolder) instance.getCandidateSolution());
    }

    /**
     * Test of setNeighbourhoodBest method, of class ClusterParticle.
     */
    @Test
    public void testSetNeighbourhoodBest() {
        System.out.println("setNeighbourhoodBest");
        ClusterParticle instance = new ClusterParticle();
        ClusterParticle neighbourhoodBest = new ClusterParticle();
        
        CentroidHolder holder = new CentroidHolder();
        ClusterCentroid centroid = ClusterCentroid.of(1,2,3,4,5,6);
        centroid.setDataItemDistances(new double[]{1,2,3,4});
        holder.add(centroid);
        holder.add(centroid);
        holder.add(centroid);
        
        neighbourhoodBest.setCandidateSolution(holder);
        neighbourhoodBest.getProperties().put(EntityType.Particle.VELOCITY, holder);
        neighbourhoodBest.getProperties().put(EntityType.Particle.BEST_POSITION, holder);
        
        instance.setNeighbourhoodBest(neighbourhoodBest);
        
        Assert.assertEquals(instance.getNeighbourhoodBest(), neighbourhoodBest);
    }

   
}
