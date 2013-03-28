/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Utility methods for {@link Vector}s.
 */
public final class Vectors {

    /**
     * Default constructor. Specified constructor to be private so that an instance
     * of this utility class cannot be created.
     */
    private Vectors() {
    }

    /**
     * Constructs a {@link Vector} from another vector with each component's
     * value set to the upper bound of that component.
     *
     * @param   vector The {@linkplain Vector} to create the upper bound vector from.
     * @return  a {@link Vector} with all the elements set to their respective upper bounds
     * @throws  UnsupportedOperationException When an element in the
     *          {@linkplain Vector} is not a {@link Numeric}
     */
    public static Vector upperBoundVector(Vector vector) {
        Vector.Builder upper = Vector.newBuilder();
        for (Numeric element : vector) {
            upper.addWithin(element.getBounds().getUpperBound(), element.getBounds());
        }
        return upper.build();
    }

    /**
     * Constructs a {@link Vector} from another vector with each component's
     * value set to the lower bound of that component.
     *
     * @param vector    The {@linkplain Vector} from which to create the lower
     *                  bound vector.
     * @return          a {@linkplain Vector} with all the elements set to their
     *                  respective lower bounds
     * @throws          UnsupportedOperationException when an element in the
     *                  {@linkplain Vector} is not a {@link Numeric}
     */
    public static Vector lowerBoundVector(Vector vector) {
        Vector.Builder lower = Vector.newBuilder();
        for (Numeric element : vector) {
            lower.addWithin(element.getBounds().getLowerBound(), element.getBounds());
        }
        return lower.build();
    }

    /**
     * Determine the sum of a list of {@link Vector} instances. Convenience
     * method for an array of vectors.
     *
     * @param vectors The {@linkplain Vector} instances to sum.
     * @return The resultant {@linkplain Vector}.
     */
    public static Vector sumOf(Vector... vectors) {
        return sumOf(Arrays.asList(vectors));
    }

    /**
     * Determine the sum of a list of {@link Vector} instances.
     *
     * @param vectors The {@linkplain Vector} instances to sum.
     * @return The resultant {@linkplain Vector}.
     */
    public static Vector sumOf(List<Vector> vectors) {
        if (vectors.isEmpty()) {
            return null;
        }

        Vector result = vectors.get(0);

        if (vectors.size() > 1) {
            for(int i = 1; i < vectors.size(); i++) {
                result = result.plus(vectors.get(i));
            }
        }

        return result;
    }

    /**
     * Determine the mean of a list of {@link Vector} instances. Convenience
     * method for an array of vectors.
     *
     * @param vectors The {@linkplain Vector} instances to sum.
     * @return The resultant {@linkplain Vector}.
     */
    public static Vector mean(Vector... vectors) {
        return mean(Arrays.asList(vectors));
    }

    /**
     * Determine the sum of a list of {@link Vector} instances.
     *
     * @param vectors The {@linkplain Vector} instances to sum.
     * @return The resultant {@linkplain Vector}.
     */
    public static Vector mean(List<Vector> vectors) {
        return sumOf(vectors).divide(vectors.size());
    }

    /**
     * Uses the Gram-Schmidt process to orthonormalize a list of {@link Vector}
     * instances.
     *
     * @param vectors The {@linkplain Vector} instances to orthonormalize.
     * @return The resultant {@linkplain Vector}.
     */
    public static List<Vector> orthonormalize(List<Vector> vectors) {
        List<Vector> orthonormalBases = Lists.newArrayList();
        List<Vector> result = Lists.newArrayList();

        Vector u1 = Vector.copyOf(vectors.get(0));
        orthonormalBases.add(u1);

        for (int i = 1; i < vectors.size(); i++) {
            Vector ui = vectors.get(i);

            for (int j = 0; j < orthonormalBases.size(); j++) {
                ui = ui.subtract(vectors.get(i).project(orthonormalBases.get(j)));
            }

            if (!ui.isZero()) {
                orthonormalBases.add(ui);
            }
        }

        for (Vector v : orthonormalBases) {
            result.add(v.normalize());
        }

        return result;
    }

    public static <T extends Number> Vector transform(Vector vector, Function<Numeric, T> function) {
        Vector.Builder builder = Vector.newBuilder();
        for (Numeric n : vector) {
            builder.addWithin(function.apply(n).doubleValue(), n.getBounds()); //??
        }
        return builder.build();
    }
}
