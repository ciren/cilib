/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching.iterators;

import net.sourceforge.cilib.niching.NichingSwarms;
import static net.sourceforge.cilib.niching.NichingSwarms.onFirstSubSwarm;

public class FirstSwarmIterator extends SubswarmIterator {
    @Override
    public NichingSwarms f(NichingSwarms a) {
        return onFirstSubSwarm(iterator).f(a);
    }

    @Override
    public FirstSwarmIterator getClone() {
        FirstSwarmIterator i = new FirstSwarmIterator();
        i.setIterator(iterator);
        return i;
    }
}
