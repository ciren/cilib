/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.selection.arrangement;

import com.google.common.collect.Lists;
import java.util.List;

/**
 *
 */
public class RingBasedArrangement<T> implements Arrangement<T> {

    private T marker;

    public RingBasedArrangement(T marker) {
        this.marker = marker;
    }

    @Override
    public Iterable<T> arrange(Iterable<T> elements) {
        List<T> tmp = Lists.newArrayList(elements);
        List<T> result = Lists.newArrayListWithCapacity(tmp.size());

        int position = 0;
        for (T entry : elements) {
            if (this.marker.equals(entry)) {
                break;
            }
            position++;
        }

        for (int i = 0; i < tmp.size(); ++i) {
            result.add(tmp.get((position + 1 + i) % tmp.size()));
        }

        return result;
    }
}
