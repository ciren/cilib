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
package net.sourceforge.cilib.neuralnetwork.generic.datacontainers;

import java.util.ArrayList;

import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkDataIterator;

/**
 * @author stefanv
 *
 */
public class LinearDataIterator implements NeuralNetworkDataIterator {
    private static final long serialVersionUID = 104673955761529700L;

    private int count;
    private ArrayList<NNPattern> list = null;

    /**
     * Create an instance, with the provided list.
     * @param list The list of data patterns.
     */
    public LinearDataIterator(ArrayList<NNPattern> list) {
        this.list = list;
        this.count = 0;
    }

    /**
     * {@inheritDoc}
     */
    public void next() {
        count++;
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasMore() {
        return (count < list.size());
    }

    /**
     * {@inheritDoc}
     */
    public void reset() {
        count = 0;
    }

    /**
     * {@inheritDoc}
     */
    public NNPattern value() {
        return list.get(count);
    }

    /**
     * {@inheritDoc}
     */
    public int size() {
        return list.size();
    }

    /**
     * {@inheritDoc}
     */
    public int currentPos() {
        return count;
    }

    /**
     * {@inheritDoc}
     */
    public NeuralNetworkDataIterator getClone(){
        LinearDataIterator tmp = new LinearDataIterator(this.list);
        tmp.count = this.count;
        return tmp;
    }

}
