/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.algorithm;

/**
 * This interface must be implemented by any class that wishes to receive progress
 * events.
 *
 */
public interface ProgressListener {

    /**
     * Process the given progress event.
     *
     * @param event The progress event.
     */
    void handleProgressEvent(ProgressEvent event);

}
