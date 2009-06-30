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

import java.util.ArrayList;

import net.sourceforge.cilib.neuralnetwork.generic.Weight;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.NeuronConfig;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @author stefanv
 *
 */
public class SpecificWeightInitialiser implements GenericTopologyVisitor {

    ArrayList<Weight> weights = null;

    public SpecificWeightInitialiser() {
        super();
        this.weights = null;
    }


    public void visitNeuronConfig(NeuronConfig n) {

        if(n.getInputWeights() != null){

           Weight[] tmp = new Weight[n.getInputWeights().length];
           for (int i = 0; i < tmp.length; i++){
                   tmp[i] = weights.remove(0);
           }
           n.setInputWeights(tmp);
        }

    }

    public void setWeights(Vector w) {

        weights = new ArrayList<Weight>();
        for (int i = 0; i < w.size(); i++){
            weights.add(new Weight(w.get(i)));
        }
    }

    public boolean isEmpty(){
        return weights.size() == 0;
    }

}
