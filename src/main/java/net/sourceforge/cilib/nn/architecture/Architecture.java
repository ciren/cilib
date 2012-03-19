/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.nn.architecture;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.nn.architecture.builder.ArchitectureBuilder;
import net.sourceforge.cilib.nn.architecture.builder.FeedForwardArchitectureBuilder;
import net.sourceforge.cilib.nn.architecture.visitors.ArchitectureVisitor;

/**
 * Class represents a neural network architecture and encapsulates a {@link ArchitectureBuilder}
 * and a list of {@link Layers}.
 */
public class Architecture {

    private ArchitectureBuilder architectureBuilder;
    private List<Layer> layers;

    /**
     * Default constructor. Default ArchitectureBuilder is a {@link FeedForwardArchitectureBuilder}
     */
    public Architecture() {
        layers = new ArrayList<Layer>();
        architectureBuilder = new FeedForwardArchitectureBuilder();
    }

    /**
     * Initializes the architecture by calling the builder's build method on
     * 'this' object.
     */
    public void initialize() {
        architectureBuilder.buildArchitecture(this);
    }

    /**
     * Accepts an {@link ArchitectureVisitor} and calls its visit method on
     * 'this' object.
     * @param visitor
     */
    public void accept(ArchitectureVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Gets the architecture builder.
     * @return the architecture builder.
     */
    public ArchitectureBuilder getArchitectureBuilder() {
        return architectureBuilder;
    }

    /**
     * Sets the architecture builder.
     * @param architectureBuilder the new architecture builder.
     */
    public void setArchitectureBuilder(ArchitectureBuilder architectureBuilder) {
        this.architectureBuilder = architectureBuilder;
    }

    /**
     * Gets the number of layers the architecture contains.
     * @return the number of layers in the architecture.
     */
    public int getNumLayers() {
        return layers.size();
    }

    /**
     * Gets the list containing the layers.
     * @return list containing the layers.
     */
    public List<Layer> getLayers() {
        return layers;
    }

    /**
     * Sets the list containing the layers.
     * @param layers the new list containing the layers.
     */
    public void setLayers(List<Layer> layers) {
        this.layers = layers;
    }
}
