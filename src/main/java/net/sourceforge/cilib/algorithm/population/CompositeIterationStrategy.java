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
package net.sourceforge.cilib.algorithm.population;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.cilib.moo.iterationstrategies.ArchivingIterationStep;

/**
 * <p>
 * Represents a composed {@link IterationStrategy} made up from a collection of other {@code IterationStrategy}
 * classes. This class is usefull if you have generic {@code IterationStrategy} components that you want to
 * reuse in different situations by executing them before or after another {@link IterationStrategy} class
 * (as is the case with the {@link ArchivingIterationStep} that you want to execute after a Multi-objective
 * algorithm's default {@code IterationStrategy} class).
 * </p>
 *
 * @author Wiehann Matthysen
 * @param <E> The {@code PopulationBasedAlgorithm} type.
 */
public class CompositeIterationStrategy<E extends PopulationBasedAlgorithm> implements IterationStrategy<E>, Iterable<IterationStrategy<E>> {

    private static final long serialVersionUID = 1550634359375723218L;
    protected List<IterationStrategy<E>> iterationStrategies;

    public CompositeIterationStrategy() {
        this.iterationStrategies = new ArrayList<IterationStrategy<E>>();
    }

    public CompositeIterationStrategy(CompositeIterationStrategy<E> copy) {
        this.iterationStrategies = new ArrayList<IterationStrategy<E>>();
        for (IterationStrategy<E> iterationStrategy : copy.iterationStrategies) {
            this.iterationStrategies.add(iterationStrategy.getClone());
        }
    }

    @Override
    public CompositeIterationStrategy<E> getClone() {
        return new CompositeIterationStrategy<E>(this);
    }

    public void addIterationStrategy(IterationStrategy<E> iterationStrategy) {
        this.iterationStrategies.add(iterationStrategy);
    }

    public void removeIterationStrategy(IterationStrategy<E> iterationStrategy) {
        this.iterationStrategies.remove(iterationStrategy);
    }

    @Override
    public Iterator<IterationStrategy<E>> iterator() {
        return this.iterationStrategies.iterator();
    }

    @Override
    public void performIteration(E algorithm) {
        for (IterationStrategy<E> iterationStrategy : this) {
            iterationStrategy.performIteration(algorithm);
        }
    }
}
