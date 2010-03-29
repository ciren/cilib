/*
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.nn.architecture.visitors;

import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.nn.FFNNTopology;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.nn.architecture.builder.LayerConfiguration;
import net.sourceforge.cilib.nn.components.PatternInputSource;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author andrich
 */
public class BackPropagationVisitorTest {

    @Test
    public void testVisit3Layers() {
        Vector input = Vectors.create(0.1, 0.2, 0.3, 0.4, 0.5);
        Vector target = Vectors.create(0.1, 0.9);
        StandardPattern pattern = new StandardPattern(input, target);

        NeuralNetwork network = new NeuralNetwork();
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(5));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3,3)");
        network.initialize();

        FFNNTopology topology = new FFNNTopology(5, 3, 2, 0.1, 0.9);

        topology.setWeights(network.getWeights());

        BackPropagationVisitor visitor = new BackPropagationVisitor();
        double[][] previousWeightUpdates = null;

        for (int i = 0; i < 3; i++) {
            //FF
            topology.evaluate(new PatternInputSource(pattern));
            network.evaluatePattern(pattern);

            //BP
            topology.train();

            visitor.setPreviousPattern(pattern);
            visitor.setPreviousWeightUpdates(previousWeightUpdates);
            visitor.visit(network.getArchitecture());
            previousWeightUpdates = visitor.getPreviousWeightUpdates();

            Vector topologyWeights = topology.getWeights();
            Vector networkWeights = network.getWeights();
            for (int j = 0; j < topology.getWeights().size(); j++) {
                Assert.assertEquals(topologyWeights.doubleValueOf(j), networkWeights.doubleValueOf(j), Maths.EPSILON);
            }
        }

    }

    /*
     * This does not actually assert anything (I have nothing to compare against)
     * it just runs and if an exception occurs, the test will fail.
     */
    @Test
    public void testVisit5Layers() {
        Vector input = Vectors.create(0.1, 0.2, 0.3, 0.4, 0.5);
        Real target = new Real(0.9);
        StandardPattern pattern = new StandardPattern(input, target);

        NeuralNetwork network = new NeuralNetwork();
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(5));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1));
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3,3)");
        network.initialize();

        BackPropagationVisitor visitor = new BackPropagationVisitor();
        double[][] previousWeightUpdates = null;

        for (int i = 0; i < 3; i++) {

            //FF
            network.evaluatePattern(pattern);

            //BP
            visitor.setPreviousPattern(pattern);
            visitor.setPreviousWeightUpdates(previousWeightUpdates);
            visitor.visit(network.getArchitecture());
            previousWeightUpdates = visitor.getPreviousWeightUpdates();
        }
    }
}
