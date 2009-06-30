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
 *
 * @author mneethling
 * This class computes the Free Energy (dG) of an RNA conformation by calling
 * an external program (efn2). efn2 is the enrgy calculation function supplied
 * with mfold from Michael Zuker.
 *
 */
public class Efn2RNAFitness extends RNAFitness {
    private static final long serialVersionUID = 328787448081439602L;

    public Efn2RNAFitness() {
        nucleotides = NucleotideString.getInstance().getNucleotideString();
    }

    public Efn2RNAFitness(Efn2RNAFitness copy) {
        // @TODO: complete this
    }

    public Efn2RNAFitness getClone() {
        return new Efn2RNAFitness(this);
    }

    @Override
    public Double getRNAFitness(RNAConformation stems) {
        String outputString = toCt(stems);
        System.out.println("Conf ");
        System.out.println(outputString);
        System.out.println("\n\n");

        return new Double(10.0);
    }

    /**
     *
     * @return a string containing the conformation in CT format.
     */
    private String toCt(RNAConformation stems) {
        int [] confArray = new int[131]; //!!!
        //clear array
        for (int i = 0; i < confArray.length; i++) {
            confArray[i] = 0;
        }
        //fill in the confArray.
//        for (RNAStem stem : stems) {
//            for (int l = 0; l < stem.getLength(); l++) {
            // FIXME This line cant be right?
                //confArray[stem.getP5_index()+l] = stem.getP3_index()-l;
//            }
//        }
    //    String line = "";
        StringBuilder outString = new StringBuilder();
        outString.append(nucleotides.length());
        outString.append("\n");
        for (int i = 0; i < nucleotides.length()-1; i++) {
            outString.append("\t");
            outString.append(i+1);    //1
            outString.append((" "));
            outString.append(nucleotides.charAt(i));    //2
            outString.append("\t");
            outString.append(i);    //3
            outString.append("\t");
            outString.append(i+2);    //4
            outString.append("\t");
            outString.append(confArray[i]);    //5
            outString.append("\t");
            outString.append(i+1);    //6
            outString.append("\n");
        }
        //add last line
        outString.append("\t");
        outString.append(nucleotides.length());    //1
        outString.append((" "));
        outString.append(nucleotides.charAt(nucleotides.length()-1));    //2
        outString.append("\t");
        outString.append(nucleotides.length()-1);    //3
        outString.append("\t");
        outString.append("0");    //4
        outString.append("\t");
        outString.append(confArray[nucleotides.length()-1]);    //5
        outString.append("\t");
        outString.append(nucleotides.length());    //6
        outString.append("\n");
        outString.append("@");

        return outString.toString();
    }

}
