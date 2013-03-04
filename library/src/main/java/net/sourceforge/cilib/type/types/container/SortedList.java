/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type.types.container;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import net.sourceforge.cilib.util.Cloneable;

/**
 * A collection that always provides a list of elements that is sorted. The ordering is
 * specified by the provided {@linkplain Comparator} instance. If no comparator is
 * provided, the natural ordering of the type {@code E} will be used.
 *
 * @param <E> The {@linkplain Comparable} type.
 */
public class SortedList<E> implements List<E>, Cloneable {

    private static final long serialVersionUID = 4170822549076470223L;
    private LinkedList<E> list;
    private Comparator<E> comparator = null;

    /**
     * Create a new instance of {@linkplain SortedList} without a {@linkplain Comparator}
     * defined.
     */
    public SortedList() {
        this.list = new LinkedList<E>();
    }

    /**
     * Create a new instance of {@linkplain SortedList} with the provided {@linkplain Comparator}.
     * @param comparator The {@linkplain Comparator} to use.
     */
    public SortedList(Comparator<E> comparator) {
        this.list = new LinkedList<E>();
        this.comparator = comparator;
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public SortedList(SortedList<E> copy) {
        this.list = new LinkedList<E>();
        this.comparator = copy.comparator;
        Collections.copy(this.list, copy.list);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SortedList<E> getClone() {
        return new SortedList<E>(this);
    }

    /**
     * Determine the size of the current list.
     * @return The current size of the list.
     */
    @Override
    public int size() {
        return this.list.size();
    }

    /**
     * Returns {@code true} if this list contains no elements.
     * @return {@code true} if this list contains no elements.
     */
    @Override
    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    /**
     * Returns {@code true} if this list contains the specified element. More formally,
     * returns true if and only if this list contains at least one element {@code e}
     * such that {@code (o==null ? e==null : o.equals(e))}.
     * @param o element whose presence is to be tested.
     * @return {@code true} if this list contains the specified element.
     */
    @Override
    public boolean contains(Object o) {
        return this.list.contains(o);
    }

    /**
     * Returns an iterator over this sequence in proper order.
     * @return an iterator over the elements in this list.
     */
    @Override
    public Iterator<E> iterator() {
        return this.list.iterator();
    }

    /**
     * Return a new array containing the elements of this list.
     * @return a new array containing the list elements.
     */
    @Override
    public Object[] toArray() {
        return this.list.toArray();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T[] toArray(T[] a) {
        return this.list.toArray(a);
    }

    /**
     * Add the provided element to the list. The position of the element is
     * determined by the ordering as defined by the associated {@link Comparator}.
     * The insertion is determined by first performing a binary search to
     * determine the location of the insert and then, finally adding the element.
     * @param e The object to add to the list.
     * @return {@code true} if the addition was successful.
     */
    @Override
    public boolean add(E e) {
        int index = Collections.binarySearch(this.list, e, comparator);

        if (index < 0)
            this.list.add(-index-1, e);
        else
            this.list.add(index, e);

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(Object o) {
        return this.list.remove(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        return this.list.containsAll(c);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E element : c)
            add(element);

        return true;
    }

    /**
     * Insert the provided collection to the list at {@code index}. This operation
     * is not guaranteed and to ensure that the list remains sorted, the list
     * is tested after addition and reordered, if needed.
     * @param index The index to attempt the addition.
     * @param c The collection to add.
     * @return {@code true} if the operation was successful.
     */
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        boolean result = this.list.addAll(index, c);
        Collections.sort(list, comparator);

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        return this.list.removeAll(c);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        return this.list.retainAll(c);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        this.list.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E get(int index) {
        return this.list.get(index);
    }

    /**
     * Replace the current element located at {@code index} with {@code element}.
     * Even though the element is replaced, there is no guarantee that the order
     * or location of the new element will be at {@code index}. After the replacement
     * the order of the list is shuffled to ensure that the sorted nature of the
     * list is maintained.
     *
     * @param index The current location of the element to replace.
     * @param element The element to replace the element at index {@code index}.
     * @return The new element that has been set.
     */
    @Override
    public E set(int index, E element) {
        E result = this.list.set(index, element);
        Collections.sort(this.list, comparator);
        return result;
    }

    /**
     * Add {@code element} to the list at index {@code index}. This action is a
     * best effort. There is no guarantee that the addition of the element at
     * {@code index} will preserve the list's ordering. As a result the list
     * order is verified after addition.
     *
     * @param index The position in the list to attempt the addition.
     * @param element The element to add.
     */
    @Override
    public void add(int index, E element) {
        this.list.add(index, element);
        Collections.sort(this.list, comparator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E remove(int index) {
        return this.list.remove(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int indexOf(Object o) {
        return this.list.indexOf(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int lastIndexOf(Object o) {
        return this.list.lastIndexOf(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListIterator<E> listIterator() {
        return this.list.listIterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListIterator<E> listIterator(int index) {
        return this.list.listIterator(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return this.list.subList(fromIndex, toIndex);
    }

    /**
     * Get the current {@linkplain Comparator} instance.
     * @return Returns the comparator.
     */
    public Comparator<E> getComparator() {
        return comparator;
    }

    /**
     * Set the {@linkplain Comparator} to use.
     * @param comparator The comparator to set.
     */
    public void setComparator(Comparator<E> comparator) {
        this.comparator = comparator;
    }

}
