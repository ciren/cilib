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
package net.sourceforge.cilib.algorithm;

import net.sourceforge.cilib.util.Cloneable;

/**
 * Any class can implement this interface to be notified about algorithm events. Classes
 * implementing this interface can be added to the algorithm as an event listener using
 * {@link Algorithm#addAlgorithmListener(AlgorithmListener)}.
 * @author Edwin Peer
 */
public interface AlgorithmListener extends Cloneable {
    /**
     * This event is fired just prior to the execution of the main loop of the algorithm.
     * @param e an event containing a reference to the source algorithm.
     */
    public void algorithmStarted(AlgorithmEvent e);

    /**
     * This event is fired when the algorithm has completed normally.
     * @param e an event containing a reference to the source algorithm.
     */
    public void algorithmFinished(AlgorithmEvent e);

    /**
     * This event is fired when the algorithm is terminated abnormally.
     * @param e an event containing a reference to the source algorithm.
     */
    public void algorithmTerminated(AlgorithmEvent e);

    /**
     * This event is fired after each iteration of the mail loop of the algorithm.
     * @param e an event containing a reference to the source algorithm.
     */
    public void iterationCompleted(AlgorithmEvent e);

    /**
     * {@inheritDoc}
     */
    public AlgorithmListener getClone();
}
