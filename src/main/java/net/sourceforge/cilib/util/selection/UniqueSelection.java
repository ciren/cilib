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

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.sourceforge.cilib.math.random.generator.Random;
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
    private List<SelectionSyntax.Entry<E>> elements;

    /**
     * Assign the UniqueSelection to take palce on the provided collection. The
     * collection is copied to ensure that the original collection reference is
     * not altered.
     * @param elements The elements on which the selection should take place.
     */
     UniqueSelection(Collection<? extends E> elements) {
        this.elements = Lists.newArrayListWithCapacity(elements.size());

        for (E element : elements) {
            this.elements.add(new Entry<E>(element));
        }
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
        return Lists.transform(elements, new Function<SelectionSyntax.Entry<E>, E>() {
            @Override
            public E apply(SelectionSyntax.Entry<E> from) {
                return from.getElement();
            }
        });
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
    public List<SelectionSyntax.Entry<E>> entries() {
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
        List<SelectionSyntax.Entry<E>> tmp = Lists.newArrayList();

        for (E e : exclusions) {
            for (SelectionSyntax.Entry<E> entry : this.elements)
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
    public UniqueSelection<E> satisfies(Predicate<? super E> predicate) {
        List<SelectionSyntax.Entry<E>> tmp = Lists.newArrayList();

        for (SelectionSyntax.Entry<E> entry : this.elements) {
            if (!predicate.apply(entry.getElement())) {
                tmp.add(entry);
            }
        }

        this.elements.removeAll(tmp);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueSelection<E> random(Random random) {
        SelectionSyntax.Entry<E> randomEntry = Selection.randomFrom(elements, random);
        elements.clear();
        elements.add(randomEntry);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueSelection<E> random(Random random, int number) {
        Preconditions.checkArgument(number <= elements.size(), "Unable to select " + number + " unique elements, current Selection only contains " + elements.size() + " elements.");
        List<SelectionSyntax.Entry<E>> tmp = Lists.newArrayListWithCapacity(number);

        for (int i = 0; i < number; i++) {
            int index = random.nextInt(elements.size());
            tmp.add(elements.get(index));
            elements.remove(index); // Remove the selected entry fromt he original list.
        }
        elements = tmp;
        return this;
    }

    static class Entry<E> implements SelectionSyntax.Entry<E> {
        private final E element;
        private double weight;

        /**
         * Create a new {@code Entry}. This constructor is private intentionall
         * @param element The element to decorate.
         */
        Entry(E element) {
            this.element = element;
            this.weight = 0.0;
        }

        /**
         * Get the {@code element} that this {@code Entry} represents.
         * @return The decorated {@code element}.
         */
        @Override
        public E getElement() {
            return element;
        }

        /**
         * Obtain the weight value associated with this {@code Entry}.
         * <p>
         * The weight value need not be set. It is not always used.
         *
         * @return The {@code weight} value.
         */
        @Override
        public double getWeight() {
            return this.weight;
        }

        /**
         * Set the {@code weight} value for the current {@code Entry}
         * within the {@code Selection}.
         * @param weight The {@code weight} value to set.
         */
        @Override
        public void setWeight(double weight) {
            this.weight = weight;
        }

        /**
         * Determine if the provided {@code obj} is equal to the currently
         * decorated element within this {@code Entry}.
         * @param obj The object instance to compare.
         * @return {@code true} if the objects are equal, {@code false} otherwi
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }

            if ((obj == null) || (this.getClass() != obj.getClass())) {
                return false;
            }

            Entry<E> other = (Entry<E>) obj;
            return this.element.equals(other.element);
        }

        /**
         * Obtain the hash of the decorated {@code element}.
         * @return The decorated instance's hash value.
         */
        @Override
        public int hashCode() {
            return element.hashCode();
        }

        /**
         * Obtain the {@code String} of the decorated {@code element}.
         * @return The {@code toString()} of the decorated element.
         */
        @Override
        public String toString() {
            return this.element.toString() + ":" + this.weight;
        }
    }

}
