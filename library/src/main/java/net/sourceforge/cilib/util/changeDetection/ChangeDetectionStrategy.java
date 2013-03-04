/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */

/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */

package net.sourceforge.cilib.util.changeDetection;

public abstract class ChangeDetectionStrategy {
    /*
     * Clone method of the ChangeDetectionStrategy
     * @return A new instance of the ChangeDetectionStrategy
     */
    public abstract ChangeDetectionStrategy getClone();

    /*
     * Checks if a change has occurred
     * @return True if a change has taken place, false if it has not
     */
    public abstract boolean detectChange();
}
