/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions;

/**
 * Function definition. All functions apply some or other transformation
 * on a set of input variables and create an output that is representative of
 * the input.
 * @param <F> The "from" type.
 * @param <T> The "to" type.
 */
public interface Function<F, T> {

    /**
     * Perform the evaluation of the input and return the result.
     * @param input The input for the function.
     * @return The result of the evaluation.
     */
    T apply(F input);
}
