/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.algorithm;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Stack;

/**
 * Maintain the currently running algorithm stack. The currently executing
 * algorithm will be on the top of the stack.
 */
public class AlgorithmStack {
    private Stack<Algorithm> algorithmStack;

    /**
     * Create a new {@linkplain AlgorithmStack} instance.
     */
    public AlgorithmStack() {
        this.algorithmStack = new Stack<Algorithm>();
    }

    /**
     * Push the current {@linkplain Algorithm} onto the stack.
     * @param algorithm The {@linkplain Algorithm} to push.
     */
    public synchronized void push(Algorithm algorithm) {
        this.algorithmStack.push(algorithm);
    }

    /**
     * Remove the current top of the stack and return it.
     * @return The previous instance that was the top of the stack.
     */
    public synchronized Algorithm pop() {
        return this.algorithmStack.pop();
    }

    /**
     * Get the {@linkplain Algorithm} that is currently at the top of the stack.
     * @return The current top {@linkplain Algorithm}.
     */
    public synchronized Algorithm peek() {
        return this.algorithmStack.peek();
    }

    /**
     * <p>
     * Return an immutable list of {@code Algorithm} instances. The list is the path of currently
     * running algorithms. The returned list will have a size {@literal > 1}, if and only if
     * the algorithm is a composition of other algorithms.
     * </p>
     * <p>
     * It is <b>important to realise that the result of this method is a snapshot in time</b> and
     * will change if the reference is maintained.
     * </p>
     * @return An immutable list of algorithms instances.
     */
    public synchronized List<Algorithm> asList() {
        return ImmutableList.copyOf(this.algorithmStack);
    }
}
