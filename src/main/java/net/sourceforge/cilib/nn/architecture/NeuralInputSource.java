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

package net.sourceforge.cilib.nn.architecture;

/**
 * An interface to a neural input source i.e. a source from which a neuron
 * can calculate its activations. This abstraction is necessary for avoiding
 * special cases such as when the feeding source is a pattern, or two layers
 * instead of one.
 * @author andrich
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
