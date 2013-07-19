/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import java.util.ArrayList;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.problem.nn.NNTrainingProblem;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Calculates the area under the ROC curve from generalisation set. One AUC
 * is calculated for each output neuron and returned as a Vector.
 * 
 * Threshold starts at the lowest output bound and is then moved to between
 * the next two successive outputs.
 * 
 * Area under the curve is approximate using the trapezoidal rule. 
 */
public class AreaUnderROCCurve implements Measurement<Vector> {

    private double lowerBound;
    private double upperBound;

    public AreaUnderROCCurve() {
        lowerBound = 0.0;
        upperBound = 1.0;
    }

    public AreaUnderROCCurve(AreaUnderROCCurve rhs) {
        lowerBound = rhs.lowerBound;
        upperBound = rhs.upperBound;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public AreaUnderROCCurve getClone() {
        return new AreaUnderROCCurve(this);
    }

    /**
     * Calculates the AUC measurment.
     * 
     * @param algorithm The optimisation algorithm with a NNTrainingProblem.
     * @return A Vector with the AUC for each NN output.
     */
    @Override
    public Vector getValue(Algorithm algorithm) {
        Vector solution = (Vector) algorithm.getBestSolution().getPosition();
        NNTrainingProblem problem = (NNTrainingProblem) algorithm.getOptimisationProblem();
        StandardPatternDataTable generalisationSet = problem.getGeneralisationSet();
        NeuralNetwork neuralNetwork = problem.getNeuralNetwork();
        neuralNetwork.setWeights(solution);

        //Arrange outputs and target values into ArrayLists.
        ArrayList<ArrayList<Real> > targets = new ArrayList<ArrayList<Real> >();
        ArrayList<ArrayList<Real> > outputs = new ArrayList<ArrayList<Real> >();
        //case of multiple outputs
        if (generalisationSet.getRow(0).getTarget() instanceof Vector) {
            int size = ((Vector) generalisationSet.getRow(0).getTarget()).size();
            for (int i = 0; i < size; ++i) {
                targets.add(new ArrayList<Real>());
                outputs.add(new ArrayList<Real>());
            }

            for (StandardPattern pattern : generalisationSet) {
                Vector target = (Vector) pattern.getTarget();
                Vector output = neuralNetwork.evaluatePattern(pattern);

                for (int curOutput = 0; curOutput < target.size(); ++curOutput) {
                    targets.get(curOutput).add((Real) target.get(curOutput));
                    outputs.get(curOutput).add((Real) output.get(curOutput));
                }
            }
        }
        //case of single output
        else {
            targets.add(new ArrayList<Real>());
            outputs.add(new ArrayList<Real>());

            for (StandardPattern pattern : generalisationSet) {
                Real target = (Real) pattern.getTarget();
                Vector output = neuralNetwork.evaluatePattern(pattern);

                targets.get(0).add(target);
                outputs.get(0).add((Real) output.get(0));
            }
        }

        //Calculate the Vector of AUC values
        Vector results = Vector.of();
        for (int curOutput = 0; curOutput < outputs.size(); ++curOutput) {
            results.add(Real.valueOf(areaUnderCurve(targets.get(curOutput), outputs.get(curOutput))));
        }

        return results;
    }

    /**
     * Calculates the AUC of one output.
     * 
     * @param targets The expected outputs. Each value is discetised to the
     *                  closest bound.
     * @param outputs The outputs of the NN.
     * @return The AUC in the range [0,1].
     */
    public double areaUnderCurve(ArrayList<Real> targets, ArrayList<Real> outputs) {
        
        double negatives = 0.0;
        double positives = 0.0;

        double centerBound = (upperBound + lowerBound)/2.0;

        //determine total positives and negatives
        for (Real curTarget : targets) {
            if (curTarget.doubleValue() > centerBound)
                positives += 1;
            else
                negatives += 1;
        }

        //plot ROC curve coordinates
        double threshold = lowerBound;
        ArrayList<Real> tpCoords = new ArrayList<Real>();
        ArrayList<Real> fpCoords = new ArrayList<Real>();

        //when all outputs are seen as true
        tpCoords.add(Real.valueOf(1.0));
        fpCoords.add(Real.valueOf(1.0));

        double newThresholdL;
        double newThresholdU;
        do {

            //calculate next threshold
            newThresholdL = upperBound;
            newThresholdU = upperBound;
            for (Real curOutput : outputs) {
                if (curOutput.doubleValue() >= threshold && curOutput.doubleValue() < newThresholdL) {
                    newThresholdL = curOutput.doubleValue();
                }
            }
            for (Real curOutput : outputs) {
                if (curOutput.doubleValue() > newThresholdL && curOutput.doubleValue() < newThresholdU) {
                    newThresholdU = curOutput.doubleValue();
                }
            }
            threshold = (newThresholdL + newThresholdU)/2.0;

            //calculate tp and fp for this threshold
            double tPositive = 0;
            double fPositive = 0;
            for (int curOutput = 0; curOutput < outputs.size(); ++curOutput) {
                if (targets.get(curOutput).doubleValue() > centerBound
                        && outputs.get(curOutput).doubleValue() > threshold) {
                    tPositive += 1;
                }
                else if (targets.get(curOutput).doubleValue() <= centerBound
                        && outputs.get(curOutput).doubleValue() > threshold) {
                    fPositive += 1;
                }
            }
            tpCoords.add(Real.valueOf(tPositive / positives));
            fpCoords.add(Real.valueOf(fPositive / negatives));

        } while (newThresholdU < upperBound);

        //when all outputs are seen as false
        tpCoords.add(Real.valueOf(0.0));
        fpCoords.add(Real.valueOf(0.0));

        //calculate area
        double area = 0.0;
        for (int curCoord = 1; curCoord < fpCoords.size(); ++curCoord) {
            area += ((tpCoords.get(curCoord-1).doubleValue() + tpCoords.get(curCoord).doubleValue())/2)
                    *(fpCoords.get(curCoord-1).doubleValue() - fpCoords.get(curCoord).doubleValue()); //fpCoords sorted in decending order
        }

        return area;
    }

    /**
     * Sets the lowest bound of the NN outputs (default 0).
     * 
     * @param lowerBound The value of the bound.
     */
    public void setLowerBound(double lowerBound) {
        this.lowerBound = lowerBound;
    }

    /**
     * Sets the highest bound of the NN outputs (default 1).
     * 
     * @param lowerBound The value of the bound.
     */
    public void setUpperBound(double upperBound) {
        this.upperBound = upperBound;
    }
}
