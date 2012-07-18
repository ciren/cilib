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

import java.util.List;
import net.sourceforge.cilib.nn.architecture.Architecture;
import net.sourceforge.cilib.nn.architecture.Layer;
import net.sourceforge.cilib.nn.components.Neuron;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Visitor retreives all the weights in the architecture, starting with the far
 * left hidden layer and top neuron, moving down and then to the layer to the right.
 */
public class WeightRetrievalVisitor extends ArchitectureOperationVisitor {

    /**
     * Retrieves the weights.
     * @param architecture the architecture to visit.
     */
    @Override
    public void visit(Architecture architecture) {
        List<Layer> layers = architecture.getLayers();

        Vector.Builder outputBuilder = Vector.newBuilder();
        int numLayers = layers.size();
        for (int i = 1; i < numLayers; i++) {
            Layer layer = layers.get(i);
            for (Neuron neuron : layer) {
                outputBuilder.copyOf(neuron.getWeights());
            }
        }
        
        output = outputBuilder.build();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean isDone() {
        return false;
    }
}
