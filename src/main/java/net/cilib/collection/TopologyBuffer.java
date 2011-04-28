package net.cilib.collection;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @param <A>
 */
public class TopologyBuffer<A> {
    private Topology.TopologyBuilder<A> builder;
    private final List<A> list;

    public TopologyBuffer(Topology.TopologyBuilder<A> builder, ImmutableList<A> list) {
        this.builder = builder;
        this.list = Lists.newArrayList(list);
    }

    /**
     * @param element
     * @return
     */
    public boolean add(A element) {
        return this.list.add(element);
    }

    /**
     * @return
     */
    public Topology<A> build() {
        try {
            for (A a : list) {
                builder.add(a);
            }
            return builder.build();
        } finally {
            builder = null;
        }
    }
}
