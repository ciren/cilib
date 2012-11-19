/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.clustervalidity;

import junit.framework.Assert;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Test;

public class DunnValidityIndexTest {
    
    /**
     * Test of getMinimumIntraclusterDistance method, of class DunnValidityIndex.
     */
    @Test
    public void testGetMinimumIntraclusterDistance() {
        ClusterCentroid cluster1 = ClusterCentroid.of(1,5);
        cluster1.addDataItem(0, Vector.of(0.5,7));
        cluster1.addDataItem(0, Vector.of(1.5,6));
        cluster1.addDataItem(0, Vector.of(1.4,4));
        ClusterCentroid cluster2 = ClusterCentroid.of(3,2);
        cluster2.addDataItem(0, Vector.of(2,2.5));
        cluster2.addDataItem(0, Vector.of(4,1.5));
        DunnValidityIndex instance = new DunnValidityIndex();
        
        double distance = instance.getMinimumIntraclusterDistance(cluster1, cluster2);
        
        Assert.assertEquals(Math.round(1.6155494421403512093752131474621 * 1e10)/1e10 , Math.round(distance * 1e10) / 1e10);
    }

    /**
     * Test of getMaximumInterclusterDistance method, of class DunnValidityIndex.
     */
    @Test
    public void testGetMaximumInterclusterDistance() {
        ClusterCentroid cluster1 = ClusterCentroid.of(1,5);
        cluster1.addDataItem(0, Vector.of(0.5,7));
        cluster1.addDataItem(0, Vector.of(1.5,6));
        cluster1.addDataItem(0, Vector.of(1.4,4));
        ClusterCentroid cluster2 = ClusterCentroid.of(3,2);
        cluster2.addDataItem(0, Vector.of(2,2.5));
        cluster2.addDataItem(0, Vector.of(4,1.5));
        DunnValidityIndex instance = new DunnValidityIndex();
        
        double distance = instance.getMaximumInterclusterDistance(cluster1);
        
        Assert.assertEquals(Math.round(3.1320919526731650539273262067644 * 1e10) / 1e10, Math.round(distance * 1e10) / 1e10);
    }

}
