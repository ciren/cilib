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

import java.util.List;
import net.sourceforge.cilib.nn.architecture.Architecture;
import net.sourceforge.cilib.nn.architecture.Layer;
import net.sourceforge.cilib.nn.components.Neuron;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Visitor that takes a {@link Vector} of weights as input to set the weights of
 * the neurons in an architecture. The weight values are not cloned.
 * @author andrich
 */
public class WeightSettingVisitor implements ArchitectureVisitor {

    private Vector weights;

    /**
     * Sets the weights of the architecture.
     * @param architecture the architecture to visit.
     */
    @Override
    public void visit(Architecture architecture) {
        List<Layer> layers = architecture.getLayers();

        int currentIndex = 0;
        int numLayers = layers.size();
        for (int i = 1; i < numLayers; i++) {
            Layer layer = layers.get(i);
            for (Neuron neuron : layer) {
                Vector neuronWeights = neuron.getWeights();
                int size = neuronWeights.size();
                for (int j = 0; j < size; j++) {
                    neuronWeights.set(j, weights.get(currentIndex++));
                }
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean isDone() {
        return false;
    }

    /**
     * Gets the weights to use.
     * @return the weights to use for setting.
     */
    public Vector getWeights() {
        return weights;
    }

    /**
     * Sets the weights.
     * @param weights the new weights to use for setting.
     */
    public void setWeights(Vector weights) {
        this.weights = weights;
    }

}
