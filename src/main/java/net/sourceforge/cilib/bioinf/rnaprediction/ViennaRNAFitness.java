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


/**
 * @author marais
 * Uses the RNAeval program from the Vienna package to compute the Free Energy
 * of the structure.
 */
public class ViennaRNAFitness extends RNAFitness {
    private static final long serialVersionUID = -38000347027058908L;

    native float vfitness(String nuc, String brack);

    public ViennaRNAFitness() {
        nucleotides = NucleotideString.getInstance().getNucleotideString();

        firsttime = true;
        //nucleotides = StemGenerator.getInstance().getNucleotides();
        //System.out.println("A nuc string, construction!" + this.nucleotides);
        /*String [] cmds = {"RNAeval"};
        Process p;
        try {
            //System.out.println("Starting RNAEval..");
            p = Runtime.getRuntime().exec(cmds);
            System.out.println("Running:" + p.toString());
            out = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        } catch (IOException e) {
            System.out.println("Couldnt start a process in ViennaRNAFitness");
            e.printStackTrace();
        }*/
    }

    public ViennaRNAFitness(ViennaRNAFitness copy) {
    }

    public ViennaRNAFitness getClone() {
        return new ViennaRNAFitness(this);
    }

    /* (non-Javadoc)
     * @see net.sourceforge.cilib.BioInf.RNAFitness#getRNAFitness(net.sourceforge.cilib.Type.Types.Set)
     */
    @Override
    public Double getRNAFitness(RNAConformation stems) {
        if (firsttime) {
            nucleotides = NucleotideString.getInstance().getNucleotideString();
            firsttime = false;
        }
        //if (this.nucleotides == null)
        //    this.nucleotides = StemGenerator.getInstance().getNucleotides();
        Double energy = new Double(0.0);
        /*try {
            out.write(nucleotides+"\n");
            out.write(getRepresentation(stems)+"\n");
            out.newLine();
            out.flush();
            System.out.println("Wrote to outstream...");
            while (!in.ready()) {
                System.out.println("Not Ready...");
            }
            energy = Double.parseDouble(in.readLine());

            //energy = Double.parseDouble(in.read());
            System.out.println("Got Back: " + energy);
        } catch (IOException e) {
            System.out.println("Couldn't compute the energy value in ViennaRNAFitness.getRNAFitness");
            e.printStackTrace();
        }*/
        //TODO Memory optimisation (Change to use char array instead of String.)
        //Currently a new String object is constructed with every call.
        //System.out.println("Nucleotides length: "+nucleotides.length()+" representation length: "+stems.getRepresentation().length);
        energy = Float.valueOf(vfitness(nucleotides, new String(stems.getRepresentation()))).doubleValue();
        return energy;
    }

    /*private Process RNAeval;
    private BufferedWriter out;
    private BufferedReader in;*/
    boolean firsttime;
}
