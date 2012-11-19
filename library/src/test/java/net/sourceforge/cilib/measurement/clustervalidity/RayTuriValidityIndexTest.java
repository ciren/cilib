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
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Test;

public class RayTuriValidityIndexTest {
    
    /**
     * Test of getaverageClusterDistance method, of class RayTuriValidityIndex.
     */
    @Test
    public void testGetIntraclusterDistance() {
        ClusterCentroid cluster1 = ClusterCentroid.of(1,2);
        cluster1.addDataItem(0, Vector.of(2,2));
        cluster1.addDataItem(0, Vector.of(3,1));
        ClusterCentroid cluster2 = ClusterCentroid.of(3,4);
        cluster2.addDataItem(0, Vector.of(6,1));
        cluster2.addDataItem(0, Vector.of(2,4));
        
        CentroidHolder holder = new CentroidHolder();
        holder.add(cluster1);
        holder.add(cluster2);
        
        RayTuriValidityIndex instance = new RayTuriValidityIndex();
        double result = instance.getaverageClusterDistance(holder);
        
        Assert.assertEquals(Math.round(2.119677166154775 * 1e10) / 1e10, Math.round(result * 1e10) / 1e10);
    }

    /**
     * Test of getInterClusterDistance method, of class RayTuriValidityIndex.
     */
    @Test
    public void testGetInterClusterDistance() {
        ClusterCentroid cluster1 = ClusterCentroid.of(1,2);
        cluster1.addDataItem(0, Vector.of(2,2));
        cluster1.addDataItem(0, Vector.of(3,1));
        ClusterCentroid cluster2 = ClusterCentroid.of(3,4);
        cluster2.addDataItem(0, Vector.of(6,1));
        cluster2.addDataItem(0, Vector.of(2,4));
        
        CentroidHolder holder = new CentroidHolder();
        holder.add(cluster1);
        holder.add(cluster2);
        
        RayTuriValidityIndex instance = new RayTuriValidityIndex();
        double result = instance.getInterClusterDistance(holder);
        
        Assert.assertEquals(Math.round(2.8284271247462 * 1e10) / 1e10, Math.round(result * 1e10) / 1e10);
    }
}
