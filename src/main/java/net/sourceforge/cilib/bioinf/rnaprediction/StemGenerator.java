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

import java.util.ArrayList;


/**
 * @author mneethling
 *
 */

public final class StemGenerator {

    private static StemGenerator instance;
    private ArrayList<RNAStem> allStems = null;
    private PivotString pOn;
    private PivotString pOff;
    @SuppressWarnings("unused")
    private String nucleotides;
    private final int minStemLength = 3;

    private StemGenerator() {
        //System.out.println("StemGenerator Constructed!");
    }

    public static StemGenerator getInstance() {
        if (instance == null)
            instance = new StemGenerator();

        return instance;
    }

    public ArrayList<RNAStem> generateStems(String nucleotides, Boolean generateSubstems) {
        if (allStems != null)
            return allStems;
        System.out.println("StemGenerator.generateStems called!");
        this.nucleotides = new String(nucleotides);
        allStems = new ArrayList<RNAStem>();
        pOn = new PivotString(nucleotides, true);
        pOff = new PivotString(nucleotides, false);
        @SuppressWarnings("unused") int index, count;
        while (pOn.hasNext()) { //for each lined-up configuration
            pOn.next();
            //System.out.println(pOn.toString());
            //System.out.println();
            for (int position = 0; position < pOn.length(); position++) {
                if (pOn.canBind(position)) {
//                    then this is the start of a new stem. Count the consecutive bindings
                    index = position;
                    RNAStem tempstem = new RNAStem();
                    while (pOn.canBind(index)) {
                        tempstem.add(new NucleotidePair(pOn.get5prime(index)+1, pOn.get3prime(index)+1));
                        if (generateSubstems && tempstem.size()>=minStemLength) {
                            allStems.add((RNAStem) tempstem.getClone());
                            //System.out.println("Stem added");
                            //System.out.println(tempstem);
                            //System.out.println();
                            //System.out.println("Stem added: " + (pOn.get5prime(position)+1)+"-"+(pOn.get3prime(index)+1)+" length:"+tempstem.size());
                        }
                        index++;
                    }
                    if (!generateSubstems && tempstem.size()>=minStemLength) {
                        allStems.add(tempstem);
//                        move position to end of this stem to continue;
                        position += tempstem.size();
                        //System.out.println("Stem added");
                        //System.out.println(tempstem);
                        //System.out.println();
                        tempstem = null;
                    }
                }
            }
            //System.out.println();

            /*
            //System.out.println("\n"+pOn);
            for (int position = 0; position < pOn.length(); position++){
                if (pOn.canBind(position)) {
                    //then this is the start of a new stem. Count the consecutive bindings
                    index = position;
                    //p3 = ds.get3Prime(p5);
                    count = 0;
                    while (pOn.canBind(index)) {
                        //System.out.println("p5: "+p5+" nucs: "+ds.getNucleotide(p5)+" "+ds.getComplimentaryNucleotide(p5));
                        //Note: change this to 'count >= 1' to include stems which is only 1 contact long
                        count++;
                        if (count >= 3) { //>= 2) { // && count < 2) {
                //            allStems.add(new RNAStem(pOn.get5prime(index),pOn.get3prime(index),count));
                //            System.out.println("Found one! 5':"+pOn.get5prime(index)+" 3':"+pOn.get3prime(index)+" count:"+count+"\n");
                        }
                        index++;
                    }
                }
            }*/
        }

        while (pOff.hasNext()) { //for each lined-up configuration
            pOff.next();
            //System.out.println(pOff.toString());
            for (int position = 0; position < pOff.length(); position++) {
                if (pOff.canBind(position)) {
//                    then this is the start of a new stem. Count the consecutive bindings
                    index = position;
                    RNAStem tempstem = new RNAStem();
                    while (pOff.canBind(index)) {
                        tempstem.add(new NucleotidePair(pOff.get5prime(index)+1, pOff.get3prime(index)+1));
                        if (generateSubstems && tempstem.size()>=minStemLength) {
                            allStems.add((RNAStem) tempstem.getClone());
                            //System.out.println("Stem added");
                            //System.out.println(tempstem);
                            //System.out.println();
                        }
                        index++;
                    }
                    if (!generateSubstems && tempstem.size()>=minStemLength) {
                        allStems.add(tempstem);
                        //move position to end of this stem to continue;
                        position += tempstem.size();
                        //System.out.println("Stem added");
                        //System.out.println(tempstem);
                        //System.out.println();
                        tempstem = null;
                    }
                }
            }
            //System.out.println();


            /*
            //System.out.println("\n"+pOff);
            for (int position = 0; position < pOff.length(); position++){
                if (pOff.canBind(position)) {
                    //then this is the start of a new stem. Count the consecutive bindings
                    index = position;
                    //p3 = ds.get3Prime(p5);
                    count = 0;
                    while (pOff.canBind(index)) {
                        //System.out.println("p5: "+p5+" nucs: "+ds.getNucleotide(p5)+" "+ds.getComplimentaryNucleotide(p5));
                        //Note: change this to 'count >= 1' to include stems which is only 1 contact long
                        count++;
                        if (count >= 3) { //>= 2) { // && count < 2) {
                            allStems.add(new RNAStem(pOff.get5prime(index),pOff.get3prime(index),count));
                //            System.out.println("Found one! 5':"+pOff.get5prime(index)+" 3':"+pOff.get3prime(index)+" count:"+count+"\n");
                        }
                        index++;
                    }
                }
            }*/
        }
/*        int i, p5, p3, count, p3Start;
        for (i = 1; i <= nucleotides.length(); i++) {
            for (int j = 1; j <= i; j++) {
                p5 = j;
                p3 = nucleotides.length()-j;
//                System.out.println(p3);
                p5Start = p5;
                p3Start = p3;
                count = 0;
                while (p5 < nucleotides.length() && p3 >= 0 && canBind(nucleotides.charAt(p5),nucleotides.charAt(p3)) && ((p3 - p5) > 3)) {
                    count++;
                    p5++;
                    p3--;
                }
                //Note: change this to 'count >= 1' to include stems which is only 1 contact long
                if (count > 1) {
                    allStems.add(new RNAStem(p5Start,p3Start,count));
                }
            }
        }*/
        //System.out.println("About to set conflicting stems");
        //Initialise all the stems' conflictingStems list
        //for (RNAStem stem : allStems) {
        //    stem.setConflictingStems(getConflictingStems(stem));
        //}

        //Assign indexes to stems;
        index = 0;
        for (RNAStem st : allStems) {
            st.setIndex(index);
            index++;
        }

        System.out.println("Number of stems generated: " + allStems.size());
        System.out.println("Finished Generating stems. Setting conflicting stems");
        System.out.println();
        StemConflictTable.getInstance().create(allStems.size());
        for (RNAStem st : allStems) {
            System.out.print("\rSetting conflicts for: "+st.getIndex());
            for (RNAStem st2 : allStems) {
                if (st2.slowConflictsWith(st)) {
                    StemConflictTable.getInstance().setBit(st.getIndex(), st2.getIndex());
                }
            }
        }
        System.out.println("Done setting conflicting stems");
        return allStems;
    }

