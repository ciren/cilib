package net.cilib.collection.immutable;

import com.google.common.collect.Iterators;
import java.util.Iterator;
import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;

/**
 *
 * @author gpampara
 */
public class EmptyImmutableTopologyTest {

    @Test
    public void neighborhoodOfArbObject() {
        EmptyImmutableTopology topology = EmptyImmutableTopology.INSTANCE;
        Assert.assertThat(topology.neighborhoodOf(new Object()), instanceOf(Iterator.class));
        Assert.assertThat(topology.neighborhoodOf(null), instanceOf(Iterator.class));
    }

    @Test
    public void emptyIterator() {
        EmptyImmutableTopology topology = EmptyImmutableTopology.INSTANCE;
        Iterator i = topology.iterator();
        Assert.assertThat(i, instanceOf(Iterator.class));
        Assert.assertThat(Iterators.size(i), equalTo(0));
    }
}