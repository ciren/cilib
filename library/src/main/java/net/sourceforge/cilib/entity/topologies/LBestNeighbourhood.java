/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.topologies;

import com.google.common.collect.Lists;

import fj.data.List;
import fj.data.Stream;

public class LBestNeighbourhood<E> extends Neighbourhood<E> {

    private int n;

    public LBestNeighbourhood() {
        this.n = 3;
    }

    public LBestNeighbourhood(int n) {
        this.n = n;
    }


    @Override
    public List<E> f(List<E> list, E element) {
        java.util.List<E> inner = Lists.newArrayList(list);
        int ts = inner.size();
        int x = (inner.indexOf(element) - (n / 2) + ts) % ts;

        return Stream.cycle(list.toStream()).drop(x).take(n).toList();
    }

    public void setNeighbourhoodSize(int n) {
        this.n = n;
    }

}
