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
package net.sourceforge.cilib.problem.mappingproblem;

import java.util.List;

import net.sourceforge.cilib.functions.continuous.FunctionDimensionMapping;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.problem.OptimisationProblemAdapter;
import net.sourceforge.cilib.problem.dataset.StringDataSetBuilder;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * TODO: Complete this javadoc.
 */
public class FunctionDimensionMappingProblem extends OptimisationProblemAdapter {
    private static final long serialVersionUID = -5419400002196415792L;

    private FunctionDimensionMapping function;
    private double[][] higherDimensionDistanceMatrix;

    public FunctionDimensionMappingProblem() {
        function = new FunctionDimensionMapping();
    }

    public FunctionDimensionMappingProblem(FunctionDimensionMappingProblem copy) {

    }

    public FunctionDimensionMappingProblem getClone() {
        return new FunctionDimensionMappingProblem(this);
    }


    @Override
    protected Fitness calculateFitness(Type solution) {
        //System.out.println(solution);
        if (higherDimensionDistanceMatrix == null) {
            intialiseMatrix();
        }

        //System.out.println(solution);
        Vector solutionVector = (Vector) solution;
    //    System.out.println("sil: " + solutionVector);
        function.setHigherDimensionDistanceMatrix(higherDimensionDistanceMatrix);

        return new MinimisationFitness(function.evaluate(solutionVector));
    }

    public DomainRegistry getDomain() {
        return function.getDomainRegistry();
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
        //System.out.println("intialiseMatrix()");
        DistanceMeasure measure = new EuclideanDistanceMeasure();
        StringDataSetBuilder builder = (StringDataSetBuilder) getDataSetBuilder();
        List<String> data = builder.getStrings();

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
        Vector v = new Vector();

        for (String s : parts) {
            v.add(new Real(Double.valueOf(s)));
        }

        return v;
    }

    public void setDataSetBuilder(StringDataSetBuilder builder) {
        super.setDataSetBuilder(builder);
    }

}
