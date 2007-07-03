package net.sourceforge.cilib.util;

import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.cilib.type.types.Vector;

/**
 * The Minkowski Metric is a generic measure of distance. It is defined in:<br/>
 * <strong>Data Clustering: A Review</strong> by A.K. Jain and M.N. Murty and P.J. Flynn<br/>
 * <em>ACM Computing Surveys</em>, pages 264-323, number 3, volume 31, September 1999.<br/>
 * {@link net.sourceforge.cilib.util.ManhattanDistanceMeasure Manhattan Distance} is a special case of the Minkowski Metric with 'alpha' := 1.<br/>
 * {@link net.sourceforge.cilib.util.EuclideanDistanceMeasure Euclidean Distance} is a special case of the Minkowski Metric with 'alpha' := 2.<br/>
 * The default 'alpha' value, when this class is instantiated, is 3.
 * @author Theuns Cloete
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
	 * Instantiate the Minkowski Metric with the specified 'alpha' value
	 * @param a the value to which 'alpha' should be set
	 */
	public MinkowskiMetric(int a) {
		alpha = a;
	}

	/**
	 * Calculate the distance between two vectors.
	 * @param x the one vector.
	 * @param y the other vector.
	 * @return the distance (as a double) between the two vectors.
	 * @throws IllegalArgumentException when the two vectors' dimension differ.
	 */
	public <T extends Vector> double distance(T x, T y) {
		if(x.getDimension() != y.getDimension())
			throw new IllegalArgumentException("Cannot calculate Minkowski Metric for vectors of different dimensions");
		if(alpha < 1)
			throw new IllegalArgumentException("The 'alpha' parameter of the Minkowski Metric must be >= 1, i.e. not " + alpha);

		double distance = 0.0;
		for(int i = 0; i < x.getDimension(); ++i) {
			distance += Math.pow(Math.abs(x.getReal(i) - y.getReal(i)), alpha);
		}
		return Math.pow(distance, 1.0 / alpha);
	}

	/**
	 * Calculate the distance between two vectors represented by Java Collection objects.
	 * @param x the one Java Collection object.
	 * @param y the other Java Collection object.
	 * @return the distance (as a double) between the two vectors.
	 * @throws IllegalArgumentException when the two vectors' dimension differ.
	 */
	public <T extends Collection<? extends Number>> double distance(T x, T y) {
		if (x.size() != y.size())
			throw new IllegalArgumentException("Cannot calculate Minkowski Metric for vectors of different dimensions");
		if(alpha < 1)
			throw new IllegalArgumentException("The 'alpha' parameter of the Minkowski Metric must be >= 1, i.e. not " + alpha);

		double distance = 0;
		Iterator<? extends Number> i = x.iterator();
		Iterator<? extends Number> j = y.iterator();

		for( ; i.hasNext() && j.hasNext(); ) {
			distance += Math.pow(Math.abs(i.next().doubleValue() - j.next().doubleValue()), alpha);
		}
		return Math.pow(distance, 1.0 / alpha);
	}

	/**
	 * Set the 'alpha' value that will be used in the calculation of the Minkowski Metric.
	 * @param a the new 'alpha' value
	 * @throws IllegalArgumentException when the given parameter is less than one.
	 */
	public void setAlpha(int a) {
		if(a < 1)
			throw new IllegalArgumentException("The 'alpha' parameter of the Minkowski Metric must be >= 1");
		alpha = a;
	}
}
