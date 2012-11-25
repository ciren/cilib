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
import net.sourceforge.cilib.math.random.generator.Rand;

public class RandomArrangement<T> implements Arrangement<T> {

    @Override
    public Iterable<T> arrange(final Iterable<T> elements) {
        final List<T> list = Lists.newArrayList(elements);
        shuffle(list);
        return list;
    }

    /**
     * Implementation of the Fisher-Yates shuffle algorithm. This algorithm runs in O(n).
     * <p>
     * This method has been added to the implementation due to the fact that Collections.shuffle()
     * does not perform the same operation efficiently. Collections.shuffle() <b>does not</b>
     * use the current size of the permutation sublist.
     *
     * @param elements The elements to shuffle.
     */
    private <E> void shuffle(List<E> elements) {
        int n = elements.size();

        while (n > 1) {
            int k = Rand.nextInt(n); // 0 <= k < n
            n--;
            Collections.swap(elements, n, k);
        }
    }
}
