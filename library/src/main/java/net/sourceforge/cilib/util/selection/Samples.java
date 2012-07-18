/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.util.selection;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;
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
            Set<T> set = Sets.newHashSet(list);
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
