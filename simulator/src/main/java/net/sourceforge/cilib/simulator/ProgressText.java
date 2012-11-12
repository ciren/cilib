/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.simulator;

import net.sourceforge.cilib.algorithm.ProgressEvent;
import net.sourceforge.cilib.algorithm.ProgressListener;

/**
 * Implements a text progress meter.
 *
 */
final class ProgressText implements ProgressListener {

    private boolean printedDone;
    private final int simulations;

    /**
     * Creates new form ProgressFrame.
     *
     * @param simulations The number of simulations in total.
     * */
    ProgressText(int simulations) {
        this.simulations = simulations;
        printedDone = false;
    }

    @Override
    public void handleProgressEvent(ProgressEvent event) {
        if (printedDone) {
            return;
        }
        double percentage = (int) (1000 * event.getPercentage()) / 10.0;
        int nequals = (int) (50 * event.getPercentage());
        int i = 0;
        System.out.printf("\rProgress (%3.1f) |", percentage);
        while (i++ < nequals) {
            System.out.print("=");
        }
        while (i++ < 50) {
            System.out.print(" ");
        }
        System.out.print("|");
        if (nequals == 50) {
            printedDone = true;
            System.out.println(" done.");
        } else {
            System.out.flush();
        }
    }

    public void setSimulation(int simulation) {
        System.out.println("Starting simulation " + (simulation + 1) + " of " + simulations + ".");
        printedDone = false;
    }
}
