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

import net.sourceforge.cilib.neuralnetwork.generic.neuron.NeuronConfig;

/**
 * @author stefanv
 *
 * A Standard iterator for traversing a layer of NeuronConfoig objects in order.
 */
public class StandardLayerIterator implements LayerIterator {


    int currentNeuron = 0;
    ArrayList<NeuronConfig> layer = null;



    public StandardLayerIterator(ArrayList<NeuronConfig> layer) {
        this.layer = layer;
        currentNeuron = 0;
    }


    public void nextNeuron() {
        currentNeuron++;
    }


    public NeuronConfig value() {
        return layer.get(currentNeuron);
    }



    public int getNrNeurons() {
        return layer.size();
    }

    public boolean hasMore(){
        return currentNeuron < layer.size();
    }



    public void reset() {
        currentNeuron = 0;
    }



    public int currentPosition() {
        return currentNeuron;
    }

}
