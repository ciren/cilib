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
package net.sourceforge.cilib.nn.architecture.visitors;

import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.nn.architecture.Layer;
import net.sourceforge.cilib.nn.architecture.builder.LayerConfiguration;
import net.sourceforge.cilib.nn.components.Neuron;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class WeightSettingVisitorTest {

    @Test
    public void testVisit() {
        NeuralNetwork network = new NeuralNetwork();
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(5, true));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3, true));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        network.initialize();

        Vector.Builder expectedWeightsBuilder = Vector.newBuilder();
        for (int i = 0; i < network.getWeights().size(); i++) {
            expectedWeightsBuilder.add(Real.valueOf(Math.random()));
        }
        Vector expectedWeights = expectedWeightsBuilder.build();

        WeightSettingVisitor visitor = new WeightSettingVisitor(expectedWeights);
        visitor.visit(network.getArchitecture());

        Vector.Builder actualWeightsBuilder = Vector.newBuilder();
        int numLayers = network.getArchitecture().getNumLayers();
        for (int i = 1; i < numLayers; i++) {
            Layer layer = network.getArchitecture().getLayers().get(i);
            for (Neuron neuron : layer) {
                actualWeightsBuilder.copyOf(neuron.getWeights());
            }
        }

        Assert.assertEquals(expectedWeights, actualWeightsBuilder.build());
    }
}
