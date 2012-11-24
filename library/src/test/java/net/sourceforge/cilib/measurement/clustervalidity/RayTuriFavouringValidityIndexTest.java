/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.clustervalidity;

import junit.framework.Assert;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import org.junit.Test;

public class RayTuriFavouringValidityIndexTest {
    
    /**
     * Test of getGaussianValue method, of class RayTuriFavouringValidityIndex.
     */
    @Test
    public void testGetGaussianValue() {
        ClusterCentroid cluster1 = ClusterCentroid.of(1,5);
        ClusterCentroid cluster2 = ClusterCentroid.of(3,2);
        ClusterCentroid cluster3 = ClusterCentroid.of(3,2);
        
        CentroidHolder holder = new CentroidHolder();
        holder.add(cluster1);
        holder.add(cluster2);
        holder.add(cluster3);
        
        RayTuriFavouringValidityIndex instance = new RayTuriFavouringValidityIndex();
        double result = instance.getGaussianValue(holder);
        
        Assert.assertEquals(Math.round(0.00443184841194 * 1e10) / 1e10, Math.round(result * 1e10) / 1e10);
    }
}
