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
package net.sourceforge.cilib.util.selection.weighing;

import java.util.List;
import net.sourceforge.cilib.util.selection.Selection;

/**
 * Apply a fixed weighing to a list of elements.
 * @param <E> The selection type.
 * @author Wiehann Matthysen
 */
public class FixedWeighing<E> implements Weighing<E> {
    private static final long serialVersionUID = -6990220691744842964L;

    private double weight;

    /**
     * Create a new instance with the default weighing value of {@code 0.0}.
     */
    public FixedWeighing() {
        this(0.0);
    }

    /**
     * Create a new instance with the provided value for weighing.
     * @param weight The weighing value to use.
     */
    public FixedWeighing(double weight) {
        this.weight = weight;
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public FixedWeighing(FixedWeighing<E> copy) {
        this.weight = copy.weight;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FixedWeighing<E> getClone() {
        return new FixedWeighing<E>(this);
    }

    /**
     * Set the weight value.
     * @param weight The value to set.
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * Get the weight value.
     * @return
     */
    public double getWeight() {
        return this.weight;
    }

    /**
     * {@inheritDoc}
     * Apply the defined weight value to the all the entries contain in {@code elements}.
     */
    @Override
    public boolean weigh(List<Selection.Entry<E>> elements) {
        for (Selection.Entry<E> object : elements) {
            object.setWeight(this.weight);
        }

        return true;
    }
}
