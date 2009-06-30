/**
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
package net.sourceforge.cilib.neuralnetwork.generic.topologyvisitors;

import net.sourceforge.cilib.neuralnetwork.generic.Weight;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.NeuronConfig;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * TODO: Complete this javadoc.
 */
public class WeightExtractingVisitor implements GenericTopologyVisitor {

    private Vector weights = null;

    public WeightExtractingVisitor() {
        this.weights = new Vector();
    }

    public void visitNeuronConfig(NeuronConfig n) {

        if(n.getInputWeights() != null){
            Weight[] neuronWeights = n.getInputWeights();

            for (int i = 0; i < neuronWeights.length; i++){
                weights.add((Numeric) neuronWeights[i].getWeightValue().getClone());
            }
        }

    }

    public Vector getWeights() {
        return weights;
    }

    public void setWeights(Vector weights) {
        throw new IllegalArgumentException("This operation is not supported for this visitor.");
    }

}
