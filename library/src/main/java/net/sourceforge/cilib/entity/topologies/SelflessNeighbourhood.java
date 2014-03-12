/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.topologies;

import fj.Equal;

import fj.data.List;

public class SelflessNeighbourhood<E> extends Neighbourhood<E> {

    private Neighbourhood<E> delegate;

    public SelflessNeighbourhood() {
        this.delegate = new LBestNeighbourhood<>();
    }

    @Override
    public List<E> f(List<E> list, E element) {
        return delegate.f(list, element).delete(element, Equal.<E>anyEqual());
    }

    public void setDelegate(Neighbourhood<E> delegate) {
        this.delegate = delegate;
    }
}
