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
package net.sourceforge.cilib.nn;

import net.sourceforge.cilib.io.exception.CIlibIOException;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.nn.architecture.builder.LayerConfiguration;
import net.sourceforge.cilib.nn.architecture.visitors.FeedForwardVisitor;
import net.sourceforge.cilib.nn.components.PatternInputSource;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author andrich
 */
public class NeuralNetworkTest {

    private NeuralNetwork network;

    @Before
    public void setup() throws CIlibIOException {
        network = new NeuralNetwork();
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(5));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3,3)");
        network.initialize();
    }

    @Test
    public void testEvaluate() {
        network.setOperationVisitor(new FeedForwardVisitor());
        Vector input = Vectors.create(0.1, 0.2, 0.3, 0.4, 0.5);
        StandardPattern pattern = new StandardPattern(input, input);

        FFNNTopology topology = new FFNNTopology(5, 3, 2, 0.0, 0.0);
        topology.setWeights(network.getWeights());
        network.setWeights(topology.getWeights());
        for (int i = 0; i < 2; i++) {
            Assert.assertEquals(((Real)topology.evaluate(new PatternInputSource(pattern)).get(i)).getReal(),
                    ((Real)network.evaluatePattern(pattern).get(i)).getReal(), Maths.EPSILON);
        }
    }
}
