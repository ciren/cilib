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
package net.sourceforge.cilib.neuralnetwork.generic.datacontainers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.StringTokenizer;

import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * TODO: Complete this javadoc.
 *
 */
//Draft class - not tested yet.
public class CrossValidationStrategy implements DataDistributionStrategy {

    protected BufferedReader inputReader;
    protected String file;
    protected Random shuffleRandomizer;

    protected int noInputs;

    //cross validation number of sets = K
    protected int k;
    protected int kOffset;
    protected int percentTrain;
    protected int percentGen;
    protected int percentCan;


    public CrossValidationStrategy() {

        this.noInputs = 999;
        this.k = 99999;
        this.kOffset = 999999;
        this.file= null;
        //Default choice, can be set explicitely.
        this.shuffleRandomizer = new Random(System.currentTimeMillis());

    }


    public void initialize(){

        if (this.file == null){
            throw new IllegalArgumentException("Required filename object was null during initialization");
        }

        if (this.noInputs == 999){
            throw new IllegalArgumentException("Required noInputs object was null during initialization");
        }

        if (this.percentCan + this.percentTrain + this.percentGen != 100){
            throw new IllegalArgumentException("Percentages for data sets do not add up to 100");
        }

        if (this.k == 99999) {
            throw new IllegalArgumentException("K-value is not set.");
        }

        if (this.kOffset >= this.k) {
            throw new IllegalArgumentException("K-offset is too large or not set correctly.");
        }


    }




    public void populateData(ArrayList<NNPattern> dc,
                             ArrayList<NNPattern> dt,
                             ArrayList<NNPattern> dg,
                             ArrayList<NNPattern> dv) {

        try {
            inputReader = new BufferedReader(new FileReader(file));
        }
        catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Input data file not found...");
        }

        try {
            while(inputReader.ready()) {
                String line = inputReader.readLine();

                StringTokenizer token = new StringTokenizer(line, " ");

                if(token.countTokens() <= this.noInputs)
                    throw new IllegalStateException("IOException: Record lengths dont match or too many spaces in line.");

                Vector input = new Vector();
                Vector target = new Vector();

                for (int i = 0; i < noInputs; i++){
                    input.add(new Real(Double.parseDouble(token.nextToken())));
                }

                while(token.hasMoreTokens()){
                    target.add(new Real(Double.parseDouble(token.nextToken())));
                }
                StandardPattern tmp = new StandardPattern();
                tmp.setInput(input);
                tmp.setTarget(target);
                dc.add(tmp);

            }//end while
        }
        catch (IOException e){
            throw new IllegalStateException("IOException: Data not in correct format");
        }


        //==================================================================================
        //=   Distribute the patterns into Dc, Dt, Dg and Dv usinf K-fold crossvalidation  =
        //==================================================================================

        int kValidationSize = (int) Math.floor(dc.size() / this.k);
        int trainsetSize = (int) Math.floor((dc.size() - kValidationSize) * this.percentTrain / 100);
        int gensetSize = (int) Math.floor((dc.size() - kValidationSize) * this.percentGen / 100);

        if (dc.size() % kValidationSize > 0){
            throw new IllegalArgumentException("Invalid value for K - total patterns mod K is not zero.");
        }

        Collections.shuffle(dc, this.shuffleRandomizer);

        //Validation set = Kth set of patterns
        for (int i = 0; i < kValidationSize; i++){
            dv.add(dc.remove(this.kOffset * kValidationSize));
        }

        //Generalization set
        for (int i = 0; i < gensetSize; i++){
            dg.add(dc.remove(0));
        }

        //Training set
        for (int i = 0; i < trainsetSize; i++){
            dt.add(dc.remove(0));
        }
    }


    public void setFile(String file) {
        this.file = file;
    }


    public void setK(int k) {
        this.k = k;
    }


    public void setKoffset(int koffset) {
        kOffset = koffset;
    }


    public void setNoInputs(int noInputs) {
        this.noInputs = noInputs;
    }


    public void setPercentCan(int percentCan) {
        this.percentCan = percentCan;
    }


    public void setPercentGen(int percentGen) {
        this.percentGen = percentGen;
    }


    public void setPercentTrain(int percentTrain) {
        this.percentTrain = percentTrain;
    }


    public void setShuffleRandomizer(Random shuffleRandomizer) {
        this.shuffleRandomizer = shuffleRandomizer;
    }



}
