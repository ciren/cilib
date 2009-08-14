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
package net.sourceforge.cilib.bioinf.rnaprediction;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import net.sourceforge.cilib.algorithm.InitialisationException;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.pso.PSO;

/**
 * @author marais
 *
 */
public class RNAPSO extends PSO {
    private static final long serialVersionUID = 8034984869522637866L;

    /**
     * Create an instance of the {@linkplain RNAPSO}.
     */
    public RNAPSO() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public void performInitialisation() {
        if (getOptimisationProblem() == null) {
           throw new InitialisationException("No problem has been specified");
        }

        readDataSet();

        Topology<Particle> topology = getTopology();
        Iterable<? extends Entity> particles = this.initialisationStrategy.initialise(getOptimisationProblem());
        
        //Iterables.addAll(getTopology(), particles); // Use this instead?
        for (Entity particle : particles)
            topology.add((Particle) particle);
    }

    /**
     * Read the provided data set.
     */
    private void readDataSet() {
        StringBuilder nucs = new StringBuilder();
        ArrayList<Integer> struct = new ArrayList<Integer>();
        int[] intStruct;
        int length = 0;

        BufferedReader in;
        try {
            RNAOptimisationProblem p = (RNAOptimisationProblem) this.getOptimisationProblem();
            in = new BufferedReader(new InputStreamReader(p.getDataSetBuilder().getDataSet(0).getInputStream()));
            String line;
            //Read and discard first 4 lines
            in.readLine();
            in.readLine();
            in.readLine();
            in.readLine();

            line = in.readLine();

            while (line != null) {
                String [] tokens = line.split(" ");
                nucs.append(tokens[1]);
                struct.add(Integer.parseInt(tokens[2]));
                length++;
                line = in.readLine();
            }

            in.close();

            if (length < 1) {
                System.out.println("Error reading data input file");
                System.exit(1);
            }
            //convert arraylist into char array
            System.out.print(length+" ");
            System.out.println(nucs.toString());
            System.out.print(struct.size()+" ");
            System.out.println(struct.toString());

        }
        catch (FileNotFoundException f) {
            System.out.println("Couldn't find the input file.");
            System.exit(1);
        }
        catch (IOException e) {
            System.out.println("Couldn't read from the input file");
            System.exit(1);
        }

        intStruct = new int[struct.size()];
        for (int i = 0; i < struct.size(); i++) {
            intStruct[i] = struct.get(i);
        }

        //System.out.println("nucleotides: " + nucleotides);
        //StemGenerator.getInstance().setNucleotides(neucleotides);
        NucleotideString.getInstance().setNucleotideString(nucs.toString());
        //System.out.println(NucleotideString.getInstance().nucleotideString);
        NucleotideString.getInstance().setKnowStructure(intStruct);
        //System.out.println(NucleotideString.getInstance().knownStructure.toString());
        StemGenerator.getInstance().generateStems(NucleotideString.getInstance().getNucleotideString(), false);
    }
}
