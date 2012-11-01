/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.math.random.generator;

/**
 * This interface provides a drop-in replacement API for the standard
 * java.util.Random classes and API. The reason for _not_ using the standard
 * Java API is due to the API maintaining a large amount of static state.
 * As a result, the classes implementing this interface are all self contained
 * and should be as immutable as possible.
 */
public interface RandomProvider {

    boolean nextBoolean();

    int nextInt();

    int nextInt(int n);

    long nextLong();

    float nextFloat();

    double nextDouble();

    void nextBytes(byte[] bytes);
}
