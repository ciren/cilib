/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.algorithm.iterator;

import java.util.List;
import java.util.NoSuchElementException;
import net.sourceforge.cilib.algorithm.Algorithm;

/**
 * Iterate through a list of {@link Algorithm}s in a sequential order.
 * @param <E> The {@code Algorithm} type.
 */
public class SequentialAlgorithmIterator<E extends Algorithm> implements AlgorithmIterator<E> {
    private static final long serialVersionUID = 2996277367501679292L;

    protected List<E> algorithms = null;
    protected int index = -1;

    /**
     * Construct a new {@link SequentialAlgorithmIterator}.
     */
    public SequentialAlgorithmIterator() {
        index = -1;
    }

    /**
     * Construct a new {@link SequentialAlgorithmIterator} for the supplied list.
     * @param a the list that will be iterated through sequentially.
     */
    public SequentialAlgorithmIterator(List<E> a) {
        algorithms = a;
        index = -1;
    }

    /**
     * Construct a new {@link SequentialAlgorithmIterator} from the supplied one.
     * @param rhs the {@link SequentialAlgorithmIterator} that should be copied.
     */
    @SuppressWarnings("unchecked")
    public SequentialAlgorithmIterator(SequentialAlgorithmIterator rhs) {
        algorithms = rhs.algorithms;
        index = rhs.index;
    }

    /**
     * Clone this list iterator and return it.
     * @return a clone of this list iterator.
     */
    public SequentialAlgorithmIterator<E> getClone() {
        return new SequentialAlgorithmIterator<E>(this);
    }

    /**
     * Gets the next element in the list. This method may be called repeatedly to iterate through the list, or intermixed with calls to previous to go back and forth. (Note that alternating calls to next and previous will return the same element repeatedly.)
     * @return the next element in the list.
     * @throws NoSuchElementException when the current index is already at the end of the list.
     */
    public E next() {
        if (index + 1 >= algorithms.size())
            throw new NoSuchElementException("Trying to iterate past the end of the list");

        return algorithms.get(++index);
    }

    /**
     * Gets the previous element in the list. This method may be called repeatedly to iterate through the list backwards, or intermixed with calls to next to go back and forth. (Note that alternating calls to next and previous will return the same element repeatedly.)
     * @return the previous element in the list.
     * @throws NoSuchElementException when the current index is already at the beginning of the list.
     */
    public E previous() {
        if (index < 0)
            throw new NoSuchElementException("Trying to iterate past the beginning of the list");

        return algorithms.get(index--);
    }

    /**
     * Returns true if this list iterator has more elements when traversing the list in the forward direction. (In other words, returns true if next would return an element rather than throwing an exception.)
     * @return true if this list iterator has more elements when traversing the list in the forward direction; otherwise it returns false.
     */
    public boolean hasNext() {
        return index < algorithms.size() - 1;
    }

    /**
     * Returns true if this list iterator has more elements when traversing the list in the reverse direction. (In other words, returns true if previous would return an element rather than throwing an exception.)
     * @return true if this list iterator has more elements when traversing the list in the reverse direction; otherwise it returns false.
     */
    public boolean hasPrevious() {
        return index > -1;
    }

    /**
     * Returns the index of the element that would be returned by a subsequent call to next.
     * @return The index of the element that would be returned by a subsequent call to next.
     */
    public int nextIndex() {
        return index + 1;
    }

    /**
     * Returns the index of the element that would be returned by a subsequent call to previous.
     * @return The index of the element that would be returned by a subsequent call to previous.
     */
    public int previousIndex() {
        return index;
    }

    /**
     * Inserts the specified element into the list. The element is inserted immediately before the next element that would be returned by next, if any, and after the next element that would be returned by previous, if any. (If the list contains no elements, the new element becomes the sole element on the list.) The new element is inserted before the implicit cursor: a subsequent call to next would be unaffected, and a subsequent call to previous would return the new element. (This call increases by one the value that would be returned by a call to nextIndex  or previousIndex.)
     * @param algorithm The {@linkplain Algorithm} to add.
     */
    @Override
    public void add(E algorithm) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".add()");
    }

    /**
     * Removes from the list the last element that was returned by next or previous.
     * The index of the current element is also repositioned to prevent the iteration from skipping elements (due to the manner in which the .next() and .previous() methods work).
     * @throws IndexOutOfBoundsException when the current element index is past the beginning or end of the list.
     */
    public void remove() {
        if (index < 0 || index >= algorithms.size())
            throw new IndexOutOfBoundsException("The iterator is not at a valid position");

        // post-decrement to prevent elements from being skipped during iteration
        // and also to keep iterator valid (when removing last element in list)
        algorithms.remove(index--);
    }

    /**
     * Replaces the last element returned by next or previous with the specified element.
     * @param algorithm The element with which to replace the last element returned by next or previous.
     */
    public void set(E algorithm) {
        if (index < 0 || index >= algorithms.size())
            throw new IndexOutOfBoundsException("The iterator is not at a valid position");

        algorithms.set(index, algorithm);
    }

    /**
     * Sets the list that will be iterated through.
     * Sets the current iteration position to -1, i.e. iteration hasn't started yet.
     * @param a The list of {@linkplain Algorithm}s to set.
     */
    public void setAlgorithms(List<E> a) {
        algorithms = a;
        index = -1;
    }

    /**
     * Returns the element at the current index.
     * @return The element at the current index.
     */
    public E current() {
        return algorithms.get(index);
    }
}
