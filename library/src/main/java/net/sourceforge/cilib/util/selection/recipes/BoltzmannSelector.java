/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.selection.recipes;

import java.util.Arrays;
import java.util.Iterator;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.comparator.BoltzmannComparator;
import net.sourceforge.cilib.util.selection.PartialSelection;
import net.sourceforge.cilib.util.selection.Selection;

public class BoltzmannSelector<E extends Entity> implements Selector<E> {
    
    private BoltzmannComparator comparator;
    
    public BoltzmannSelector() {
        this.comparator = new BoltzmannComparator();
    }

    @Override
    public PartialSelection<E> on(Iterable<E> iterable) {
        Iterator<E> iter = iterable.iterator();
        E item1 = iter.next();
        E item2 = iter.next();        
        
        if (comparator.compare(item1, item2) < 0) {
            return Selection.copyOf(Arrays.asList(item2));
        }
        
        return Selection.copyOf(Arrays.asList(item1));
    }

    public void setComparator(BoltzmannComparator comparator) {
        this.comparator = comparator;
    }
}
