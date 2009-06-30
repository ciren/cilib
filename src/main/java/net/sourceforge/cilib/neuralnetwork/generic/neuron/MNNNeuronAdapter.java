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
package net.sourceforge.cilib.neuralnetwork.generic.neuron;

import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.neuralnetwork.generic.GenericTopology;
import net.sourceforge.cilib.neuralnetwork.generic.datacontainers.StandardPattern;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * TODO: Complete this javadoc.
 */
//This class is not fully completed and tested.... Draft version
public class MNNNeuronAdapter extends NeuronConfig {

    GenericTopology topology;

    public Type computeActivationFunctionDerivativeAtPos(Type pos) {
        throw new IllegalArgumentException("Method is unimplemented - please use a subclass.");
    }

    public Type computeActivationFunctionDerivativeUsingLastOutput(Type lastOutput) {
        throw new IllegalArgumentException("Method is unimplemented - please use a subclass.");
    }

    public Type computeOutput(NeuronConfig n, NNPattern p) {

        Vector inputMNN = new Vector();
        Vector weightsMNN = new Vector();

        for (int i = 0; i < this.input.length; i++){
            Vector tmpInput = ((Vector) input[i].getCurrentOutput());
            Vector tmpWeight = ((Vector) inputWeights[i].getWeightValue());

            //copy relevant weights and inputs to vector
            for (int j = 0; j < tmpInput.size(); j++){
                if (tmpWeight.get(j) != null){
                    inputMNN.add(tmpInput.get(j));
                    weightsMNN.add(tmpWeight.get(j));
                }
            }
        }//end for i

        //compute output
        NNPattern pat = new StandardPattern();
        pat.setInput(inputMNN);
        return this.topology.evaluate(pat);
    }

    public Type computeOutputFunctionDerivativeAtPos(Type pos) {
        throw new IllegalArgumentException("Method is unimplemented - please use a subclass.");
    }

    public Type computeOutputFunctionDerivativeUsingLastOutput(Type lastOutput) {
        throw new IllegalArgumentException("Method is unimplemented - please use a subclass.");
    }

}
