/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.entity.topologies;

import java.util.*;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
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
 *
 * @param <E> A {@linkplain Entity} instance.
 */
public class VonNeumannTopology<E extends Entity> extends AbstractTopology<E> {
    private static final long serialVersionUID = -4795901403887110994L;

    private enum Direction { CENTER, NORTH, EAST, SOUTH, WEST, DONE };

    /**
     * Default constructor.
     */
    public VonNeumannTopology() {
        super();
        this.neighbourhoodSize = ConstantControlParameter.of(5);
    }

    /**
     * Copy constructor.
     */
    public VonNeumannTopology(VonNeumannTopology<E> copy) {
        super(copy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VonNeumannTopology<E> getClone() {
        return new VonNeumannTopology<E>(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<E> neighbourhood(Iterator<? extends Entity> iterator) {
        return new VonNeumannNeighbourhoodIterator<E>(this, (IndexedIterator<E>) iterator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNeighbourhoodSize() {
        return 5;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNeighbourhoodSize(ControlParameter neighbourhoodSize) {
        //Note: This is fixed to 5 so it cant be change
    }

    /**
     * Iterator to traverse the Von Neumann topology.
     */
    private class VonNeumannNeighbourhoodIterator<T extends Entity> extends NeighbourhoodIterator<T> {

        private final int sqSide;
        private final int nRows;
        private final int row;
        private final int col;
        private Direction element;

        public VonNeumannNeighbourhoodIterator(AbstractTopology<T> topology, IndexedIterator<T> iterator) {
            super(topology, iterator)         ;
            
            this.sqSide = (int) Math.round(Math.sqrt(topology.size()));
            this.row = iterator.getIndex() / sqSide;
            this.col = iterator.getIndex() % sqSide;
            this.nRows = (int) Math.ceil(topology.size() / (double) sqSide);
            this.index = row * sqSide + col;
            this.element = Direction.CENTER;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasNext() {
            return (element != Direction.DONE);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public T next() {
            int r;
            int c;
            
            switch (element) {
                case CENTER:
                    r = row;
                    c = col;
                    break;

                case NORTH:
                    r = (row - 1 + nRows) % nRows;
                    c = col;
                    while (c >= getColumnsInRow(r)) {
                        r = (--r + nRows) % nRows;
                    }
                    break;

                case EAST:
                    r = row;
                    c = (col + 1) % getColumnsInRow(r);
                    break;

                case SOUTH:
                    r = (row + 1) % nRows;
                    c = col;                    
                    while (c >= getColumnsInRow(r)) {
                        r = ++r % nRows;
                    }                    
                    break;

                case WEST:
                    r = row;
                    c = (col - 1 + getColumnsInRow(r)) % getColumnsInRow(r);
                    break;

                default: throw new NoSuchElementException();
            }

            index = r * sqSide + c;
            element = Direction.values()[element.ordinal()+1];
            return topology.get(index);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void remove() {
            topology.remove(index);
            if (element == Direction.CENTER) {
                element = Direction.DONE;
            }
        }
        
        /**
         * Gets the number of columns in a given row.
         * 
         * @param r The given row.
         * @return The number of columns in the row.
         */
        private int getColumnsInRow(int r) {
            return r == nRows - 1 ? topology.size() - r * sqSide : sqSide;
        }
    }
}
