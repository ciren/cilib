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
import net.sourceforge.cilib.type.types.container.Vector;
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
public class DaviesBouldinValidityIndexTest {
    
    public DaviesBouldinValidityIndexTest() {
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
     * Test of getMaximumInterclusterDistance method, of class DaviesBouldinValidityIndex.
     */
    @Test
    public void testGetMaximumInterclusterDistance() {
        System.out.println("getMaximumInterclusterDistance");
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
