/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.selection.recipes;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.comparator.BoltzmannComparator;

public class BoltzmannSelector<E extends Entity> extends ElitistSelector<E> {
    
    public BoltzmannSelector() {
        this.comparator = new BoltzmannComparator();
    }

}
