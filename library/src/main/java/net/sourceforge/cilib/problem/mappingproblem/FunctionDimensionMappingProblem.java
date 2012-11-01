/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.mappingproblem;

import java.util.List;
import net.sourceforge.cilib.functions.continuous.FunctionDimensionMapping;
import net.sourceforge.cilib.problem.AbstractProblem;
import net.sourceforge.cilib.problem.dataset.StringDataSetBuilder;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * TODO: Complete this javadoc.
 * TODO: Maybe this should be a function, not a problem;
 */
public class FunctionDimensionMappingProblem extends AbstractProblem {

    private static final long serialVersionUID = -5419400002196415792L;

    private StringDataSetBuilder dataSetBuilder;
    private FunctionDimensionMapping function;
    private double[][] higherDimensionDistanceMatrix;

    public FunctionDimensionMappingProblem() {
        function = new FunctionDimensionMapping();
    }

    public FunctionDimensionMappingProblem(FunctionDimensionMappingProblem copy) {
        super(copy);
        this.function = copy.function;
        this.higherDimensionDistanceMatrix = copy.higherDimensionDistanceMatrix;
        if (copy.dataSetBuilder != null) {
            this.dataSetBuilder = copy.dataSetBuilder;
        }
    }

    public FunctionDimensionMappingProblem getClone() {
        return new FunctionDimensionMappingProblem(this);
    }

    @Override
    protected Fitness calculateFitness(Type solution) {
        if (higherDimensionDistanceMatrix == null) {
            intialiseMatrix();
        }

        Vector solutionVector = (Vector) solution;
        function.setHigherDimensionDistanceMatrix(higherDimensionDistanceMatrix);

        return objective.evaluate(function.apply(solutionVector));
    }

    /**
     * @return Returns the function.
     */
    public FunctionDimensionMapping getFunction() {
        return function;
    }

    /**
     * @param function The function to set.
     */
    public void setFunction(FunctionDimensionMapping function) {
        this.function = function;
    }

    private void intialiseMatrix() {
        DistanceMeasure measure = new EuclideanDistanceMeasure();
        List<String> data = dataSetBuilder.getStrings();

        higherDimensionDistanceMatrix = new double[data.size()][data.size()];

        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.size(); j++) {
                Vector v1 = vectorise(data.get(i).split(" "));
                Vector v2 = vectorise(data.get(j).split(" "));

                higherDimensionDistanceMatrix[i][j] = measure.distance(v1, v2);
            }
        }

        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.size(); j++) {
                System.out.println(higherDimensionDistanceMatrix[i][j]);
            }
        }
    }

    private Vector vectorise(String [] parts) {
        Vector v = Vector.of();

        for (String s : parts) {
            v.add(Real.valueOf(Double.valueOf(s)));
        }

        return v;
    }

    public StringDataSetBuilder getDataSetBuilder() {
        return this.dataSetBuilder;
    }

    public void setDataSetBuilder(StringDataSetBuilder dsb) {
        this.dataSetBuilder = dsb;
    }
}
