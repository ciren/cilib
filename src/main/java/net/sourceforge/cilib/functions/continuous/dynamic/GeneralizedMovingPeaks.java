/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.functions.continuous.dynamic;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.type.parser.DomainParser;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.functions.DynamicFunction;

/**
 * A generalized implementation of the Moving Peaks benchmark problem
 * as presented by Branke.
 * <p/>
 * Reference:<br/>
 * <pre>
 * {@literal @}INPROCEEDINGS{785502, author={Branke, J.},
 * booktitle={Evolutionary Computation, 1999. CEC 99. Proceedings of the 1999 Congress on},
 * title={Memory enhanced evolutionary algorithms for changing optimization problems},
 * year={1999},
 * month={},
 * volume={3},
 * number={},
 * pages={3 vol. (xxxvii+2348)},
 * keywords={benchmark problem;changing optimization problems;explicit memory;memory enhanced evolutionary algorithms;negative side effects;evolutionary computation;storage management;},
 * doi={10.1109/CEC.1999.785502},
 * ISSN={},}
 * </pre>
 *
 * @author Bennie Leonard
 */
public class GeneralizedMovingPeaks implements ContinuousFunction, DynamicFunction {
    private ProbabilityDistributionFuction gaussian, uniform; //random providers.
    private int frequency; //the frequency (in iterations) with which the environment changes.
    private int peaks; //the number of peaks.
    private int lastChange = 0; //iteration that last change was applied
    private double widthSeverity, heightSeverity, shiftSeverity, lambda; //controls the severity and movement trends of peak movements.
    private double[] peakHeigths, peakWidths; //the current heights and widths of all peaks.
    private double minHeight, maxHeight, minWidth, maxWidth; //minimum and maximum values for peak heights and widths.
    private Vector[] peakPositions, shiftVectors; //the positions of all peaks, as well as the previous shift vectors.
    private int[][] movementDirections; //the movement directions of peaks in each dimension.

    public GeneralizedMovingPeaks() {
        this.gaussian = new GaussianDistribution();
        this.uniform = new UniformDistribution();

        this.frequency = 10;
        this.peaks = 5;
        this.widthSeverity = 0.01;
        this.heightSeverity = 7.0;
        this.shiftSeverity = 1.0;
        this.lambda = 0.75;
        this.minHeight = 30.0;
        this.maxHeight = 70.0;
        this.minWidth = 1.0;
        this.maxWidth = 12.0;

        this.peakHeigths = new double[peaks];
        this.peakWidths = new double[peaks];
        this.movementDirections = null; //cannot initialize without knowing the dimension of the problem
        this.peakPositions = new Vector[peaks];
        this.shiftVectors = new Vector[peaks];
    }

    public GeneralizedMovingPeaks(int frequency, int peaks, double widthSeverity, double heightSeverity, double shiftSeverity, double lambda) {
        this.gaussian = new GaussianDistribution();
        this.uniform = new UniformDistribution();

        this.frequency = frequency;
        this.peaks = peaks;
        this.widthSeverity = widthSeverity;
        this.heightSeverity = heightSeverity;
        this.shiftSeverity = shiftSeverity;
        this.lambda = lambda;
        this.minHeight = 30.0;
        this.maxHeight = 70.0;
        this.minWidth = 1.0;
        this.maxWidth = 12.0;

        this.peakHeigths = new double[peaks];
        this.peakWidths = new double[peaks];
        this.movementDirections = null; //cannot initialize without knowing the dimension of the problem
        this.peakPositions = new Vector[peaks];
        this.shiftVectors = new Vector[peaks];
    }

    @Override
    public Double apply(Vector input) {
        //this is silly, but there's no way of knowing the dimensions of the problem until this method is called...
        if (movementDirections == null) initializePeaks(input.size());

        //change environment once at each change interval
        int iteration = AbstractAlgorithm.get().getIterations();
        if (iteration % frequency == 0 && lastChange != iteration) {
            lastChange = iteration;
            changeEnvironment();

            //for (int p = 0; p < peaks; p++) System.out.println(iteration + " " + peakHeigths[p]);
        }

        //evaluate function
        double maximum = Double.MIN_VALUE;
        int dimensions = input.size();

        for (int p = 0; p < peaks; p++) {
            double thisPeak = 0.0;

            for (int i = 0; i < dimensions; i++) {
                thisPeak += Math.pow(input.doubleValueOf(i) - peakPositions[p].doubleValueOf(i), 2);
            }

            thisPeak = 1 + (peakWidths[p] * thisPeak);
            thisPeak = peakHeigths[p] / thisPeak;

            if (thisPeak > maximum) maximum = thisPeak;
        }

        return maximum;
    }

