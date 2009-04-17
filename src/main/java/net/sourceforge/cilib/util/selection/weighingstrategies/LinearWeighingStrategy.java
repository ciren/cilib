/**
 * Copyright (C) 2003 - 2008
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
package net.sourceforge.cilib.util.selection.weighingstrategies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.cilib.container.Pair;

/**
 * @author Wiehann Matthysen
 * @param <T>
 */
public class LinearWeighingStrategy<T> implements WeighingStrategy<Double, T> {

    private static final long serialVersionUID = -3722102703322767623L;
    private double minWeight;
    private double maxWeight;

    public LinearWeighingStrategy(double minWeight, double maxWeight) {
        this.minWeight = minWeight;
        this.maxWeight = maxWeight;
    }

    public LinearWeighingStrategy() {
        this.minWeight = 0.0;
        this.maxWeight = 1.0;
    }

    public LinearWeighingStrategy(LinearWeighingStrategy<T> copy) {
        this.minWeight = copy.minWeight;
        this.maxWeight = copy.maxWeight;
    }

    @Override
    public LinearWeighingStrategy<T> getClone() {
        return new LinearWeighingStrategy<T>(this);
    }

    public void setMinWeight(double minWeight) {
        this.minWeight = minWeight;
    }

    public double getMinWeight() {
        return this.minWeight;
    }

    public void setMaxWeight(double maxWeight) {
        this.maxWeight = maxWeight;
    }

    public double getMaxWeight() {
        return this.maxWeight;
    }

    @Override
    public List<Pair<Double, T>> weigh(Collection<? extends T> objects) {
        List<Pair<Double, T>> weighedObjects = new ArrayList<Pair<Double, T>>();
        double stepSize = (this.maxWeight - this.minWeight) / (objects.size() - 1);
        int objectIndex = 0;
        for (T object : objects) {
            weighedObjects.add(new Pair<Double, T>(objectIndex++ * stepSize + this.minWeight, object));
        }
        return weighedObjects;
    }
}
