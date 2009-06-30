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
 * @author stefanv
 *
 */
public class RandomWeightInitialiser implements GenericTopologyVisitor {

    //the range over which values may be initiated.
    //offset is center from  of the distribution
    //i.e. range = 2, offset 0 means range [-1,1]
    double range = 2;
    double offset = 0;

    public RandomWeightInitialiser() {
        this.range = 2;
        this.offset = 0;
    }

    public void visitNeuronConfig(NeuronConfig n) {

        //first initialise all neuron-to-neuron weights if they exist
        if (n.getInputWeights() != null) {
            for (int i = 0; i < n.getInputWeights().length; i++) {
                Weight w = n.getInputWeights()[i];
                w.setWeightValue(new Real((Math.random() * range) - range / 2.0 + offset));
            }
        }

        //also initialise pattern-to-neuron weight if they exist.
        //default = 1
        if (n.getPatternWeight() != null) {
            n.getPatternWeight().setWeightValue(new Real(1.0));
        }

    }

    public double getOffset() {
        return offset;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }
}
