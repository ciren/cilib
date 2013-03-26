/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util;

import com.google.common.collect.Lists;
import fj.F;
import java.util.List;
import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Matrix;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Utility functions pertaining to Matrices.
 */
public class Matrices {

    /**
     * Creates a square identity matrix.
     * @param size The dimension of the matrix.
     * @return The identity matrix.
     */
    public static Matrix getIdentityMatrix(int size) {
        return Matrix.builder().dimensions(size, size).identity().build();
    }

    /**
     * Creates a random square orthonormal matrix.
     * @param size The dimension of the matrix.
     * @return The orthonormal matrix.
     */
    public static Matrix getRandomOrthonormalMatrix(int size) {
        List<Vector> vecs = Lists.<Vector>newArrayList();
        final GaussianDistribution random = new GaussianDistribution();
        Vector proto = Vector.fill(0.0, size);
        Matrix.Builder builder = Matrix.builder().dimensions(size, size);

        //get random vectors
        for(int i = 0; i < size; i++) {
            vecs.add(proto.map(new F<Numeric, Numeric>() {
                @Override
                public Numeric f(Numeric x) {
                    return Real.valueOf(random.getRandomNumber());
                }
            }));
        }

        //orthonormalize the random vectors
        vecs = Vectors.orthonormalize(vecs);

        //convert vectors to matrix
        for (Vector v : vecs) {
            Double[] vals = new Double[size];
            int i = 0;

            for (Numeric n : v) {
                vals[i++] = n.doubleValue();
            }

            builder.addRow(vals);
        }

        return builder.build();
    }

    /**
     * Creates a random linear transformation matrix:
     * PxNxQ where P and Q are orthonormal matrices and N is a diagonal matrix with
     * n_ii = c^(U(1,D)-1 / D). c is the condition number.
     * @param size
     * @param condition
     * @return the random linear transformation matrix.
     */
    public static Matrix getRandomLinearTransformationMatrix(int size, int condition) {
        ProbabilityDistributionFunction random = new UniformDistribution();

        Matrix p = getRandomOrthonormalMatrix(size);
        Matrix q = getRandomOrthonormalMatrix(size);
        Matrix.Builder builder = Matrix.builder().dimensions(size, size);

        for (int i = 0; i < size; i++) {
            Double[] row = new Double[size];

            for (int j = 0; j < size; j++) {
                row[j] = i == j ? Math.pow(condition, (random.getRandomNumber(1, size + 1) - 1) / (size)) : 0.0;
            }

            builder.addRow(row);
        }

        Matrix n = builder.build();

        return p.times(n).times(q);
    }
}
