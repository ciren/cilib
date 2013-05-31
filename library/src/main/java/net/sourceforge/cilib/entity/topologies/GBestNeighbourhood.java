package net.sourceforge.cilib.entity.topologies;

import fj.data.List;

public class GBestNeighbourhood<E> extends Neighbourhood<E> {

    @Override
    public List<E> f(List<E> list, E element) {
        return list;
    }

}
