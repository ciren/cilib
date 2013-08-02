/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.selection;

import com.google.common.base.Predicate;
import java.util.List;
import net.sourceforge.cilib.util.selection.arrangement.Arrangement;

public interface PartialSelection<T> {
    
    PartialSelection<T> exclude(T... items);

    PartialSelection<T> exclude(Iterable<T> items);

    PartialSelection<T> filter(Predicate<? super T> predicate);
    
    PartialSelection<T> orderBy(Arrangement arangement);
    
    /**
     * Return the first element within the result of matching elements.
     *
     * @return the selected element.
     */
    T select();

    /**
     * Return the list of matching elements.
     *
     * @param predicate
     * @return a list of selected elements.
     */
    List<T> select(Samples predicate);

}
