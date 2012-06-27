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

import net.sourceforge.cilib.nn.architecture.Architecture;
import net.sourceforge.cilib.nn.architecture.Layer;
import net.sourceforge.cilib.nn.components.Neuron;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * The visitor calculates the output error as the difference between the target
 * output and the output layer output. The differences is not squared or summed.
 */
public class OutputErrorVisitor extends ArchitectureOperationVisitor {

    /**
     * Calculate the output error given the StandardPattern that contains the target
     * as {@link #input} .
     * @param architecture the architecture to visit.
     */
    @Override
    public void visit(Architecture architecture) {
        Layer outputLayer = architecture.getLayers().get(architecture.getNumLayers() - 1);
        int layerSize = outputLayer.size();
        Vector.Builder outputBuilder = Vector.newBuilder();
        for (int k = 0; k < layerSize; k++) {
            Neuron currentNeuron = outputLayer.get(k);
            double t_k = layerSize > 1 ? ((Vector) input.getTarget()).doubleValueOf(k) : ((Real)input.getTarget()).doubleValue();
            double o_k = currentNeuron.getActivation();
            double tmp = (t_k - o_k);
            outputBuilder.add(tmp);
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
