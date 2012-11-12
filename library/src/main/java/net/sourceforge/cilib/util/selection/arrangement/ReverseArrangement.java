/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.selection.arrangement;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class ReverseArrangement<T> implements Arrangement<T> {

    @Override
    public Iterable<T> arrange(Iterable<T> elements) {
        List<T> list = Lists.newArrayList(elements);
        Collections.reverse(list);
        return list;
    }
}
