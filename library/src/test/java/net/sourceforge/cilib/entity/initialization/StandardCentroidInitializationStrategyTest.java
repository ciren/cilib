/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
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
import org.junit.Test;

public class StandardCentroidInitializationStrategyTest {
    
    /**
     * Test of initialize method, of class StandardCentroidInitializationStrategy.
     */
    @Test
    public void testInitialize() {
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
