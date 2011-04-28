package net.cilib.matchers;

import fj.data.Option;
import net.cilib.collection.Topology;
import net.cilib.entity.FitnessComparator;
import net.cilib.entity.HasFitness;

/**
 * A collection of matchers based on Entity instances.
 */
public final class EntityMatchers {

    private EntityMatchers() {
        throw new UnsupportedOperationException();
    }

    /**
     * Return the most fit {@code HasFitness} instance contained within the
     * provided {@code Topology}.
     * @param topology the container of elements to compare.
     * @param comparator the comparison method.
     * @return an {@code Option} containing the most fit instance, or
     *         {@code none}.
     */
    public static <A extends HasFitness> Option<A> mostFit(Topology<A> topology, FitnessComparator comparator) {
        A currentBest = null;
        for (A a : topology) {
            if (currentBest == null) {
                currentBest = a;
            }
            currentBest = comparator.moreFit(a, currentBest);
        }
        return (currentBest != null) ? Option.some(currentBest) : Option.<A>none();
    }
}
