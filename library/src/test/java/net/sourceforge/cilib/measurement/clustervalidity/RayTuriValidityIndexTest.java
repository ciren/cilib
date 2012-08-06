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
package net.sourceforge.cilib.measurement.clustervalidity;

import junit.framework.Assert;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RayTuriValidityIndexTest {
    
    public RayTuriValidityIndexTest() {
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
