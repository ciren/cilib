/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.algorithm;

import net.sourceforge.cilib.util.Cloneable;

/**
 * Any class can implement this interface to be notified about algorithm events.
 * Classes implementing this interface can be added to the algorithm as an event
 * listener using
 * {@link AbstractAlgorithm#addAlgorithmListener(AlgorithmListener)}.
 */
public interface AlgorithmListener extends Cloneable {
    /**
     * This event is fired just prior to the execution of the main loop of the algorithm.
     * @param e an event containing a reference to the source algorithm.
     */
    void algorithmStarted(AlgorithmEvent e);

    /**
     * This event is fired when the algorithm has completed normally.
     * @param e an event containing a reference to the source algorithm.
     */
    void algorithmFinished(AlgorithmEvent e);

    /**
     * This event is fired after each iteration of the mail loop of the algorithm.
     * @param e an event containing a reference to the source algorithm.
     */
    void iterationCompleted(AlgorithmEvent e);

    /**
     * {@inheritDoc}
     */
    @Override
    AlgorithmListener getClone();
}
