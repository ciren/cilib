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
package net.sourceforge.cilib.functions.clustering.clustercenterstrategies;

import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.type.types.StringType;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

/**
 * @author Theuns Cloete
 */
public class ClusterMeanStrategyTest {

    private static ClusterCenterStrategy centerStrategy;
    private static Cluster cluster;

    @BeforeClass
    public static void setUpClass() throws Exception {
        centerStrategy = new ClusterMeanStrategy();
        cluster = new Cluster(Vector.of(1.0, 2.0, 3.0));
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        centerStrategy = null;
    }

    @After
    public void AfterEachMethod() {
        cluster.clear();
    }

    @Test
    public void testGetCenter() {
        cluster.add(new StandardPattern(Vector.of(1.0, 2.0, 3.0), new StringType("one")));
        assertThat(centerStrategy.getCenter(cluster), equalTo(Vector.of(1.0, 2.0, 3.0)));
        cluster.add(new StandardPattern(Vector.of(2.0, 3.0, 4.0), new StringType("two")));
        assertThat(centerStrategy.getCenter(cluster), equalTo(Vector.of(1.5, 2.5, 3.5)));
        cluster.add(new StandardPattern(Vector.of(3.0, 4.0, 5.0), new StringType("three")));
        assertThat(centerStrategy.getCenter(cluster), equalTo(Vector.of(2.0, 3.0, 4.0)));
    }

    @Test(expected=IllegalStateException.class)
    public void testGetCenterEmptyCluster() {
        assertThat(centerStrategy.getCenter(cluster), nullValue());
    }
}
