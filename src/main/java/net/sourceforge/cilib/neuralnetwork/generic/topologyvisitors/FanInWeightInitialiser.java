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
import net.sourceforge.cilib.type.types.Real;

/**
 * TODO: Complete this javadoc.
 */
public class FanInWeightInitialiser implements GenericTopologyVisitor {

    public FanInWeightInitialiser() {
        super();
    }

    public void visitNeuronConfig(NeuronConfig n) {

        //first initialise all neuron-to-neuron weights if they exist
        if (n.getInputWeights() != null){
            double fanInRange = 1.0 / Math.sqrt((double) n.getInputWeights().length);

            for (int i = 0; i < n.getInputWeights().length; i++){

                Weight w = n.getInputWeights()[i];
                //set weight value in range [-1/sqrt(fanIn), 1/sqrt(fanIn)]
                w.setWeightValue(new Real((Math.random() * 2 * fanInRange) - fanInRange));
            }
        }

        //also initialise pattern-to-neuron weight if they exist.
        //default = 1
        if (n.getPatternWeight() != null){
            n.getPatternWeight().setWeightValue(new Real(1.0));
        }

    }

}
