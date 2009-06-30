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

import net.sourceforge.cilib.neuralnetwork.foundation.Initializable;
import net.sourceforge.cilib.neuralnetwork.foundation.NNError;
import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkData;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.TypeList;

/**
 * @author stefanv
 *
 */
public class MSEErrorFunction implements NNError, Initializable {
    private static final long serialVersionUID = 2782200795898945699L;

    protected double value = 0;
    protected double computationData = 0;
    protected int noOutputs = 0;
    protected int noPatterns = 0;


    public MSEErrorFunction() {
        this.value = 0;
        this.computationData = 0;
        noOutputs = 0;
        noPatterns = 0;
    }


    public MSEErrorFunction(int noOutputs, int noPatterns, double value) {

        //this constructor is used by getClone() to ensure fewer function calls are made during cloning, as NNErrors are cloned often.
        this.noOutputs = noOutputs;
        this.noPatterns = noPatterns;
        this.computationData = 0;
        this.value = value;
    }




    public void initialize(){
        if (this.noOutputs <= 0) {
            throw new IllegalArgumentException("Incorrect noOutputs variable set for class");
        }

        if (this.noPatterns < 0) {
            throw new IllegalArgumentException("Negative noPatterns variable set for class");
        }

    }


    public Double getValue() {
        if (computationData == 0){
            return new Double(value);
        }
        else throw new IllegalStateException("Error not finalised by finaliseError() method.  No value to return");
    }


    public int compareTo(Fitness arg0) {

        if (!(arg0 instanceof MSEErrorFunction)) {
            throw new IllegalArgumentException("Incorrect class instance passed");
        }

        if (computationData == 0){
            return this.getValue().compareTo(((MSEErrorFunction) arg0).getValue());
        }
        else throw new IllegalStateException("Error not finalised by finaliseError() method.  No value to compare");

    }


    public void computeIteration(TypeList output, NNPattern input) {

        if (input.getTargetLength() != output.size()){
            throw new IllegalArgumentException("Output and target lenghts don't match");
        }
        else {

            for (int i = 0; i < output.size(); i++){
                this.computationData += Math.pow(((Real) input.getTarget().get(i)).getReal() -
                        ((Real) output.get(i)).getReal(), 2);
            }

        }
    }


    public void finaliseError() {

        if (this.noOutputs == 0){
            throw new IllegalStateException("noOutputs is zero - division by zero");
        }

        if (this.noPatterns == 0){
            throw new IllegalStateException("noPatterns is zero - division by zero");
        }

        if (this.computationData != 0) {
            this.value = computationData / ((double) noOutputs * (double) noPatterns);
        }
        computationData = 0;
    }


    public NNError getClone(){
        MSEErrorFunction tmp = new MSEErrorFunction(this.noOutputs, this.noPatterns, this.value);
        tmp.computationData = this.computationData;
        return tmp;
    }


    public int getNoOutputs() {
        return noOutputs;
    }

    public void setNoOutputs(int noOutputs) {

        this.noOutputs = noOutputs;
    }

    public int getNoPatterns() {
        return noPatterns;
    }

    public void setNoPatterns(int noPatterns) {
        //not callable via XML interface as information set by EvaluationStrategy initialize()
        this.noPatterns = noPatterns;
        if (this.noPatterns < 0){
            throw new IllegalArgumentException("Negative noPatterns variable not set for class");
        }
    }


    public void setValue(Object val) {
        value = ((Double) val).doubleValue();
        computationData = 0;
    }

    public String getName() {
        return new String("MSE Error Function");
    }


    public NeuralNetworkData getData() {
        throw new IllegalAccessError("Illegal operation on class - obtain data object from EvaluationStrategy object instead");
    }

    public String toString(){
        return new String(Double.valueOf(this.value).toString());
    }

    @Override
    public Fitness newInstance(Double value) {
        MSEErrorFunction function = new MSEErrorFunction();
        function.setValue(value);
        return function;
    }

}
