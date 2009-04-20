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
package net.sourceforge.cilib.util.selection.selectionstrategies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.sourceforge.cilib.container.Pair;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;

/**
 * @author Wiehann Matthysen
 * @param <T>
 */
public class ProbabilisticSelectionStrategy<T> extends WeighedSelectionStrategy<Double, T> {

    private static final long serialVersionUID = -7118485089082044172L;
    private Random randomiser;

    public ProbabilisticSelectionStrategy(Random randomiser) {
        this.randomiser = randomiser;
    }

    public ProbabilisticSelectionStrategy() {
        this(new MersenneTwister());
    }

    public ProbabilisticSelectionStrategy(ProbabilisticSelectionStrategy<T> copy) {
        super(copy);
        this.randomiser = new MersenneTwister();
    }

    @Override
    public ProbabilisticSelectionStrategy<T> getClone() {
        return new ProbabilisticSelectionStrategy<T>(this);
    }

    @Override
    public Collection<T> select(List<Pair<Double, T>> weighedObjects, int numberOfObjects) {
        double totalSum = 0.0;
        for (Pair<Double, T> weighedObject : weighedObjects) {
            totalSum += weighedObject.getKey();
        }

        if (Double.compare(totalSum, 0.0) == 0) {
            RandomSelectionStrategy<T> randomSelectionStrategy = new RandomSelectionStrategy<T>();
            List<T> tempObjects = new ArrayList<T>();
            for (Pair<Double, T> weighedObject : weighedObjects) {
                tempObjects.add(weighedObject.getValue());
            }
            return randomSelectionStrategy.select(tempObjects, numberOfObjects);
        }

        List<Pair<Double, T>> tempObjects = new ArrayList<Pair<Double, T>>(weighedObjects.size());
        tempObjects.addAll(weighedObjects);
        Collections.shuffle(tempObjects, this.randomiser);

        List<T> selectedObjects = new ArrayList<T>(numberOfObjects);
        while (selectedObjects.size() < numberOfObjects && tempObjects.size() > 0) {
            double randomValue = this.randomiser.nextDouble() * totalSum;
            Pair<Double, T> selectedObject = null;
            Iterator<Pair<Double, T>> objectIterator = tempObjects.iterator();
            double weightSum = 0.0;
            while (weightSum < randomValue && objectIterator.hasNext()) {
                selectedObject = objectIterator.next();
                double weight = selectedObject.getKey();
                weightSum += weight;
            }
            selectedObjects.add(selectedObject.getValue());
            totalSum -= selectedObject.getKey();
            objectIterator.remove();
        }

        return selectedObjects;
    }
}
