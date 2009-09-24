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

import net.sourceforge.cilib.nn.architecture.Architecture;
import net.sourceforge.cilib.nn.architecture.Layer;
import net.sourceforge.cilib.nn.components.Neuron;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * The visitor calculates the output error as the difference between the target
 * output and the output layer output. The differences is not squared or summed.
 * @author andrich
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
        output = new Vector(layerSize);
        for (int k = 0; k < layerSize; k++) {
            Neuron currentNeuron = outputLayer.get(k);
            double t_k = layerSize > 1 ? ((Vector) input.getTarget()).getReal(k) : ((Real)input.getTarget()).getReal();
            double o_k = currentNeuron.getActivation();
            double tmp = (t_k - o_k);
            output.add(new Real(tmp));
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean isDone() {
        return false;
    }

}
