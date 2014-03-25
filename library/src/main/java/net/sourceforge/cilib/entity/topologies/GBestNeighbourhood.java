/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.topologies;

import fj.data.List;

public class GBestNeighbourhood<E> extends Neighbourhood<E> {

    @Override
    public List<E> f(List<E> list, E element) {
        return list;
    }

}
