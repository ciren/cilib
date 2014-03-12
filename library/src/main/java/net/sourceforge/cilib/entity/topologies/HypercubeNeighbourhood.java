/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.topologies;

import fj.Equal;
import fj.F;
import fj.data.List;

public class HypercubeNeighbourhood<E> extends Neighbourhood<E> {
    private int n = 5;

    @Override
    public List<E> f(final List<E> list, final E current) {
        final int index = list.elementIndex(Equal.<E>anyEqual(), current).orSome(-1);
        return List.range(0, n).map(new F<Integer, E>() {
            @Override
            public E f(Integer a) {
                return list.index(index ^ Double.valueOf(Math.pow(2, a)).intValue());
            }
        });
    }

    public void setNeighbourhoodSize(int n) {
        this.n = n;
    }
}
