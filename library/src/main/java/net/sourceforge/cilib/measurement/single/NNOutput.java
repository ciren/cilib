/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.io.CSVFileWriter;
import net.sourceforge.cilib.io.FileWriter;
import net.sourceforge.cilib.io.NNOutputReader;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.problem.nn.NNDataTrainingProblem;
import net.sourceforge.cilib.problem.nn.NNTrainingProblem;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.types.container.Vector.Builder;

/**
 * Calculates the MSE generalization error of the best solution of an algorithm
 * optimizing a {@link NNDataTrainingProblem}.
 */
public class NNOutput implements Measurement {

    private static final long serialVersionUID = -1014032196750640716L;
    private NNOutputReader outputReader;
    private FileWriter fileWriter = new CSVFileWriter();
    private String destinationURL;
    /**
     * {@inheritDoc }
     */
    @Override
    public Measurement getClone() {
        return this;
    }

    /**
     * Return file name; write output to a separate file.
     */
    @Override
    public Type getValue(Algorithm algorithm) {
        Vector solution = (Vector) algorithm.getBestSolution().getPosition();
        NNTrainingProblem problem = (NNTrainingProblem) algorithm.getOptimisationProblem();
        StandardPatternDataTable trainingSet = problem.getTrainingSet();
        StandardPatternDataTable generalizationSet = problem.getGeneralizationSet();
         StandardPatternDataTable validationSet = problem.getValidationSet();
        NeuralNetwork neuralNetwork = problem.getNeuralNetwork();
        neuralNetwork.setWeights(solution);

        Builder builder = Vector.newBuilder();
        for (StandardPattern pattern : trainingSet) {
            Vector outs = neuralNetwork.evaluatePattern(pattern);
            for(Numeric out : outs) {
                builder.add(out);
            }
        }
        for (StandardPattern pattern : generalizationSet) {
            Vector outs = neuralNetwork.evaluatePattern(pattern);
            for(Numeric out : outs) {
                builder.add(out);
            }
        }
        for (StandardPattern pattern : validationSet) {
            Vector outs = neuralNetwork.evaluatePattern(pattern);
            for(Numeric out : outs) {
                builder.add(out);
            }
        }
        return builder.build();

        /*
        StandardDataTable<Type> dataTable = new StandardDataTable<Type>();

        try {
            this.outputReader.open();
            while(outputReader.hasNextRow()) {
                dataTable.addRow(outputReader.nextRow());
            }
            this.outputReader.close();
            // Got the outputs; write to file now.
            this.fileWriter.setDestinationURL(this.destinationURL + "_" + algorithm.getIterations());
            this.fileWriter.open();
            this.fileWriter.write(dataTable);
            this.fileWriter.close();
        } catch (CIlibIOException ex) {
            ex.printStackTrace();
            return null;
        }
        return new StringType(fileWriter.getDestinationURL());

         */
    }

    public void setOutputReader(NNOutputReader outputReader) {
        this.outputReader = outputReader;
    }

    public void setFileWriter(FileWriter fileWriter) {
        this.fileWriter = fileWriter;
    }

    public void setDestinationURL(String destinationURL) {
        this.destinationURL = destinationURL;
    }
}
