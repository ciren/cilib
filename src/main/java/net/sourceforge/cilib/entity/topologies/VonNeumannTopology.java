/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.entity.topologies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import net.sourceforge.cilib.entity.AbstractTopology;
import net.sourceforge.cilib.entity.Entity;

/**
 * <p>
 * Implementation of the Von Neumann neighbourhood topology. The Von Neumann topology is
 * a two dimensional grid of particles with wrap around.
 * </p><p>
 * Refereces:
 * </p><p><ul><li>
 * J. Kennedy and R. Mendes, "Population structure and particle swarm performance,"
 * in Proceedings of the IEEE Congress on Evolutionary Computation,
 * (Honolulu, Hawaii USA), May 2002.
 * </li></ul></p>
 *
 * @author Edwin Peer
 * @author Gary Pampara
 *
 * @param <E> A {@linkplain Entity} instance.
 */
public class VonNeumannTopology<E extends Entity> extends AbstractTopology<E> {
    private static final long serialVersionUID = -4795901403887110994L;

    private enum Direction { CENTER, NORTH, EAST, SOUTH, WEST, DONE };
    private ArrayList<ArrayList<E>> entities;
    private int lastRow;
    private int lastCol;

    /**
     * Creates a new instance of <code>VonNeumannTopology</code>.
     */
    public VonNeumannTopology() {
        entities = new ArrayList<ArrayList<E>>();
        lastRow = 0;
        lastCol = -1;
    }

