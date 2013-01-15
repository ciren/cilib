/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.selection;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class Samples {

    private final Strategy strategy;
    private static final Samples ALL = new Samples(new Strategy() {
        @Override
        public <T> List<T> sample(List<T> list) {
            return list;
        }
    });

    private Samples(Strategy strategy) {
        this.strategy = strategy;
    }

    private Samples(Samples prototype) {
        this.strategy = prototype.strategy;
    }

    public static Samples first() {
        return new Samples(new DefaultStrategy(1));
    }

    public static Samples first(int number) {
        return new Samples(new DefaultStrategy(number));
    }

    public Samples unique() {
        return new Samples(new UniqueStrategy(strategy));
    }

    public final <T> List<T> sample(List<T> list) {
        return this.strategy.sample(list);
    }

    public static Samples all() {
        return ALL;
    }

    public static Samples last() {
        return new Samples(new DefaultStrategy(1) {
            @Override
            public <T> List<T> sample(List<T> list) {
                List<T> current = Lists.newArrayList(list);
                Collections.reverse(current);
                return super.sample(current);
            }
        });
    }

    public static Samples last(final int number) {
        return new Samples(new DefaultStrategy(number) {
            @Override
            public <T> List<T> sample(List<T> list) {
                List<T> current = Lists.newArrayList(list);
                Collections.reverse(current);
                return super.sample(current);
            }
        });
    }

    private interface Strategy {

        public <T> List<T> sample(List<T> list);
    }

    private final static class UniqueStrategy implements Strategy {

        private final Strategy strategy;

        private UniqueStrategy(Strategy strategy) {
            this.strategy = checkNotNull(strategy);
        }

        @Override
        public <T> List<T> sample(List<T> list) {
            Set<T> set = Sets.newLinkedHashSet(list);
            return strategy.sample(Lists.newArrayList(set));
        }
    }

    private static class DefaultStrategy implements Strategy {

        private final int number;

        DefaultStrategy(int number) {
            this.number = number;
        }

        @Override
        public <T> List<T> sample(List<T> list) {
            checkArgument(list.size() >= number);
            List<T> result = Lists.newArrayListWithExpectedSize(number);
            int count = 0;
            for (T t : list) {
                if (count < number) {
                    result.add(t);
                    count++;
                }
            }
            return result;
        }
    }
}
