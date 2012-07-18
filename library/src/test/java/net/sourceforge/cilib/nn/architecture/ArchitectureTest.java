/**
 * Computational Intelligence Library (CIlib) Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP) Department of Computer
 * Science University of Pretoria South Africa
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.nn.architecture;

import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.nn.architecture.builder.LayerConfiguration;
import net.sourceforge.cilib.nn.architecture.visitors.WeightRetrievalVisitor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ArchitectureTest {

    private NeuralNetwork network;

    @Before
    public void setup() {
        network = new NeuralNetwork();
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(5));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1));
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        network.initialize();
    }

    @Test
    public void testInitialize() {
        // assert number of layers, it is not necessary to test constructed components
        // as they have their own unit tests.
        Assert.assertEquals(3, network.getArchitecture().getNumLayers());
    }

    @Test
    public void testAccept() {
        // this is mostly to test whether the visit actually occurs and not whether
        // the visitor works.
        WeightRetrievalVisitor visitor = new WeightRetrievalVisitor();
        network.getArchitecture().accept(visitor);
        Assert.assertEquals(22, visitor.getOutput().size());
    }
}