    /**
     * Copy constructor. Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    @SuppressWarnings("unchecked")
    public VonNeumannTopology(VonNeumannTopology<E> copy) {
        this.entities = new ArrayList<ArrayList<E>>(copy.entities.size());
        for (ArrayList<E> list : copy.entities) {
            ArrayList<E> tmpList = new ArrayList<E>(list.size());

            for (E e : list) {
                tmpList.add((E) e.getClone());
            }

            this.entities.add(tmpList);
        }

        this.lastRow = copy.lastRow;
        this.lastCol = copy.lastCol;
    }

    /**
     * {@inheritDoc}
     */
    public VonNeumannTopology<E> getClone() {
        return new VonNeumannTopology<E>(this);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Iterator<E> neighbourhood(Iterator<? extends Entity> iterator) {
        MatrixIterator<E> i = (MatrixIterator<E>) iterator;
        return new VonNeumannNeighbourhoodIterator<E>(this, i);
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<E> iterator() {
        return new VonNeumannTopologyIterator<E>(this);
    }

    /**
     * {@inheritDoc}
     */
    public boolean add(E particle) {
        int min = entities.size();
        ArrayList<E> shortest = null;
        for (ArrayList<E> tmp : entities) {
            if (tmp.size() < min) {
                shortest = tmp;
                min = tmp.size();
            }
        }
        if (shortest == null) {
            shortest = new ArrayList<E>(entities.size() + 1);
            entities.add(shortest);
        }
        shortest.add(particle);

        lastRow = entities.size() - 1;
        lastCol = entities.get(lastRow).size() - 1;

        return true;
    }

    /**
     * {@inheritDoc}
     */
    public boolean addAll(Collection<? extends E> set) {
        this.entities.ensureCapacity(this.entities.size()+set.size());
        Iterator<? extends E> i = set.iterator();

        while (i.hasNext()) {
            this.add(i.next());
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    public int size() {
        // TODO: couldn't we just return entities.size()?
        int size = 0;

        for (ArrayList<E> i : entities)
            size += i.size();

        return size;
    }

    private void remove(int x, int y) {
        ArrayList<E> row = entities.get(x);
        row.remove(y);
        if (row.size() == 0) {
            entities.remove(x);
        }

        lastRow = entities.size() - 1;
        lastCol = entities.get(lastRow).size() - 1;
    }



    private interface MatrixIterator<T extends Entity> extends Iterator<T> {
        public int getRow();
        public int getCol();
    }

    private class VonNeumannTopologyIterator<T extends Entity> implements MatrixIterator<T> {

        private int row;
        private int col;
        private VonNeumannTopology<T> topology;

        public VonNeumannTopologyIterator(VonNeumannTopology<T> topology) {
            this.topology = topology;
            row = 0;
            col = -1;
        }

        /**
         * {@inheritDoc}
         */
        public boolean hasNext() {
            return row != topology.lastRow || col != topology.lastCol;
        }

        /**
         * {@inheritDoc}
         */
        public T next() {
            if (row == topology.lastRow && col == topology.lastCol) {
                throw new NoSuchElementException();
            }

            ++col;
            if (col >= entities.get(row).size()) {
                ++row;
                col = 0;
            }

            return topology.entities.get(row).get(col);
        }

        /**
         * {@inheritDoc}
         */
        public void remove() {
            if (col == -1) {
                throw new IllegalStateException();
            }

            topology.remove(row, col);

            --col;
            if (row != 0 && col < 0) {
                --row;
                col = topology.entities.get(row).size() - 1;
            }
        }

        /**
         * {@inheritDoc}
         */
        public int getRow() {
            return row;
        }

        /**
         * {@inheritDoc}
         */
        public int getCol() {
            return col;
        }
    }

    private class VonNeumannNeighbourhoodIterator<T extends Entity> implements MatrixIterator<T> {

        private int x;
        private int y;
        private int row;
        private int col;
        private Direction index;
        private VonNeumannTopology<T> topology;


        public VonNeumannNeighbourhoodIterator(VonNeumannTopology<T> topology, MatrixIterator<T> iterator) {
            if (iterator.getCol() == -1) {
                throw new IllegalStateException();
            }
            this.topology = topology;
            row = x = iterator.getRow();
            col = y = iterator.getCol();
            index = Direction.CENTER;
        }

        /**
         * {@inheritDoc}
         */
        public boolean hasNext() {
            return (index != Direction.DONE);
        }

        /**
         * {@inheritDoc}
         */
        public T next() {
            switch (index) {
                case CENTER: {
                    row = x;
                    col = y;
                    break;
                }

                case NORTH: {
                    row = x - 1;
                    col = y;
                    while (true) {
                        if (row < 0) {
                            row = topology.entities.size() - 1;
                        }
                        if (col < topology.entities.get(row).size()) {
                            break;
                        }
                        --row;
                    }
                    break;
                }

                case EAST: {
                    row = x;
                    col = y + 1;
                    if (col >= topology.entities.get(row).size()) {
                        col = 0;
                    }
                    break;
                }

                case SOUTH: {
                    row = x + 1;
                    col = y;
                    while (true) {
                        if (row >= topology.entities.size()) {
                            row = 0;
                        }
                        if (col < topology.entities.get(row).size()) {
                            break;
                        }
                        ++row;
                    }
                    break;
                }

                case WEST: {
                    row = x;
                    col = y - 1;
                    if (col < 0) {
                        col = topology.entities.get(row).size() - 1;
                    }
                    break;
                }

                default: throw new NoSuchElementException();
            }

            index = Direction.values()[index.ordinal()+1];
            return topology.entities.get(row).get(col);
        }

        /**
         * {@inheritDoc}
         */
        public void remove() {
            topology.remove(row, col);
            if (index == Direction.CENTER) {
                index = Direction.DONE;
            }
        }

        /**
         * {@inheritDoc}
         */
        public int getRow() {
            return row;
        }

        /**
         * {@inheritDoc}
         */
        public int getCol() {
            return col;
        }

    }

    /**
     * {@inheritDoc}
     */
    public boolean remove(E indiv) {
        throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
    }

    /**
     * {@inheritDoc}
     */
    public E get(int index) {
        throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
    }

    /**
     * {@inheritDoc}
     */
    public E set(int index, E indiv) {
        throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
    }

    /**
     * {@inheritDoc}
     */
    public List<E> asList() {
        List<E> entityList = new ArrayList<E>();
        for (ArrayList<E> i : entities){
            entityList.addAll(i);
        }
        return entityList;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEmpty() {
        throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
    }

    /**
     * {@inheritDoc}
     */
    public void clear() {
        this.entities.clear();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return this.entities.hashCode() + this.lastCol*6 + this.lastRow*8;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
    }

    /**
     * {@inheritDoc}
     */
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
    }

    /**
     * {@inheritDoc}
     */
    public void add(int index, E element) {
        throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
    }

    /**
     * {@inheritDoc}
     */
    public E remove(int index) {
        throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
    }

    /**
     * {@inheritDoc}
     */
    public int indexOf(Object o) {
        throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
    }

    /**
     * {@inheritDoc}
     */
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
    }

    /**
     * {@inheritDoc}
     */
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
    }

    /**
     * {@inheritDoc}
     */
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
    }

    /**
     * {@inheritDoc}
     */
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Method not supported in VonNeumannTopology");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setId(String id) {

    }

}
