/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.topologies;

import com.google.common.collect.Lists;

import fj.F;
import fj.data.List;

public class VonNeumannNeighbourhood<E> extends Neighbourhood<E> {

    private E find(List<E> list, int n, int r, int c) {
        return list.index(r * n + c);
    }

    @Override
    public List<E> f(final List<E> list, final E target) {
        final int np = list.length();
        final int index = Lists.newArrayList(list).indexOf(target);
        final int sqSide = (int) Math.round(Math.sqrt(np));
        final int nRows = (int) Math.ceil(np / (double) sqSide);
        final int row = index / sqSide;
        final int col = index % sqSide;

        final F<Integer, Integer> colsInRow = new F<Integer, Integer>() {
            @Override
            public Integer f(Integer r) {
                return r == nRows - 1 ? np - r * sqSide : sqSide;
            }
        };

        final E north = find(list, sqSide, (row - 1 + nRows) % nRows - ((col >= colsInRow.f((row - 1 + nRows) % nRows)) ? 1 : 0), col);
        final E south = find(list, sqSide, ((col >= colsInRow.f((row + 1) % nRows)) ? 0 : (row + 1) % nRows), col);
        final E east = find(list, sqSide, row, (col + 1) % colsInRow.f(row));
        final E west = find(list, sqSide, row, (col - 1 + colsInRow.f(row)) % colsInRow.f(row));

        return List.list(target, north, east, south, west);
    }
}
