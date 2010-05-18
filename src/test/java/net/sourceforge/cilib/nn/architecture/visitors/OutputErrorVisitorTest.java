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

import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.nn.architecture.builder.LayerConfiguration;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author andrich
 */
public class OutputErrorVisitorTest {

    @Test
    public void testVisit() {
        Vector vector = Vectors.create(0.1, 0.2, 0.3, 0.4, 0.5);
        Vector target = Vectors.create(0.9, 0.9);
        StandardPattern pattern = new StandardPattern(vector,target);

        NeuralNetwork network = new NeuralNetwork();
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(5,true));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3,true));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3,3)");
        network.initialize();

        ArchitectureOperationVisitor visitor = new FeedForwardVisitor();
        visitor.setInput(pattern);
        visitor.visit(network.getArchitecture());
        Vector output = visitor.getOutput();
        
        visitor = new OutputErrorVisitor();
        visitor.setInput(pattern);
        visitor.visit(network.getArchitecture());
        Vector outputError = visitor.getOutput();
        
        Vector referenceError = new Vector();
        for (int i = 0; i < output.size(); i++) {
            referenceError.add(new Real(target.getReal(i) - output.getReal(i)));
        }

        Assert.assertEquals(referenceError, outputError);
    }

}
