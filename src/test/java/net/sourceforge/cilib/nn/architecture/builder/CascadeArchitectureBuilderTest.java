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
package net.sourceforge.cilib.nn.architecture.builder;

import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.nn.architecture.Layer;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class CascadeArchitectureBuilderTest {

    @Test
    public void testBuildArchitecture() {
		Vector input = Vector.of(0.1, 0.2);
        StandardPattern pattern = new StandardPattern(input, input);
		
        NeuralNetwork network = new NeuralNetwork();
		network.getArchitecture().setArchitectureBuilder(new CascadeArchitectureBuilder());
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        network.initialize();

        //assert number of layers
        Assert.assertEquals(3, network.getArchitecture().getNumLayers());

        //assert number of weights
        Assert.assertEquals(24, network.getWeights().size());

        //assert layer sizes
        Assert.assertEquals(3, network.getArchitecture().getLayers().get(0).size());
        Assert.assertEquals(3, network.getArchitecture().getLayers().get(1).size());
        Assert.assertEquals(2, network.getArchitecture().getLayers().get(2).size());

		//assert the number of weights for each hidden neuron
		Assert.assertEquals(3, network.getArchitecture().getLayers().get(1).get(0).getNumWeights());
		Assert.assertEquals(4, network.getArchitecture().getLayers().get(1).get(1).getNumWeights());
		Assert.assertEquals(5, network.getArchitecture().getLayers().get(1).get(2).getNumWeights());

		//assert the number of weights for each output neuron
		Assert.assertEquals(6, network.getArchitecture().getLayers().get(2).get(0).getNumWeights());
		Assert.assertEquals(6, network.getArchitecture().getLayers().get(2).get(1).getNumWeights());
    }

	@Test (expected = UnsupportedOperationException.class)
	public void testEnforceMinimumNumberOfLayers() {
		NeuralNetwork network = new NeuralNetwork();
		network.getArchitecture().setArchitectureBuilder(new CascadeArchitectureBuilder());
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        network.initialize();
	}

	@Test (expected = UnsupportedOperationException.class)
	public void testEnforceMaximumNumberOfLayers() {
		NeuralNetwork network = new NeuralNetwork();
		network.getArchitecture().setArchitectureBuilder(new CascadeArchitectureBuilder());
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        network.initialize();
	}
}
