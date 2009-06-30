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
package net.sourceforge.cilib.neuralnetwork.foundation.measurements;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkData;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkDataIterator;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkTopology;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.StringType;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.TypeList;

/**
 * TODO: Fill in some javadoc.
 */
public class AreaUnderROC implements Measurement{
    private static final long serialVersionUID = -8959256964785840633L;
    NeuralNetworkTopology topology;
    NeuralNetworkData data;

    int[][] confusionMatrix;
    int[] classCount;
    int matrixSize;
    double threshold;


    public AreaUnderROC() {
        this.topology = null;
        this.data = null;
        this.threshold = 0.5;
        this.confusionMatrix = null;
    }

    public AreaUnderROC(AreaUnderROC rhs) {
//        super(rhs);
        throw new UnsupportedOperationException("public AreaUnderROC(AreaUnderROC rhs)");
    }

    public AreaUnderROC getClone() {
        return new AreaUnderROC(this);
    }

    public Type getValue(Algorithm algorithm) {

        NeuralNetworkDataIterator iterDv = data.getValidationSetIterator();
        System.out.println("Dv size = " + iterDv.size());

        this.matrixSize = iterDv.value().getTargetLength();
        this.confusionMatrix = new int[matrixSize][matrixSize];
        classCount = new int[matrixSize];


        if (this.matrixSize == 0){
            System.out.println("ERROR - NO DATA IN Dv....... ");
        }
        else {

        //iterate over Dv
        while(iterDv.hasMore()){

            //Determine winning output, determine if misclassification
            NNPattern p = iterDv.value();
            TypeList output = this.topology.evaluate(p);

            int winningOutput = 99999;
            double winningOutputValue = 0.0;

            for (int i = 0; i < output.size(); i++){
                if((((Real) output.get(i)).getReal() >= this.threshold) && (((Real) output.get(i)).getReal() > winningOutputValue)) {
                    winningOutput = i;
                    winningOutputValue = ((Real) output.get(i)).getReal();
                }
            }

            int winningClass = 99999;
            int winningClassCount = 0;

            //look for the class and increment count
            for (int i = 0; i < p.getTargetLength(); i++){
                if (((Real) p.getTarget().get(i)).getReal() >= this.threshold){
                    winningClass = i;
                    winningClassCount++;
                }
            }

            //determine if there is a problem in target
            if (winningClassCount != 1){
                throw new IllegalStateException("Classification pattern contains more than one target class, or is zero");
            }


            //If not a misclassification, update confusion matrix
            if (!(winningOutput == 99999)){
                this.confusionMatrix[winningClass][winningOutput]++;
            }

            //update class count
            this.classCount[winningClass]++;

            iterDv.next();
        }


        //Calculate total AUC for Dv
        double totalAUC = 0.0;

        //for each {Ci, Cj} class combination
        for (int ci = 0; ci < matrixSize - 1; ci++){

            for (int cj = ci+1; cj < matrixSize; cj++){
                totalAUC += this.calculateAUC(ci, cj);
            }
        }

        System.out.println("Confusion Matrix: ");
        for (int i = 0; i < matrixSize; i++){
            for (int j = 0; j < matrixSize; j++){
                System.out.print(this.confusionMatrix[j][i] + "\t");
            }
            System.out.println();
        }

        System.out.println("Class Count:");
        for (int i = 0; i < matrixSize; i++){
            System.out.print(this.classCount[i] + "\t");
        }

        //finalise AUC.
        totalAUC = (totalAUC * 2) / (matrixSize * (matrixSize - 1));

        //Build measurement
        String countString = new String();
        countString = "(" + String.valueOf(this.classCount[0]);
        for (int i = 1; i < matrixSize; i++){
            countString += "," + String.valueOf(this.classCount[i]);
        }
        countString += ")";

        String matrix = new String();
        matrix += "{";
        for (int i = 0; i < matrixSize; i++){
            matrix += "(";
            for (int j = 0; j < matrixSize; j++){
                matrix += String.valueOf(confusionMatrix[i][j]);
                if (j < matrixSize - 1)
                    matrix += ",";
            }
            matrix += ")";

        }
        matrix += "}";


        double accuracyVal = 0;
        double totalPats = 0;
        for (int i = 0; i < this.matrixSize; i++){
            accuracyVal += this.confusionMatrix[i][i];
            totalPats += this.classCount[i];
        }
        String accuracy = String.valueOf(accuracyVal / totalPats);

        TypeList v = new TypeList();
        v.add(new Real(totalAUC));
        v.add(new StringType(countString));
        v.add(new StringType(matrix));
        v.add(new StringType(accuracy));

        return v;

        }
        return null;
    }

    private double calculateAUC(int c1, int c2) {

        /*int c1Total = this.classCount[c1], c2Total = this.classCount[c2];

        for (int i = 0; i < this.confusionMatrix[c1].length; i++){
            c1Total += this.confusionMatrix[c1][i];
            c2Total += this.confusionMatrix[c2][i];
        }
        */

        int c1Total = this.classCount[c1];
        int c2Total = this.classCount[c2];

        if ((c1Total == 0) || (c2Total == 0)){
            System.out.println("AUC error - class empty, returning performance of 'random' = 0.5");
            return 0.5;
        }

        //calculate TP and FP rates
        double c1RateTP = (double) this.confusionMatrix[c1][c1] / (double) c1Total;
        double c2RateFP = (double) this.confusionMatrix[c2][c1] / (double) c2Total;

        //calculate AUC approximation for {Ci, Cj} classes, add to AUCtotal
        double area = 0.0;

        // 1. triangular area between (0,0) and (c1RateTP, c2RateFP)
        area = 0.5 * (c1RateTP * c2RateFP);

        // 2. trapezoidal area between (c1RateTP, c2RateFP) and (1,1)
        area += (c1RateTP + 1.0)*0.5 * (1.0 - c2RateFP);

        System.out.println("ROC point for classes " + c1 + " and " + c2 + " > FP = " + c2RateFP + ", TP = " + c1RateTP);
        System.out.println("AUC = " + area);


        return area;

    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public String getDomain() {
        return "T";
    }

    public void setData(NeuralNetworkData data) {
        this.data = data;
    }

    public void setTopology(NeuralNetworkTopology topology) {
        this.topology = topology;
    }
}
