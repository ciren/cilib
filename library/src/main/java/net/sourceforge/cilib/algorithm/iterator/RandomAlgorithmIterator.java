/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.algorithm.iterator;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;

/**
 * Iterate through a list of {@link Algorithm}s in a random order. With this class it is possible
 * to move forwards and backwards in an iteration even though the order is random. To achieve this
 * a list of random indices is generated and the indices are used as the iteration order. The
 * iteration order will be different for every {@link RandomAlgorithmIterator} object.
 *
 * @param <E> The {@linkplain Algorithm} type.
 */
public class RandomAlgorithmIterator<E extends Algorithm> extends SequentialAlgorithmIterator<E> {

    private static final long serialVersionUID = 9087345802965469395L;
    private List<Integer> randomNumbers = null;

    /**
     * Construct a new {@link RandomAlgorithmIterator}.
     */
    public RandomAlgorithmIterator() {
        super();
    }

    /**
     * Construct a new {@link RandomAlgorithmIterator} for the supplied list. Generates the random
     * sequence of indices.
     * @param a the list that will be iterated through randomly.
     */
    public RandomAlgorithmIterator(List<E> a) {
        super(a);
        randomNumbers = generateRandomSequence(new UniformDistribution());
    }

    /**
     * Construct a new {@link RandomAlgorithmIterator} from the supplied one. This list iterator will
     * use the same random sequence of indices from the supplied {@link RandomAlgorithmIterator}.
     * @param rhs the {@link RandomAlgorithmIterator} that should be copied.
     */
    @SuppressWarnings("unchecked")
    public RandomAlgorithmIterator(RandomAlgorithmIterator rhs) {
        super(rhs);
        randomNumbers = generateRandomSequence(new UniformDistribution());
    }

    /**
     * Clone this list iterator and return it.
     * @return a clone of this list iterator.
     */
    @Override
    public RandomAlgorithmIterator<E> getClone() {
        return new RandomAlgorithmIterator<E>(this);
    }

    /**
     * Generate the random sequence of indices and store them in the randomNumbers list. Each index
     * is used only once because each element should be iterated only once. Using a list to store the
     * random indices makes it possible to move forwards and backwards during this iteration, as
     * opposed to generating a random index on the fly.
     * @throws IllegalStateException when the number of elements does not correspond with the number
     *         of random indices.
     */
    private List<Integer> generateRandomSequence(ProbabilityDistributionFunction distribution) {
        List<Integer> list = new ArrayList<Integer>();
        Integer random = null;

        for (int i = 0; i < algorithms.size(); i++) {
            // make sure each index is used only once
            do {
                random = Integer.valueOf((int) distribution.getRandomNumber(0, algorithms.size()));
            } while (list.contains(random));

            list.add(random);
        }

        if (list.size() != algorithms.size()) {
            throw new IllegalStateException("The number of algorithms does not correspond with the number of random indices");
        }

        return list;
    }

    /**
     * Gets the next element in the list. This method may be called repeatedly to iterate through the
     * list, or intermixed with calls to previous to go back and forth. (Note that alternating calls
     * to next and previous will return the same element repeatedly.)
     * @return the next element in the list.
     * @throws {@link NoSuchElementException} when the current index is already at the end of the
     *         list.
     */
    @Override
    public E next() {
        if (index + 1 >= randomNumbers.size()) {
            throw new NoSuchElementException("Trying to iterate past the end of the list");
        }

        return algorithms.get(randomNumbers.get(++index));
    }

    /**
     * Gets the previous element in the list. This method may be called repeatedly to iterate through
     * the list backwards, or intermixed with calls to next to go back and forth. (Note that
     * alternating calls to next and previous will return the same element repeatedly.)
     * @return the previous element in the list.
     * @throws {@link NoSuchElementException} when the current index is already at the beginning of
     *         the list.
     */
    @Override
    public E previous() {
        if (index < 0) {
            throw new NoSuchElementException("Trying to iterate past the beginning of the list");
        }

        return algorithms.get(randomNumbers.get(index--));
    }

    /**
     * Inserts the specified element into the list. The element is inserted immediately before the
     * next element that would be returned by next, if any, and after the next element that would be
     * returned by previous, if any. (If the list contains no elements, the new element becomes the
     * sole element on the list.) The new element is inserted before the implicit cursor: a
     * subsequent call to next would be unaffected, and a subsequent call to previous would return
     * the new element. (This call increases by one the value that would be returned by a call to
     * nextIndex or previousIndex.) WARNING: Using the add method too much during an iteration causes
     * randomness to disappear.
     */
    @Override
    public void add(E algorithm) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".add()");
    }

    /**
     * Removes from the list the last element that was returned by next or previous. The index of the
     * current element is also repositioned to prevent the iteration from skipping elements (due to
     * the manner in which the .next() and .previous() methods work).
     * @throws {@link IndexOutOfBoundsException} when the current element index is past the beginning
     *         or end of the list.
     */
    @Override
    public void remove() {
        if (index < 0 || index >= randomNumbers.size()) {
            throw new IndexOutOfBoundsException("The iterator is not at a valid position");
        }

        // post-decrement to prevent elements from being skipped during iteration
        // and also to keep iterator valid (when removing last element in list)
        algorithms.remove(randomNumbers.get(index));
        randomNumbers.remove(index--);
    }

    /**
     * Replaces the last element returned by next or previous with the specified element.
     * @param algorithm The element with which to replace the last element returned by next or
     *        previous.
     */
    @Override
    public void set(E algorithm) {
        algorithms.set(randomNumbers.get(index), algorithm);
    }

    /**
     * Sets the list that will be iterated through. Sets the current iteration position to -1, i.e.
     * iteration hasn't started yet. Generates the random sequence of indices.
     */
    @Override
    public void setAlgorithms(List<E> a) {
        algorithms = a;
        index = -1;
        randomNumbers = generateRandomSequence(new UniformDistribution());
    }

    /**
     * Returns the element at the current index.
     * @return The element at the current index.
     */
    @Override
    public E current() {
        return algorithms.get(randomNumbers.get(index));
    }
}
