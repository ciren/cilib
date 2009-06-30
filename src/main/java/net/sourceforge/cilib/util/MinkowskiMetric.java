/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.util;

import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.StructuredType;

/**
 * The Minkowski Metric is a generic measure of distance. It is defined in:<br/>
 * <pre>
 * Article{ 331504, author = "A. K. Jain and M. N. Murty and P. J. Flynn", title = "Data
 *           Clustering: A Review", journal = "ACM Computing Surveys", volume = "31", number = "3",
 *           year = "1999", issn = "0360-0300", pages = "264--323", doi =
 *           "http://0-doi.acm.org.innopac.up.ac.za:80/10.1145/331499.331504", publisher = "ACM
 *           Press", address = "New York, NY, USA" }
 * </pre>
 *
 * <p>
 * {@link net.sourceforge.cilib.util.ManhattanDistanceMeasure Manhattan Distance} is a special case of the Minkowski Metric with 'alpha' := 1.<br/>
 * {@link net.sourceforge.cilib.util.EuclideanDistanceMeasure Euclidean Distance} is a special case of the Minkowski Metric with 'alpha' := 2.
 * </p>
 * <p>
 * NOTE: The default 'alpha' value is 0 when this class is instantiated.
 * </p>
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
     * Instantiate the Minkowski Metric with the specified 'alpha' value.
     * @param a the value to which 'alpha' should be set
     */
    public MinkowskiMetric(int a) {
        if(a < 1)
            throw new IllegalArgumentException("The 'alpha' parameter of the Minkowski Metric must be >= 1");

        alpha = a;
    }

    /**
     * Calculate the distance between two vectors.
     * @param x the one vector.
     * @param y the other vector.
     * @return the distance (as a double) between the two vectors.
     * @throws IllegalArgumentException when the two vectors' dimension differ.
     */
    public <T extends Type, U extends StructuredType<T>> double distance(U x, U y) {
        if(x.size() != y.size())
            throw new IllegalArgumentException("Cannot calculate Minkowski Metric for vectors of different dimensions: " + x.size() + " != " + y.size());
        if(alpha < 1)
            throw new IllegalArgumentException("The 'alpha' parameter of the Minkowski Metric must be >= 1, i.e. not " + alpha);

        Iterator<T> xIterator = x.iterator();
        Iterator<T> yIterator = y.iterator();

        double distance = 0.0;
        for(int i = 0; i < x.size(); ++i) {
            Numeric xElement = (Numeric) xIterator.next();
            Numeric yElement = (Numeric) yIterator.next();

            distance += Math.pow(Math.abs(xElement.getReal() - yElement.getReal()), alpha);
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
            throw new IllegalArgumentException("Cannot calculate Minkowski Metric for vectors of different dimensions: " + x.size() + " != " + y.size());
        if(alpha < 1)
            throw new IllegalArgumentException("The 'alpha' parameter of the Minkowski Metric must be >= 1, i.e. not " + alpha);

        double distance = 0;
        Iterator<? extends Number> i = x.iterator();
        Iterator<? extends Number> j = y.iterator();

        while (i.hasNext() && j.hasNext()) {
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
