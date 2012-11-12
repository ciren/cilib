/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching.iterators;

import net.sourceforge.cilib.niching.NichingSwarms;
import static net.sourceforge.cilib.niching.NichingSwarms.onSubswarms;

public class AllSwarmsIterator extends SubswarmIterator {
    @Override
    public NichingSwarms f(NichingSwarms a) {
        return onSubswarms(iterator).f(a);
    }

    @Override
    public AllSwarmsIterator getClone() {
        AllSwarmsIterator i = new AllSwarmsIterator();
        i.setIterator(iterator);
        return i;
    }
}
