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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkController;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkDataIterator;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkProblem;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkTopology;
import net.sourceforge.cilib.neuralnetwork.generic.datacontainers.GenericData;
import net.sourceforge.cilib.neuralnetwork.generic.datacontainers.RandomDistributionStrategy;
import net.sourceforge.cilib.type.types.StringType;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.TypeList;

/**
 * Measurement to determine the output of a Neural Network.
 */
public class NNOutput implements Measurement {
    private static final long serialVersionUID = -3695723118431143860L;
    private String inputFile;
    private String outputFile;
    private int noInputs;
    private NeuralNetworkTopology topology;
    private BufferedWriter out;

    public NNOutput() {
        this.inputFile = null;
        this.outputFile = null;
    }

    public NNOutput(NNOutput rhs) {
        throw new UnsupportedOperationException("public NNOutput(NNOutput rhs)");
    }

    public NNOutput getClone() {
        return new NNOutput(this);
    }

    public String getDomain() {
        return "T";
    }

    public Type getValue(Algorithm algorithm) {
        this.topology = ((NeuralNetworkProblem) ((NeuralNetworkController) algorithm).getOptimisationProblem()).getEvaluationStrategy().getTopology();

        GenericData data = new GenericData();
        RandomDistributionStrategy distributor = new RandomDistributionStrategy();
        distributor.setFile(this.inputFile);
        distributor.setNoInputs(this.noInputs);
        distributor.setPercentCan(1000);
        data.setDistributor(distributor);
        data.initialize();

        int iter = AbstractAlgorithm.get().getIterations();
        try {
            out = new BufferedWriter(new FileWriter(this.outputFile + "_" + String.valueOf(iter) + ".txt"));
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }


        NeuralNetworkDataIterator iteratorDc = data.getCandidateSetIterator();
        iteratorDc.reset();

        while(iteratorDc.hasMore()){
            TypeList outputDg = topology.evaluate(iteratorDc.value());

            try {
                out.write(iteratorDc.value().getInput().toString() + " " + outputDg.toString());
                out.newLine();
            }
            catch (IOException e) {
                e.printStackTrace();
                throw new IllegalStateException("Problem writing measurement to file...");
            }


            iteratorDc.next();
        }

        try {
            out.flush();
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Problem writing measurement to file...");
        }

        return new StringType(this.outputFile + "_" + String.valueOf(iter) + ".txt");
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public void setNoInputs(int noInputs) {
        this.noInputs = noInputs;
    }
}
