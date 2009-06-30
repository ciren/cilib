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
 * This interface must be implemented by any class that wishes to receive progress
 * events.
 *
 * @author  Edwin Peer
 */
public interface ProgressListener {
    /**
     * Process the given progress event.
     *
     * @param event The progress event.
     */
    public void handleProgressEvent(ProgressEvent event);

    /**
     * Indicates on which event we are.
     *
     * TODO:  This needs to become a ProgressEvent, same as the indication
     * of percentage completed.
     *
     * @param simnum The number of the simulation.
     */
    public void setSimulation(int simnum);
}
