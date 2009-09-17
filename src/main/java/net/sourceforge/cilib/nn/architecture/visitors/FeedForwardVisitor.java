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
import net.sourceforge.cilib.nn.architecture.ForwardingLayer;
import net.sourceforge.cilib.nn.architecture.Layer;
import net.sourceforge.cilib.nn.components.PatternInputSource;

/**
 * Class implements an {@link ArchitectureOperationVisitor} that performs a feed-
 * forward through a neural network architechture as the visit operation.
 * @author andrich
 */
public class FeedForwardVisitor extends ArchitectureOperationVisitor {


    /**
     * Perform a feed-forward using {@link #input} as the input for the FF and
     * storing the output in {@link #output}.
     * @param architecture the architechture to visit.
     */
    @Override
    public void visit(Architecture architecture) {
        List<Layer> layers = architecture.getLayers();
        int size = layers.size();

        ((ForwardingLayer) layers.get(0)).setSource(new PatternInputSource(input));
        Layer currentLayer = null;
        for (int l = 1; l < size; l++) {
            currentLayer = layers.get(l);
            int layerSize = currentLayer.size();
            for (int n = 0; n < layerSize; n++) {
                currentLayer.get(n).calculateActivation(layers.get(l - 1));
            }
        }

        this.output = currentLayer.getActivations();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean isDone() {
        return false;
    }
}
