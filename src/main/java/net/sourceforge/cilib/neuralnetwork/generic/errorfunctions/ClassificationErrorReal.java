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
package net.sourceforge.cilib.neuralnetwork.generic.errorfunctions;

import net.sourceforge.cilib.neuralnetwork.foundation.NNError;
import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.TypeList;

/**
 * @author Stefan
 *
 */
public class ClassificationErrorReal implements NNError {
    private static final long serialVersionUID = 8105183884502670240L;

    protected int numberPatternsCorrect = 0;
    protected int numberPatternsIncorrect = 0;
    protected double outputSensitivityThreshold;
    protected double percentageCorrect = 9999999;



    public ClassificationErrorReal() {
        //Default behaviour is that output - target < 0.1
        outputSensitivityThreshold = 0.2;
    }


    public void computeIteration(TypeList output, NNPattern input) {

        boolean isCorrect = true;

        for (int i = 0; i < output.size(); i++){

            if (Math.abs(((Real) output.get(i)).getReal() - input.getTarget().getReal(i)) > this.outputSensitivityThreshold) {
                isCorrect = false;
                break;
            }
        }

        if (isCorrect){
            this.numberPatternsCorrect++;
        }
        else this.numberPatternsIncorrect++;
    }


    public void finaliseError() {

        this.percentageCorrect = (double) this.numberPatternsCorrect / ((double) this.numberPatternsIncorrect + (double) this.numberPatternsCorrect) * 100;
        numberPatternsCorrect = 0;
        numberPatternsIncorrect = 0;
    }


    public void setValue(Object val){
        throw new UnsupportedOperationException("Setting value not supported as an operation");
    }


    public Double getValue() {
        return new Double(this.percentageCorrect);
    }


    public int compareTo(Fitness f) {

        if (!(f instanceof MSEErrorFunction)) {
            throw new IllegalArgumentException("Incorrect class instance passed");
        }

        return (Double.valueOf(this.percentageCorrect)).compareTo(((ClassificationErrorReal) f).getValue());
    }

    public NNError getClone(){
        ClassificationErrorReal tmp = new ClassificationErrorReal();
        tmp.setOutputSensitivityThreshold(this.outputSensitivityThreshold);
        tmp.percentageCorrect = this.percentageCorrect;
        tmp.numberPatternsCorrect = this.numberPatternsCorrect;
        tmp.numberPatternsIncorrect = this.numberPatternsIncorrect;
        return tmp;
    }


    public String getName() {
        return new String("Classification error for Real numbers");
    }



    public void setNoPatterns(int noPatterns) {
        //not callable via XML interface as information set by EvaluationStrategy
        //unsupported action, so do nothing.
    }



    public void setNoOutputs(int nr) {
        //unsupported action, so do nothing.
    }



    public double getOutputSensitivityThreshold() {
        return outputSensitivityThreshold;
    }



    public void setOutputSensitivityThreshold(double outputSensitivityThreshold) {
        this.outputSensitivityThreshold = outputSensitivityThreshold;
    }



    public int getNumberPatternsCorrect() {
        return numberPatternsCorrect;
    }



    public int getNumberPatternsIncorrect() {
        return numberPatternsIncorrect;
    }

    public String toString(){
        return new String(Integer.valueOf(this.numberPatternsCorrect).toString());
    }

    public void initialize() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    @Override
    public Fitness newInstance(Double value) {
        throw new UnsupportedOperationException("Not supported");
    }

}
