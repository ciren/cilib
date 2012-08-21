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

import java.util.ArrayList;
import junit.framework.Assert;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class StandardCentroidInitializationStrategyTest {
    
    public StandardCentroidInitializationStrategyTest() {
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
     * Test of initialize method, of class StandardCentroidInitializationStrategy.
     */
    @Test
    public void testInitialize() {
        System.out.println("initialize");
        StandardCentroidInitializationStrategy instance = new StandardCentroidInitializationStrategy();
        ArrayList<ControlParameter[]> bounds = new ArrayList<ControlParameter[]>();
        ControlParameter[] bound1 =  {ConstantControlParameter.of(1.0), ConstantControlParameter.of(3.0)};
        ControlParameter[] bound2 =  {ConstantControlParameter.of(1.2), ConstantControlParameter.of(5.1)};
        bounds.add(bound1);
        bounds.add(bound2);
        instance.setBounds(bounds);
        
        ClusterParticle particle  = new ClusterParticle();
        CentroidHolder holder = new CentroidHolder();
        holder.add(ClusterCentroid.of(0,0));
        holder.add(ClusterCentroid.of(0,0));
        holder.add(ClusterCentroid.of(0,0));
        particle.setCandidateSolution(holder);
        instance.initialize(EntityType.CANDIDATE_SOLUTION, particle);
       
        Assert.assertTrue(particle.getCandidateSolution() instanceof CentroidHolder);
        Assert.assertTrue((((CentroidHolder) particle.getCandidateSolution()).get(0).get(0).doubleValue() < 3.0) && 
                (((CentroidHolder) particle.getCandidateSolution()).get(0).get(0).doubleValue() > 1.0) );
        Assert.assertTrue((((CentroidHolder) particle.getCandidateSolution()).get(0).get(1).doubleValue() < 5.1) && 
                (((CentroidHolder) particle.getCandidateSolution()).get(0).get(1).doubleValue() > 1.2) );
    }

    /**
     * Test of reinitialize method, of class StandardCentroidInitializationStrategy.
     */
    @Test
    public void testReinitialize() {
        System.out.println("reinitialize");
        
        StandardCentroidInitializationStrategy instance = new StandardCentroidInitializationStrategy();
        ArrayList<ControlParameter[]> bounds = new ArrayList<ControlParameter[]>();
        ControlParameter[] bound1 =  {ConstantControlParameter.of(1.0), ConstantControlParameter.of(3.0)};
        ControlParameter[] bound2 =  {ConstantControlParameter.of(1.2), ConstantControlParameter.of(5.1)};
        bounds.add(bound1);
        bounds.add(bound2);
        instance.setBounds(bounds);
        
        ClusterParticle particle  = new ClusterParticle();
        CentroidHolder holder = new CentroidHolder();
        holder.add(ClusterCentroid.of(0,0));
        holder.add(ClusterCentroid.of(0,0));
        holder.add(ClusterCentroid.of(0,0));
        particle.setCandidateSolution(holder);
        instance.initialize(EntityType.CANDIDATE_SOLUTION, particle);
        
        CentroidHolder solutionBefore = (CentroidHolder) particle.getCandidateSolution().getClone();
        instance.reinitialize(EntityType.CANDIDATE_SOLUTION, particle);
        CentroidHolder solutionAfter = (CentroidHolder) particle.getCandidateSolution().getClone();
        
        Assert.assertFalse(solutionAfter.containsAll(solutionBefore));
        
    }
}
