/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.selection.arrangement;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 */
public class SortedArrangement<T extends Comparable> implements Arrangement<T> {
    
    private Comparator<T> comparator;

    public SortedArrangement() {
        this.comparator = Ordering.natural();
    }

    public SortedArrangement(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    @Override
    public Iterable<T> arrange(Iterable<T> elements) {
        List<T> list = Lists.newArrayList(elements);
        Collections.sort(list, comparator);
        return list;
    }
}
