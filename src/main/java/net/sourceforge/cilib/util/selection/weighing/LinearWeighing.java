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
 * Apply a weighting to the provided list of elements. This weighting will be
 * linear with the bounds of the range set to [0,1] in the default case.
 * @param <E> The selection type.
 * @author Wiehann Matthysen
 */
public class LinearWeighing<E> implements Weighing<E> {
    private static final long serialVersionUID = 3294682425241945584L;

    private double min;
    private double max;

    /**
     * Create a new instance with the bounds of [0,1].
     */
    public LinearWeighing() {
        this(0.0, 1.0);
    }

    /**
     * Create a new instance with the linear bounds of {@code [min, max]}.
     * @param min The lower bound.
     * @param max The upper bound.
     */
    public LinearWeighing(double min, double max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public LinearWeighing(LinearWeighing<E> copy) {
        this.min = copy.min;
        this.max = copy.max;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LinearWeighing<E> getClone() {
        return new LinearWeighing<E>(this);
    }

    /**
     * Set the minimum bound value.
     * @param min The value to set.
     */
    public void setMin(double min) {
        this.min = min;
    }

    /**
     * Get the minimum bound value.
     * @return The minimum bound value.
     */
    public double getMin() {
        return this.min;
    }

    /**
     * Set the maximum bound value.
     * @param max The value to set.
     */
    public void setMax(double max) {
        this.max = max;
    }

    /**
     * Get the maximum bound value.
     * @return The maximum bound value.
     */
    public double getMax() {
        return this.max;
    }

    /**
     * {@inheritDoc}
     * Apply a linear weighing that has an equal step size from {@code [min, max]}.
     */
    @Override
    public boolean weigh(List<Selection.Entry<E>> elements) {
        double stepSize = (this.max - this.min) / (elements.size() - 1);
        int objectIndex = 0;
        for (Selection.Entry<E> element : elements) {
            element.setWeight(objectIndex++ * stepSize + this.min);
        }
        return true;
    }
}
