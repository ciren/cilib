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
 */
public class RandomDistributionStrategy implements DataDistributionStrategy {

    protected BufferedReader inputReader = null;
    protected String file;
    protected Random randomizer = null;

    protected int noInputs;

    protected int percentTrain;
    protected int percentGen;
    protected int percentVal;
    protected int percentCan;



    public RandomDistributionStrategy() {

        this.noInputs = 999;

        this.percentGen = 0;
        this.percentTrain = 0;
        this.percentVal = 0;
        this.percentCan = 0;

        this.file= null;
        //Default choice, can be set explicitely.
        this.randomizer = new Random(System.currentTimeMillis());
    }


    public void initialize(){

        if (this.file == null){
            throw new IllegalArgumentException("Required filename object was null during initialization");
        }

        if (this.noInputs == 999){
            throw new IllegalArgumentException("Required noInputs object was null during initialization");
        }

        if (this.percentCan + this.percentTrain + this.percentGen + this.percentVal != 100){
            throw new IllegalArgumentException("Percentages for data sets do not add up to 100.");
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


        //======================================================
        //=   Distribute the patterns into Dc, Dt, Dg and Dv   =
        //======================================================

        int totalPatterns = dc.size();

        int trainsetSize = (int) Math.floor(totalPatterns * this.percentTrain / 100);
        int gensetSize = (int) Math.floor(totalPatterns * this.percentGen / 100);
        int valsetSize = (int) Math.floor(totalPatterns * this.percentVal / 100);

        Collections.shuffle(dc, this.randomizer);

        //initiate val set randomly from Candidate set for valsetSize patterns
        for (int t = 0; t < valsetSize; t++){
            int target = 0;
            dv.add(dc.remove(target));
        }

        //initiate training set randomly from Candidate set for trainsetSize patterns
        for (int t = 0; t < trainsetSize; t++){
            int target = 0;
            dt.add(dc.remove(target));
        }

        //initiate gen set randomly from Candidate set for trainsetSize patterns
        for (int t = 0; t < gensetSize; t++){
            int target = 0;
            dg.add(dc.remove(target));
        }

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


    public void setPercentVal(int percentVal) {
        this.percentVal = percentVal;
    }


    public void setFile(String file) {
        this.file = file;
    }


    public void setNoInputs(int noInputs) {
        this.noInputs = noInputs;
    }

    public void setPatternRandomizerSeed(long seed){
        this.randomizer.setSeed(seed);
    }



}
