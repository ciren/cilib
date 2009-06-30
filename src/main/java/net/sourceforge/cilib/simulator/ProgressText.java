/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.simulator;


/**
 * Implements a text progress meter.
 *
 * @author  jkroon
 */
public class ProgressText implements ProgressListener {

    /**
     * Creates new form ProgressFrame.
     *
     * @param simulations The number of simulations in total.
     * */
    public ProgressText(int simulations) {
        this.simulations = simulations;
        printedDone = false;
    }

    public void handleProgressEvent(ProgressEvent event) {
        if(printedDone) {
            return;
        }
        double percentage = (int) (1000 * event.getPercentage()) / 10.0;
        int nequals = (int) (50 * event.getPercentage());
        int i = 0;
        System.out.print("\rProgress (" + percentage + "%) |");
        while(i++ < nequals) {
            System.out.print("=");
        }
        while(i++ < 50) {
            System.out.print(" ");
        }
        System.out.print("|");
        if(nequals == 50) {
            printedDone = true;
            System.out.println(" done.");
        }
        else {
            System.out.flush();
        }
    }

    public void setSimulation(int simulation) {
        System.out.println("Starting simulation " + (simulation + 1) + " of " + simulations + ".");
        printedDone = false;
    }

    private boolean printedDone;
    private int simulations;
}
