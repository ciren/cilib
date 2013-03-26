/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.distancemeasure;

import java.util.Collection;
import java.util.Iterator;
import net.sourceforge.cilib.type.types.Numeric;

/**
 * The Minkowski Metric is a generic measure of distance. It is defined in:
 * <p>
 * Article{ 331504, author = "A. K. Jain and M. N. Murty and P. J. Flynn", title = "Data
 *           Clustering: A Review", journal = "ACM Computing Surveys", volume = "31", number = "3",
 *           year = "1999", issn = "0360-0300", pages = "264--323", doi =
 *           "http://0-doi.acm.org.innopac.up.ac.za:80/10.1145/331499.331504", publisher = "ACM
 *           Press", address = "New York, NY, USA" }
 * </pre>
 * <p>
 * {@link ManhattanDistanceMeasure} is a special case of the Minkowski Metric with 'alpha' := 1.<br/>
 * {@link EuclideanDistanceMeasure} is a special case of the Minkowski Metric with 'alpha' := 2.
 * <p>
 * NOTE: The default 'alpha' value is 0 when this class is instantiated.
 */
public class MinkowskiMetric implements DistanceMeasure {

    protected int alpha = 0;

    /**
     * Instantiate the Minkowski Metric with 'alpha' value equal to zero. This means
     * that you have to call the setAlpha method right after calling this constructor.
     */
    public MinkowskiMetric() {
        alpha = 0;
    }

    /**
     * Instantiate the Minkowski Metric with the specified 'alpha' value.
     * @param a the value to which 'alpha' should be set
     */
    public MinkowskiMetric(int a) {
        if (a < 1) {
            throw new IllegalArgumentException("The 'alpha' parameter of the Minkowski Metric must be >= 1");
        }

        alpha = a;
    }

    /**
     * Calculate the distance between two vectors.
     * @param x the first vector.
     * @param y the second vector.
     * @return the distance (as a double) between the two vectors.
     * @throws IllegalArgumentException when the two vectors' dimension differ.
     */
    public double distance(Collection<? extends Numeric> x, Collection<? extends Numeric> y) {
        if (x.size() != y.size()) {
            throw new IllegalArgumentException("Cannot calculate Minkowski Metric for vectors of different dimensions: " + x.size() + " != " + y.size());
        }
        if (alpha < 1) {
            throw new IllegalArgumentException("The 'alpha' parameter of the Minkowski Metric must be >= 1, i.e. not " + alpha);
        }

        Iterator<?> xIterator = x.iterator();
        Iterator<?> yIterator = y.iterator();

        double distance = 0.0;
        for (int i = 0; i < x.size(); ++i) {
            Numeric xElement = (Numeric) xIterator.next();
            Numeric yElement = (Numeric) yIterator.next();

            distance += Math.pow(Math.abs(xElement.doubleValue() - yElement.doubleValue()), alpha);
        }
        return Math.pow(distance, 1.0 / alpha);
    }

    /**
     * Set the 'alpha' value that will be used in the calculation of the Minkowski Metric.
     * @param a the new 'alpha' value
     * @throws IllegalArgumentException when the given parameter is less than one.
     */
    public void setAlpha(int a) {
        if (a < 1) {
            throw new IllegalArgumentException("The 'alpha' parameter of the Minkowski Metric must be >= 1");
        }
        alpha = a;
    }
}
