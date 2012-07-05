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

import net.sourceforge.cilib.functions.activation.Linear;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.nn.architecture.ForwardingLayer;
import net.sourceforge.cilib.nn.architecture.Layer;
import net.sourceforge.cilib.nn.components.BiasNeuron;
import net.sourceforge.cilib.nn.components.Neuron;
import net.sourceforge.cilib.nn.components.PatternInputSource;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class CascadeLayerBuilderTest {

	private ForwardingLayer inputLayer;
    private Vector input;

    @Before
    public void setup() {
        input = Vector.of(0.1, 0.2);
        StandardPattern pattern = new StandardPattern(input, input);
        inputLayer = new ForwardingLayer();
        inputLayer.setSource(new PatternInputSource(pattern));
        inputLayer.add(new BiasNeuron());
    }

	@Test
    public void testWeightVectorSizes() {
		CascadeLayerBuilder layerBuilder = new CascadeLayerBuilder();
		layerBuilder.setDomain("R(-3:3)");
		
		LayerConfiguration layerConfiguration = new LayerConfiguration(3, new Linear());
		Layer cascadeLayer = layerBuilder.buildLayer(layerConfiguration, 3);

		Assert.assertEquals(3, cascadeLayer.get(0).getNumWeights());
		Assert.assertEquals(4, cascadeLayer.get(1).getNumWeights());
		Assert.assertEquals(5, cascadeLayer.get(2).getNumWeights());
    }
	
    @Test
    public void testNeuralInput() {
		CascadeLayerBuilder layerBuilder = new CascadeLayerBuilder();
		layerBuilder.setDomain("R(-3:3)");
		
		LayerConfiguration layerConfiguration = new LayerConfiguration(3, new Linear());
		Layer cascadeLayer = layerBuilder.buildLayer(layerConfiguration, 3);

		int currentIndex = 0;
		Vector weights = Vector.of(0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1.0,1.1,1.2);
		for (Neuron neuron : cascadeLayer) {
            Vector neuronWeights = neuron.getWeights();
            int size = neuronWeights.size();
            for (int j = 0; j < size; j++) {
                neuronWeights.set(j, weights.get(currentIndex++));
            }
        }

		Layer consolidatedLayer = new Layer();
		for (int i = 0; i < inputLayer.size(); i++) {
            consolidatedLayer.add(inputLayer.get(i));
        }
		for (Neuron neuron : cascadeLayer) {
            consolidatedLayer.add(neuron);
        }

		Assert.assertEquals(6, consolidatedLayer.size());

		for (int n = 0; n < cascadeLayer.size(); n++) {
            cascadeLayer.get(n).calculateActivation(consolidatedLayer);
        }
		
        Assert.assertEquals(0.1*0.1 + 0.2*0.2 - 0.3, cascadeLayer.getNeuralInput(0), Maths.EPSILON);
		Assert.assertEquals(0.4*0.1 + 0.5*0.2 - 0.6
		                    + 0.7*(0.1*0.1 + 0.2*0.2 - 0.3), cascadeLayer.getNeuralInput(1), Maths.EPSILON);
		Assert.assertEquals(0.8*0.1 + 0.9*0.2 - 1.0
		                    + 1.1*(0.1*0.1 + 0.2*0.2 - 0.3)
		                    + 1.2*(0.4*0.1 + 0.5*0.2 - 0.6 + 0.7*(0.1*0.1 + 0.2*0.2 - 0.3)), cascadeLayer.getNeuralInput(2), Maths.EPSILON);
	}

	@Test
	public void testPrototypeNeuronAccessors() {
		CascadeLayerBuilder layerBuilder = new CascadeLayerBuilder();
		layerBuilder.setDomain("R(-3:3)");
		Neuron testNeuron = new Neuron();
		layerBuilder.setPrototypeNeuron(testNeuron);
		Assert.assertEquals(testNeuron, layerBuilder.getPrototypeNeuron());
	}
}
