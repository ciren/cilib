/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.initialization;

import junit.framework.Assert;
import net.sourceforge.cilib.clustering.SlidingWindow;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import org.junit.Test;

public class DataPatternInitializationStrategyTest {
    
    /**
     * Test of initialize method, of class DataPatternInitializationStrategy.
     */
    @Test
    public void testInitialize() {
        SlidingWindow window = new SlidingWindow();
        window.setWindowSize(3);
        window.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        window.initializeWindow();
        DataTable table = window.getCurrentDataset();
        
        ClusterParticle particle  = new ClusterParticle();
        CentroidHolder holder = new CentroidHolder();
        holder.add(ClusterCentroid.of(0,0));
        particle.setCandidateSolution(holder);
        
        DataPatternInitializationStrategy strategy = new DataPatternInitializationStrategy();
        strategy.setDataset(table);
        strategy.initialize(EntityType.CANDIDATE_SOLUTION, particle);
        
        Assert.assertTrue((((CentroidHolder)particle.getCandidateSolution()).get(0).containsAll(ClusterCentroid.of(1.0,1.0,1.0,2.0))) 
                || (((CentroidHolder)particle.getCandidateSolution()).get(0).containsAll(ClusterCentroid.of(2.0,3.0,4.0,2.0))) 
                || (((CentroidHolder)particle.getCandidateSolution()).get(0).containsAll(ClusterCentroid.of(1.0,1.0,1.0,1.0))));
    }

    /**
     * Test of setDataset method, of class DataPatternInitializationStrategy.
     */
    @Test
    public void testSetDataset() {
        SlidingWindow window = new SlidingWindow();
        window.setWindowSize(3);
        window.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        window.initializeWindow();
        DataTable table = window.getCurrentDataset();
        
        DataPatternInitializationStrategy strategy = new DataPatternInitializationStrategy();
        strategy.setDataset(table);
        
        Assert.assertEquals(table, strategy.getDataset());
    }

    /**
     * Test of getDataset method, of class DataPatternInitializationStrategy.
     */
    @Test
    public void testGetDataset() {
        SlidingWindow window = new SlidingWindow();
        window.setWindowSize(3);
        window.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        window.initializeWindow();
        DataTable table = window.getCurrentDataset();
        
        DataPatternInitializationStrategy strategy = new DataPatternInitializationStrategy();
        strategy.setDataset(table);
        
        Assert.assertEquals(table, strategy.getDataset());
    }
}
