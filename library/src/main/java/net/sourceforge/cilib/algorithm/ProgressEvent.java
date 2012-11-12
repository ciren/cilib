/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.algorithm;

/**
 * A progress event informs the listening party about the percentage complete of the simulation.
 *
 */
public class ProgressEvent {

    /**
     * Creates a new instance of ProgressEvent with a given completion percentage.
     * @param percentage The current percentage value.
     */
    public ProgressEvent(double percentage) {
        this.percentage = percentage;
    }

    /**
     * Accessor for the percentage completed.
     * @return The percentage
     */
    public double getPercentage() {
        return percentage;
    }

    private double percentage;
}