    private boolean canBind(char a, char b) {
        a = Character.toUpperCase(a);
        b = Character.toUpperCase(b);

        if (a == 'A')
            if (b == 'U')
                return true;

        if (a == 'U')
            if (b == 'A' || b == 'G')  //U-G is a wobble pair
                return true;

        if (a == 'C')
            if (b == 'G')
                return true;

        if (a == 'G')
            if (b == 'C' || b == 'U')  //G-U is a wobble pair
                return true;

        return false;
    }

    public ArrayList<RNAStem> getAllStems() {
        return allStems;
    }

    /**
     * @return Returns the nucleotides.
     */
    /*public String getNucleotides() {
        return nucleotides;
    }*/

    /**
     * @param nucleotides The nucleotides to set.
     */
    public void setNucleotides(String nucleotides) {
        this.nucleotides = nucleotides;
    }


    /**
     *
     */
    public class PivotString {
        public PivotString(String str, boolean pivotOn) {
            this.str = str;
            pivot = 0;
            this.pivotOn = pivotOn;
        }
        /**
         * @return the length of the overlapping part of the string
         */
        public int length() {
            if (pivotOn)
                return Math.min(pivot, str.length()-1-pivot);
            else
                return Math.min(pivot+1, str.length()-1-pivot);
        }

        public void reset() {
            pivot = 0;
        }

        public boolean hasNext() {
            if (pivot < str.length()-1)
                return true;

            return false;
        }

