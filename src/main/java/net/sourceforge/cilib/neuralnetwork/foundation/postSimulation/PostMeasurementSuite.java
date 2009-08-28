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
package net.sourceforge.cilib.neuralnetwork.foundation.postSimulation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.AlgorithmListener;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Type;

/**
 * TODO: Complete this javadoc.
 */
public class PostMeasurementSuite {

    ArrayList<Measurement> measures;
    String outputFile;

    public PostMeasurementSuite() {
        this.measures = new ArrayList<Measurement>();
        outputFile = null;
    }


    public void addMeasurement(Measurement m){
        this.measures.add(m);
    }


    public void setOutputFile(String outFile) {
        this.outputFile = outFile;
    }


    public void performMeasurement() throws IOException{

        for (int i = 0; i < measures.size(); i++){
            if (measures.get(i) instanceof AlgorithmListener){
                ((AlgorithmListener) measures.get(i)).algorithmFinished(null);
            }
        }

        BufferedWriter out = null;

        File f = new File(outputFile);

        if (!f.exists()){
            int column = 1;
            out = new BufferedWriter(new FileWriter(outputFile));
            for (int i = 0; i < this.measures.size(); i++){
                String description = measures.get(i).getClass().getName();
                description = description.substring(description.lastIndexOf(".") + 1);
                out.write("# " + String.valueOf(column + i) + " - " + description);
                out.newLine();
            }

        }
        else {
            out = new BufferedWriter(new FileWriter(outputFile, true));
        }


        for (int i = 0; i < this.measures.size(); i++){
            Type result = measures.get(i).getValue(AbstractAlgorithm.get());

            if (i <= this.measures.size()-2){
                out.write(result.toString() + ",");
            }
            else {
                out.write(result.toString());
            }

        }
        out.newLine();


        out.close();

    }

}
