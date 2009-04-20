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
import java.util.List;
import java.util.Random;

import net.sourceforge.cilib.math.random.generator.MersenneTwister;

/**
 * @author Wiehann Matthysen
 * @param <T>
 */
public class RandomSelectionStrategy<T> extends SelectionStrategy<T> {

    private static final long serialVersionUID = -4820961568978839224L;
    private Random randomiser;

    public RandomSelectionStrategy(Random randomiser) {
        this.randomiser = randomiser;
    }

    public RandomSelectionStrategy() {
        this(new MersenneTwister());
    }

    public RandomSelectionStrategy(RandomSelectionStrategy<T> copy) {
        this.randomiser = new MersenneTwister();
    }

    @Override
    public RandomSelectionStrategy<T> getClone() {
        return new RandomSelectionStrategy<T>(this);
    }

    @Override
    public Collection<T> select(Collection<? extends T> objects, int numberOfObjects) {
        List<T> tempObjects = new ArrayList<T>(objects.size());
        tempObjects.addAll(objects);
        List<T> selectedObjects = new ArrayList<T>(numberOfObjects);
        while (selectedObjects.size() < numberOfObjects) {
            int randomIndex = this.randomiser.nextInt(tempObjects.size());
            selectedObjects.add(tempObjects.remove(randomIndex));
        }
        return selectedObjects;
    }
}
