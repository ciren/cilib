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
package net.sourceforge.cilib.neuralnetwork.generic;

import java.util.ArrayList;

import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.NeuronConfig;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.TypeList;

/**
 * TODO: Complete this javadoc.
 */
public class LayeredGenericTopology extends GenericTopology {


    /*
     * This class uses a "top to bottom, left to right" way of
     * evaluating neuron output.
     *
     */


    public LayeredGenericTopology() {
        super();
    }

    public void initialize(){
        super.initialize();
    }

    @Override
    public TypeList evaluate(NNPattern p){

        TypeList output = new TypeList();

        for (int layer = 0; layer < layerList.size(); layer++){

            ArrayList<NeuronConfig> neuronList = layerList.get(layer);
            for (int i = 0; i < neuronList.size(); i++){

                NeuronConfig neuron = neuronList.get(i);
                Type result = neuron.computeOutput(neuron, p);

                //keep track of previous outputs.
                neuron.setTminus1Output(neuron.getCurrentOutput());
                neuron.setCurrentOutput(result);

                if (neuron.isOutputNeuron())
                    output.add(result);
            }//end for i

        }

        return output;

    }

}
