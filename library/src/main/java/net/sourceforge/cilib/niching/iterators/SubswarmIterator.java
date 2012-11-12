/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching.iterators;

import net.sourceforge.cilib.niching.NichingFunctions;
import net.sourceforge.cilib.util.Cloneable;

public abstract class SubswarmIterator extends NichingFunctions.NichingFunction implements Cloneable {

    protected NicheIteration iterator;

    public SubswarmIterator() {
        this.iterator = new CompleteNicheIteration();
    }

    public void setIterator(NicheIteration iterator) {
        this.iterator = iterator;
    }

    public NicheIteration getIterator() {
        return iterator;
    }

    @Override
    public abstract SubswarmIterator getClone();
}
