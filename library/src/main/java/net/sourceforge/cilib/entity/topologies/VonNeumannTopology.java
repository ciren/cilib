/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.topologies;

import com.google.common.collect.UnmodifiableIterator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;

/**
 * <p>
 * Implementation of the Von Neumann neighbourhoodOf topology. The Von Neumann topology is
 * a two dimensional grid of particles with wrap around.
 * </p><p>
 * Refereces:
 * </p><p><ul><li>
 * J. Kennedy and R. Mendes, "Population structure and particle swarm performance,"
 * in Proceedings of the IEEE Congress on Evolutionary Computation,
 * (Honolulu, Hawaii USA), May 2002.
 * </li></ul></p>
 * @param <E> A {@linkplain Entity} instance.
 */
public class VonNeumannTopology<E extends Entity> extends AbstractTopology<E> {

    private static final long serialVersionUID = -4795901403887110994L;

    private enum Direction { CENTER, NORTH, EAST, SOUTH, WEST, DONE };

    public VonNeumannTopology() {
        this.neighbourhoodSize = ConstantControlParameter.of(5);
    }

    public VonNeumannTopology(VonNeumannTopology copy) {
        super(copy);
    }

    @Override
    public VonNeumannTopology getClone() {
        return new VonNeumannTopology(this);
    }

    @Override
    public void setNeighbourhoodSize(ControlParameter neighbourhoodSize) {
        //Note: This is fixed to 5 so it cant be changed
    }

    @Override
    protected Iterator<E> neighbourhoodOf(final E e) {
        return new UnmodifiableIterator<E>() {
            private int np = entities.size();
            private int index = entities.indexOf(e);
            private final int sqSide = (int) Math.round(Math.sqrt(np));
            private final int nRows = (int) Math.ceil(np / (double) sqSide);;
            private final int row = index / sqSide;
            private final int col = index % sqSide;
            private Direction element = Direction.CENTER;

            @Override
            public boolean hasNext() {
                return (element != Direction.DONE);
            }

            @Override
            public E next() {
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
                return entities.get(index);
            }

            /**
             * Gets the number of columns in a given row.
             *
             * @param r The given row.
             * @return The number of columns in the row.
             */
            private int getColumnsInRow(int r) {
                return r == nRows - 1 ? np - r * sqSide : sqSide;
            }
        };
    }
}