        public boolean next() {
            if (this.hasNext()) {
                pivot++;
                return true;
            }
            else {
                return false;
            }
        }

        public char getNucleotide(int index) {
            if (pivotOn) {
                if (index < this.length() && index >= 0)
                    return str.charAt(pivot - index - 1);
                else
                    throw new IndexOutOfBoundsException();
            }
            else {
                if (index < this.length() && index >= 0)
                    return str.charAt(pivot - index);
                else
                    throw new IndexOutOfBoundsException();
            }
        }

        public char getComplimentaryNucleotide(int index) {
            if (index < this.length() && index >= 0)
                return str.charAt(pivot + index + 1);
            else
                throw new IndexOutOfBoundsException();
        }

        public int get5prime(int index) {
            if (pivotOn)
                return pivot - index - 1;
            else
                return pivot - index;
        }

        public int get3prime(int index) {
            return pivot + index + 1;
        }

        public boolean canBind(int index) {
            if (pivotOn) {
                if (index < 1)
                    return false;
                else
                    try {
                        return StemGenerator.getInstance().canBind(getNucleotide(index), getComplimentaryNucleotide(index));
                    }
                    catch (IndexOutOfBoundsException e) {
                        return false;
                    }
            }
            else {
                if (index < 2)
                    return false;
                else
                    try {
                        return StemGenerator.getInstance().canBind(getNucleotide(index), getComplimentaryNucleotide(index));
                    }
                    catch (IndexOutOfBoundsException e) {
                        return false;
                    }
            }
        }

        public String toString() {
            String temp;
            String top = "";
            String middle = "";
            String inverted = "";
            if (pivotOn) {
                for (int i = pivot-1; i >= 0; i--) {
                    inverted += str.charAt(i);
                }
            }
            else {
                for (int i = pivot; i >= 0; i--) {
                    inverted += str.charAt(i);
                }
            }
            for (int i = pivot + 1; i < str.length(); i++) {
                top += str.charAt(i);
            }
            int j;
            if (pivotOn)
                j = 1;
            else
                j = 2;
            for (j = 1; j < this.length(); j++) {
                if (this.canBind(j)) {
                    middle += "|";
                }
                else {
                    middle += " ";
                }
            }
            temp = "Pivot: " + pivot;
            temp += "\n";
            temp += " "+top + " - 3'";
            temp += "\n";
            if (pivotOn)
                temp += str.charAt(pivot)+" "+middle;
            else
                temp += "  "+middle;
            temp += "\n";
            temp += " "+inverted + " - 5'";
            return temp;
        }

        private String str;
        private int pivot;
        private boolean pivotOn;
    }
        /*
    public static class DoubleString {
        public DoubleString(String str) {
            this.str = str;
            position = -1;
        }

        public int length() {
            return position+1;
        }

        public void reset () {
            position = -1;
        }

        public boolean hasNext() {
            if (position < str.length() - 1)
                return true;
            else
                return false;
        }

        public boolean next() {
            if (this.hasNext()) {
                position++;
                return true;
            } else {
                return false;
            }
        }

        public char getNucleotide(int index) throws IndexOutOfBoundsException{
            if (index <= position)
                return str.charAt(index);
            else
                throw new IndexOutOfBoundsException();
        }

        public char getComplimentaryNucleotide(int index) throws IndexOutOfBoundsException {
            if (index <= position)
                return str.charAt(position-index);
            else
                throw new IndexOutOfBoundsException();
        }
        //????
        public int get3Prime(int prime5) {
            return position - prime5;
        }
        //????
        public boolean canBind(int index) {
            if (index > position ) return false;
            else if (this.get3Prime(index) - index < 4) {
                //System.out.print("Nucleotides too close: Position: "+position);
                //System.out.println(" index: "+index+" complemntary index: "+this.get3Prime(index));
                return false;
            }
            else return StemGenerator.canBind(getNucleotide(index),getComplimentaryNucleotide(index));
        }

        public String toString() {
            String temp;
            String spaces = "";
            String inverted = "";
            for (int i = str.length()-1; i >= 0; i--) {
                inverted += str.charAt(i);
            }
            for (int i = 0; i < str.length() - 1 - position; i++)
                spaces += " ";
            temp = "Position: " + position;
            temp += "\n";
            temp += spaces;
            temp += str;
            temp += "\n";
            temp += inverted;
            return temp;
        }

        private String str;
        private int position;
    }
    */
}
