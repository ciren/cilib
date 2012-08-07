package net.sourceforge.cilib.entity;

import java.util.Iterator;

/**
 * Interface to define the manner in which the iterator is to be constructed for Array types.
 *
 * @param <T> The {@linkplain Entity} type.
 */
public interface IndexedIterator<T extends Entity> extends Iterator<T> {
    public int getIndex();
}