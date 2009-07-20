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
package net.sourceforge.cilib.functions.continuous;

import java.util.Random;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * TODO: Complete this javadoc.
 */
public class FunctionDimensionMapping extends ContinuousFunction {
    private static final long serialVersionUID = 3785385852226926590L;

    private double [] generatedPoints;
    private double [][] generatedDistanceMatrix;
    private double [][] higherDimensionDistanceMatrix;
    private int dataDimension;
    private int number;

    private DistanceMeasure measure;

    /**
     * Create an instance of {@linkplain FunctionDimensionMapping}.
     */
    public FunctionDimensionMapping() {
        dataDimension = 2;
        number = 200;

        measure = new EuclideanDistanceMeasure();

        setDomain("B^10");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FunctionDimensionMapping getClone() {
        return new FunctionDimensionMapping();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double evaluate(Vector input) {
        // The vector x is an entities information vector which represents the seed to
        // a random number generator.
        long seed = convert(input);
        Random generator = new MersenneTwister();
        generator.setSeed(seed);

        // Now generate all the data points
        //System.out.println(Double.valueOf(dataDimension*number).intValue());
        generatedPoints = new double[Double.valueOf(dataDimension*number).intValue()];
        for (int i = 0; i < generatedPoints.length; i++) {
            double num = generator.nextDouble();
            generatedPoints[i] = num; //generator.nextDouble();
        }

        // Now calculate all the distances and create the distance matrix
        int size = higherDimensionDistanceMatrix.length;
        //System.out.println("Generated matrix length: " + size);
        generatedDistanceMatrix = new double[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Vector v1 = new Vector();
                Vector v2 = new Vector();
                v1.add(new Real(generatedPoints[i*this.dataDimension]));
                v1.add(new Real(generatedPoints[i*this.dataDimension+1]));

                v2.add(new Real(generatedPoints[j*this.dataDimension]));
                v2.add(new Real(generatedPoints[j*this.dataDimension+1]));
                double distance = measure.distance(v1, v2);
                generatedDistanceMatrix[i][j] = distance;
        //        System.out.println("generated[" + i + "][" + j + "]: " + generatedDistanceMatrix[i][j]);
            }
        }

        // Now determine the fitness of the mapping
        double fitness = 0.0;

        double c = 0.0;
        for (int i = 0; i < higherDimensionDistanceMatrix.length; i++) {
            for (int j = 0; (j < i) && (j < higherDimensionDistanceMatrix.length); j++) {
                c +=  higherDimensionDistanceMatrix[i][j];
            }
        }
        //System.out.println("C: " + C);

        for (int i = 0; i < higherDimensionDistanceMatrix.length; i++) {
            for (int j = 0; (j < i) && (j < higherDimensionDistanceMatrix.length); j++) {
                double d_star = higherDimensionDistanceMatrix[i][j];
                double numerator = d_star - generatedDistanceMatrix[i][j];
                fitness += (numerator * numerator) / d_star;
            }
        }
        return fitness/c;
    }


    private long convert(Vector vector) {
        long result = 0;

        String s = vector.toString(' ', ' ', ' ');
        String tmp = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != ' ')
                tmp += s.charAt(i);
        }

        result = Long.parseLong(tmp, 2);
        return result;
    }

    /**
     * Get the dimension of the data.
     * @return Returns the dimension.
     */
    public double getDataDimension() {
        return dataDimension;
    }

    /**
     * Set the dimension of the data.
     * @param dimension The dimension to set.
     */
    public void setDataDimension(int dimension) {
        this.dataDimension = dimension;
    }

    /**
     * @return Returns the number.
     */
    public double getNumber() {
        return number;
    }

    /**
     * @param number The number to set.
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * @return Returns the higherDimensionDistanceMatrix.
     */
    public double[][] getHigherDimensionDistanceMatrix() {
        return higherDimensionDistanceMatrix;
    }

    /**
     * @param higherDimensionDistanceMatrix The higherDimensionDistanceMatrix to set.
     */
    public void setHigherDimensionDistanceMatrix(
            double[][] higherDimensionDistanceMatrix) {
        this.higherDimensionDistanceMatrix = higherDimensionDistanceMatrix;
        this.number = this.higherDimensionDistanceMatrix.length;
    }
}
