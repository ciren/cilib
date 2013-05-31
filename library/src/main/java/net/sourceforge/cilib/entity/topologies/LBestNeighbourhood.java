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