    /**
     * Changes the environment according to Branke's formal description of how
     * peak heights, widths and positions change at each change interval.
     */
    private void changeEnvironment() {
        //get problem domain boundaries
        Vector bounds = (Vector) DomainParser.parse(AbstractAlgorithm.get().getOptimisationProblem().getDomain().getDomainString());
        Vector tempPosition;

        updateShiftVectors();

        for (int p = 0; p < peaks; p++) {
            //change peak height
            double offset = (heightSeverity * gaussian.getRandomNumber());
            if (peakHeigths[p] + offset > maxHeight ||
                    peakHeigths[p] + offset < minHeight) {
                peakHeigths[p] -= offset;
            } else {
                peakHeigths[p] += offset;
            }

            //change peak width
            offset = (widthSeverity * gaussian.getRandomNumber());
            if (peakWidths[p] + offset > maxWidth ||
                    peakWidths[p] + offset < minWidth) {
                peakWidths[p] -= offset;
            } else {
                peakWidths[p] += offset;
            }

            //change peak location
            int dimensions = shiftVectors[0].size();

            Double[] shift = new Double[dimensions];
            for (int i = 0; i < dimensions; i++) {
                shift[i] = shiftVectors[p].get(i).doubleValue() * movementDirections[p][i];
            }

            tempPosition = peakPositions[p].plus(Vector.of(shift));

            //enforce boundary constraints
            for (int i = 0; i < dimensions; i++) {
                if (tempPosition.get(i).doubleValue() > bounds.boundsOf(i).getUpperBound() ||
                        tempPosition.get(i).doubleValue() < bounds.boundsOf(i).getLowerBound()) {
                    movementDirections[p][i] *= -1;
                    shift[i] *= -1;
                }
            }

            peakPositions[p] = peakPositions[p].plus(Vector.of(shift));
        }
    }

    /**
     * In Branke's original implementation, the initial positions of peaks are
     * predefined for 5 peaks in 5 dimensions. However, to allow for
     * generalization, this implementation draws peak positions from a uniform
     * distribution in each dimension.
     * <p/>
     * Peak heights and widths are initialized uniformly within the bounds
     * [minHeight, maxHeight] and [minWidth, maxWidth], respectively.
     * <p/>
     * In addition, all shiftVectors are initialized to [1.0, ..., 1.0],
     * and all movement directions are initialized to 1.
     *
     * @param dimensions The dimensions of the problem.
     */
    private void initializePeaks(int dimensions) {
        movementDirections = new int[peaks][dimensions];

        //get problem domain boundaries
        Vector bounds = (Vector) DomainParser.parse(AbstractAlgorithm.get().getOptimisationProblem().getDomain().getDomainString());

        Double[] oneVector = new Double[dimensions];
        for (int i = 0; i < dimensions; i++) {
            oneVector[i] = 1.0;
        }  // } What happened to Vector.fill?

        //initialize peaks and shift vectors
        Double[] position = new Double[dimensions];
        for (int p = 0; p < peaks; p++) {
            for (int i = 0; i < dimensions; i++) {
                position[i] = uniform.getRandomNumber(bounds.boundsOf(i).getLowerBound(), bounds.boundsOf(i).getUpperBound());
                movementDirections[p][i] = 1;
            }

            peakPositions[p] = Vector.of(position);
            peakHeigths[p] = uniform.getRandomNumber(minHeight, maxHeight);
            peakWidths[p] = uniform.getRandomNumber(minWidth, maxWidth);

            shiftVectors[p] = Vector.of(oneVector);
        }
    }

    /**
     * The movements of peaks are controlled by the shift vectors. The
     * <tt>changeSeverity</tt> controls the magnitude of the shifts, while
     * <tt>lambda</tt> controls how much a peak's change in location depends
     * on its previous move.
     */
    private void updateShiftVectors() {
        int dimensions = peakPositions[0].size();

        Vector.Builder vectorBuilder = Vector.newBuilder();
        for (int i = 0; i < dimensions; i++) {
            vectorBuilder.addWithin(0.0, new Bounds(-1.0, 1.0));
        }

        //get a vector with random direction and magnitude <tt>changeSeverity</tt>.
        Vector random = vectorBuilder.buildRandom().normalize().multiply(shiftSeverity);

        //compute new shift vectors
        Vector vector;
        for (int i = 0; i < peaks; i++) {
            vector = (random.multiply(1 - lambda)).plus(shiftVectors[i].multiply(lambda));
            shiftVectors[i] = vector.multiply(shiftSeverity).divide(vector.length());
        }
    }

    @Override
    public double getMaximum() {
        double max = Double.MIN_VALUE;

        for (int p = 0; p < peaks; p++) {
            if (peakHeigths[p] > max) {
                max = peakHeigths[p];
            }
        }

        return max;
    }

    // Getters and setters.... for use in the XML
    public int getFrequency() {
        return frequency;
    }

    public double getHeightSeverity() {
        return heightSeverity;
    }

    public double getLambda() {
        return lambda;
    }

    public double getMaxHeight() {
        return maxHeight;
    }

    public double getMaxWidth() {
        return maxWidth;
    }

    public double getMinHeight() {
        return minHeight;
    }

    public double getMinWidth() {
        return minWidth;
    }

    public int getPeaks() {
        return peaks;
    }

    public double getShiftSeverity() {
        return shiftSeverity;
    }

    public double getWidthSeverity() {
        return widthSeverity;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public void setHeightSeverity(double heightSeverity) {
        this.heightSeverity = heightSeverity;
    }

    public void setLambda(double lambda) {
        this.lambda = lambda;
    }

    public void setMaxHeight(double maxHeight) {
        this.maxHeight = maxHeight;
    }

    public void setMaxWidth(double maxWidth) {
        this.maxWidth = maxWidth;
    }

    public void setMinHeight(double minHeight) {
        this.minHeight = minHeight;
    }

    public void setMinWidth(double minWidth) {
        this.minWidth = minWidth;
    }

    public void setPeaks(int peaks) {
        this.peaks = peaks;
    }

    public void setShiftSeverity(double shiftSeverity) {
        this.shiftSeverity = shiftSeverity;
    }

    public void setWidthSeverity(double widthSeverity) {
        this.widthSeverity = widthSeverity;
    }
}
