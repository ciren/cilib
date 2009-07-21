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
package net.sourceforge.cilib.util.selection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.sourceforge.cilib.math.random.generator.Random;
import net.sourceforge.cilib.util.selection.Selection.Entry;
import net.sourceforge.cilib.util.selection.ordering.Ordering;
import net.sourceforge.cilib.util.selection.weighing.Weighing;

/**
 * <p>
 * A {@code UniqueSelection} is an abstraction that allows operations to be applied to
 * a collection instace that result in a selection of list elements, based on a varied of
 * potential combination of operators. {@code UniqueSelection} is similar to {@code Selection},
 * except that the {#code random(int)} method will return a list of unique random entries.
 * </p>
 * @param <E> The selection type.
 * @author gpampara
 * @author leo
 */
public class UniqueSelection<E> implements SelectionSyntax<E>, RandomSyntax<E> {
    private List<Entry<E>> elements;

    /**
     * Assign the UniqueSelection to take palce on the provided collection. The
     * collection is copied to ensure that the original collection reference is
     * not altered.
     * @param elements The elements on which the selection should take place.
     */
    private UniqueSelection(Collection<? extends E> elements) {
        this.elements = new ArrayList<Entry<E>>(elements.size());

        for (E element : elements) {
            this.elements.add(new Entry<E>(element));
        }
    }

    /**
     * Create a selection that will operate on the provided collection.
     * This method is intentionally package private as we don't want manual creation
     * of UniqueSelection<T> instances. All access must be through the Selection<T>
     * interface.
     * @param <T> The comparable type.
     * @param elements The collection of elements to operate on.
     * @return A UniqueSelection based on the provided collection.
     */
    static <T> UniqueSelection<T> from(List<? extends T> elements) {
        return new UniqueSelection<T>(elements);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueSelection<E> orderBy(Ordering<E> ordering) {
        boolean result = ordering.order(this.elements);

        if (result) {
            return this;
        }

        throw new UnsupportedOperationException("The ordering [" + ordering.getClass().getSimpleName() + "] " +
                "cannot be applied to the selection. Please ensure that the intention of the ordering is correct.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueSelection<E> weigh(Weighing<E> weighing) {
        boolean result = weighing.weigh(this.elements);

        if (result) {
            return this;
        }

        throw new UnsupportedOperationException("The weighing [" + weighing.getClass().getSimpleName() + "]" +
                "cannot be applied to the selection. Please ensure that the intention of the weighing is correct.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueSelection<E> first() {
        this.elements = this.elements.subList(0, 1);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueSelection<E> first(int number) {
        this.elements = this.elements.subList(0, number);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueSelection<E> last() {
        this.elements = this.elements.subList(this.elements.size() - 1, this.elements.size());
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueSelection<E> last(int number) {
        this.elements = this.elements.subList(this.elements.size() - number, this.elements.size());
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<E> select() {
        List<E> result = new ArrayList<E>();

        for (Entry<E> entry : elements) {
            result.add(entry.getElement());
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E singleSelect() {
        return this.elements.get(0).getElement();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Selection.Entry<E>> entries() {
        return this.elements;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueSelection<E> exclude(E... exclusions) {
        List<E> exclusionList = Arrays.asList(exclusions);
        return this.exclude(exclusionList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueSelection<E> exclude(Iterable<E> exclusions) {
        List<Entry<E>> tmp = new ArrayList<Entry<E>>();

        for (E e : exclusions) {
            for (Entry<E> entry : this.elements)
                if (entry.getElement().equals(e))
                    tmp.add(entry);
        }

        this.elements.removeAll(tmp);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueSelection<E> random(Random random) {
        Entry<E> randomEntry = Selection.randomFrom(elements, random);
        elements.clear();
        elements.add(randomEntry);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueSelection<E> random(Random random, int number) {
        if(number > elements.size())
            throw new RuntimeException("Unable to select " + number + " unique elements, current Selection only contains " + elements.size() + " elements.");
        List<Entry<E>> tmp = new ArrayList<Entry<E>>(number);

        for (int i = 0; i < number; i++) {
            int index = random.nextInt(elements.size());
            tmp.add(elements.get(index));
            elements.remove(index);
        }
        elements = tmp;
        return this;
    }

}
