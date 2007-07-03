package net.sourceforge.cilib.util;

import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.cilib.type.types.Vector;

/**
 * The Cosine Distance Measure (or vector dot product) is not a distance measure, but
 * rather a similarity metric. It is defined in:<br/>
 * <strong>Learning structure and concepts in data through data clustering</strong>
 * by Gregory James Hamerly<br/>
 * 2003<br/>
 * More positive values indicate similarity. The vector dot product is the sum of the
 * product of each attribute from two vectors being compared. Here we use a normalized
 * version of the metric so that the dot product is always a value between -1 and 1. The
 * vector dot product does not measure the difference in magnitude between two vectors;
 * only the difference in the angle between the vectors. If the (normalized) result = 1,
 * the two vectors are collinear and pointed in the same direction, if the (normalized)
 * result = -1, the two vectors are collinear and pointed in opposite directions, and if
 * the (normalized) result = 0, then the two vectors are orthogonal. We can convert this
 * similarity measure to a "distance" by subtracting it from one, which will always give
 * a value between 0 and 2. <strong>This is the approach we follow.</strong>
 * @author Theuns Cloete
 */
public class CosineDistanceMeasure implements DistanceMeasure {

	/**
	 * Calculate the "distance" (dot product) between (of) two vectors.
	 * @param x the one vector.
	 * @param y the other vector.
	 * @return the "distance" (or angle or dot product) (as a double) between the two vectors.
	 * @throws IllegalArgumentException when the two vectors' dimension differ.
	 */
	public <T extends Vector> double distance(T x, T y) {
		if(x.getDimension() != y.getDimension())
			throw new IllegalArgumentException("Cannot calculate Cosine Distance for vectors of different dimensions");

		double distance = 0.0, norm_x = 0.0, norm_y = 0.0;
		double x_i = 0.0, y_i = 0.0;
		for(int i = 0; i < x.getDimension(); ++i) {
			x_i = x.getReal(i);
			y_i = y.getReal(i);
			distance += x_i * y_i;
			norm_x += Math.pow(x_i, 2.0);
			norm_y += Math.pow(y_i, 2.0);
		}
		norm_x = Math.sqrt(norm_x);
		norm_y = Math.sqrt(norm_y);
		if(norm_x <= 0.0 || norm_y <= 0.0)
			throw new ArithmeticException("Division by zero");
		//convert to distance by subtracting from 1
		return 1.0 - (distance / (norm_x * norm_y));
	}

	/**
	 * Calculate the "distance" (dot product) between (of) two vectors represented by Java Collection objects.
	 * @param x the one Java Collection object.
	 * @param y the other Java Collection object.
	 * @return the distance (or angle or dot product) (as a double) between the two vectors.
	 * @throws IllegalArgumentException when the two vectors' dimension differ.
	 */
	public <T extends Collection<? extends Number>> double distance(T x, T y) {
		if (x.size() != y.size())
			throw new IllegalArgumentException("Cannot calculate Cosine Distance for vectors  of different dimensions");

		Iterator<? extends Number> i = x.iterator();
		Iterator<? extends Number> j = y.iterator();
		double distance = 0.0, norm_x = 0.0, norm_y = 0.0;
		double x_i = 0.0, y_i = 0.0;
		for( ; i.hasNext() && j.hasNext(); ) {
			x_i = i.next().doubleValue();
			y_i = j.next().doubleValue();
			distance += x_i * y_i;
			norm_x += Math.pow(x_i, 2.0);
			norm_y += Math.pow(y_i, 2.0);
		}
		norm_x = Math.sqrt(norm_x);
		norm_y = Math.sqrt(norm_y);
		if(norm_x <= 0.0 || norm_y <= 0.0)
			throw new ArithmeticException("Division by zero");
		//convert to distance by subtracting from 1
		return 1.0 - (distance / (norm_x * norm_y));
	}
}
