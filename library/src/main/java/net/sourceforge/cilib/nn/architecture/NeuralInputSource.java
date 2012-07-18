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

/**
 * An interface to a neural input source i.e. a source from which a neuron
 * can calculate its activations. This abstraction is necessary for avoiding
 * special cases such as when the feeding source is a pattern, or two layers
 * instead of one.
 */
public interface NeuralInputSource {

    /**
     * Gets the neural input at the specified index.
     * @param index the index of the input to retrieve.
     * @return the neural input at the specified index.
     */
    double getNeuralInput(int index);

    /**
     * Gets the size of the neural input source.
     * @return the size of the neural input source.
     */
    int size();
}
