/*
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
package net.sourceforge.cilib.functions.continuous.decorators;


import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.changestrategy.ChangeStrategy;
import net.sourceforge.cilib.problem.changestrategy.IterationBasedSingleChangeStrategy;
import net.sourceforge.cilib.type.parser.DomainParser;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.container.Matrix;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @author Julien Duhain
 *
 * Rotates a n-dimensional continuous function according to the pseudo-code found in:
 * <pre>
 * {@literal @}article{salomon1996reg,
 * title={{Re-evaluating genetic algorithm performance under coordinate rotation of benchmark functions. A survey of some theoretical and practical aspects of genetic algorithms}},
 * author={Salomon, R.},
 * journal={BioSystems},
 * volume={39},
 * number={3},
 * pages={263--278},
 * year={1996},
 * publisher={Elsevier}
 *}
 * </pre>
 *
 * The function rotates around the center of its domain (unless specified otherwise). The domain rotates
 * with the function so that the domain size stays constant.
 *
 * @param cycleLength specifies how many rotation it takes for the function to be back
 * at its original position.
 * @param rotatingFrequency specifies how many iterations take place between 2 rotations
 * of the function.
 *
 */
public class RotatingFunctionDecorator extends ContinuousFunction {
    private static final long serialVersionUID = 3107473364744861153L;
    private ContinuousFunction function;
    private int N;
    private double alpha=0;
    private Matrix matrix;

    private int cycleLength = 100;
    private int rotatingFrequency = 5;
    private double center = 0;
    private ChangeStrategy changeStrategy;

    public double getCenter() {
        return center;
    }

    public void setCenter(double center) {
        this.center = center;
    }

    public int getCycleLength() {
        return cycleLength;
    }

    public void setCycleLength(int cycleLength) {
        this.cycleLength = cycleLength;
    }

    public int getRotatingFrequency() {
        return rotatingFrequency;
    }

    public void setRotatingFrequency(int rotatingFrequency) {
        this.rotatingFrequency = rotatingFrequency;
        changeStrategy = new IterationBasedSingleChangeStrategy(rotatingFrequency);
    }

    private Matrix initMatrices(){
        Matrix.Builder matrixBuilder = Matrix.builder().dimensions(N, N);
        for(int i=0; i<N; i++){
            for(int j=0; j<N; j++){
                if(i==j){
                    matrixBuilder.valueAt(i, j, 1);
                }//if
                else{
                    matrixBuilder.valueAt(i, j, 0);
                }//else
            }//for
        }//for

        return matrixBuilder.build();
    }

    private Matrix localRotate(int i, int j){
        Matrix tmp = this.matrix.getClone();

        if (this.changeStrategy.shouldApply(null)){
            alpha += 2*Math.PI/cycleLength;
        }

        return tmp.rotate(alpha, i, j);
    }

    private Matrix createMatrix(){
        Matrix result = this.matrix.getClone();
        for(int i=1; i<N; i++){
            Matrix rotated = localRotate(0,i);
            result = result.times(rotated);
        }//for
        for(int i=1; i<N-1; i++){
            Matrix rotated = localRotate(i,N-1);
            result = result.multiply(rotated);
        }//for

        return result;
    }

    public RotatingFunctionDecorator() {
        setDomain("R");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RotatedFunctionDecorator getClone() {
        return new RotatedFunctionDecorator();
    }

    @Override
    public Double evaluate(Vector input) {
        Matrix result = createMatrix();
        Vector rotatedX = input.getClone();
        rotatedX.reset();

        for(int j = 0; j < input.getDimension(); j++) {
            for(int i = 0; i < input.getDimension(); i++) {
                double value = rotatedX.getReal(j) + (input.getReal(i)-center) * result.valueAt(i, j);
                rotatedX.setReal(j, value);
            }
            double rotatedValue=rotatedX.getReal(j)+center;
            rotatedX.setReal(j, rotatedValue);
        }

        return function.evaluate(rotatedX);
    }

    /**
     * @return the function
     */
    public ContinuousFunction getFunction() {
        return function;
    }

    /**
     * @param function the function to set
     */
    public void setFunction(ContinuousFunction function) {
        this.function = function;
        this.setDomain(function.getDomainRegistry().getDomainString());

        Vector structure = (Vector) DomainParser.parse(function.getDomainRegistry().getDomainString());
        Bounds bounds = structure.get(0).getBounds();
        double lowerLimit = bounds.getLowerBound();
        double upperLimit = bounds.getUpperBound();

        center = (upperLimit - lowerLimit) / 2 + lowerLimit;
        this.N = function.getDimension();
        this.matrix = initMatrices();
    }
}
