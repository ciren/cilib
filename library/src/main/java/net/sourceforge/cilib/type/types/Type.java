/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type.types;

import net.sourceforge.cilib.util.Cloneable;

/**
 * {@code Type} interface for all type-objects that are used within CIlib.
 * The types are built on an extend the Java Collections Framework.
 */
public interface Type extends Cloneable {

    /**
     * {@inheritDoc}
     */
    @Override
    Type getClone();

    /**
     * Compare the specified object with this type for equality. Returns
     * {@code true} if and only if the specified object is also an instance
     * of the same type.
     * @param obj The object to compare.
     * @return {@code true} if equality exists, {@code false} otherwise.
     * @see Object#equals(Object)
     */
    @Override
    boolean equals(Object obj);

    /**
     * Returns the hash code value for this list.  The hash code of a list
     * is defined to be the result of the following calculation:
     * <pre>
     *  int hashCode = 7;
     *  Iterator&lt;E&gt; i = list.iterator();
     *  while (i.hasNext()) {
     *      E obj = i.next();
     *      hashCode = 31*hashCode + (obj==null ? 0 : obj.hashCode());
     *  }
     * </pre>
     * This ensures that <tt>type1.equals(type2)</tt> implies that
     * <tt>type1.hashCode()==type2.hashCode()</tt> for any two types,
     * <tt>type1</tt> and <tt>type2</tt>, as required by the general
     * contract of {@link Object#hashCode}.
     *
     * @return the hash code value for this list
     * @see Object#equals(Object)
     * @see #equals(Object)
     */
    @Override
    int hashCode();

}
