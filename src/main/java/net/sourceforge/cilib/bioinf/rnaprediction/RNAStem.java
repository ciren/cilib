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
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.cilib.type.types.Type;


/**
 * @author mneethling
 *
 */
public class RNAStem implements Type, Comparable<RNAStem>, Collection<NucleotidePair>  {

    /**
     *
     */
    private static final long serialVersionUID = -6779192334692407858L;
    private ArrayList<NucleotidePair> basePairs;
    private int id;
    private int index;
    private static int count = 0;
    private ArrayList<RNAStem> conflictingStems;

    public RNAStem() {
        this.basePairs = new ArrayList<NucleotidePair>();
        this.id = count++;
    }

    public RNAStem(ArrayList<NucleotidePair> basePairs) {
        this.basePairs = basePairs;
        this.id = count++;
    }

    public int getId() {
        return this.id;
    }

    /**
     * @return Returns the index.
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index The index to set.
     */
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;

        if ((other == null) || (this.getClass() != other.getClass()))
            return false;

        RNAStem otherStem = (RNAStem) other;
        return (this.id == otherStem.id) &&
            (this.index == otherStem.index) &&
            (this.conflictingStems.equals(otherStem.conflictingStems));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + this.id;
        hash = 31 * hash + this.index;
        hash = 31 * hash + (this.conflictingStems == null ? 0 : this.conflictingStems.hashCode());
        return hash;
    }


    public int getLength() {
        return basePairs.size();
    }

    public int size() {
        return basePairs.size();
    }

    public boolean isEmpty() {
        return basePairs.isEmpty();
    }

    public boolean contains(Object o) {
        return basePairs.contains(o);
    }

    public Object[] toArray() {
        return basePairs.toArray();
    }

    public boolean remove(Object o) {
        return basePairs.remove(o);
    }

    public void clear() {
        basePairs.clear();
    }

    /**
     * Check if this stem is inside otherstem.
     * ie i`<i<j<j` where i, j is this stem and i`,j` is other stem.
     * @param otherstem
     * @return
     */
    @SuppressWarnings("unused")
    private boolean inside(RNAStem otherstem) {
        for (NucleotidePair p : basePairs) {
            for (NucleotidePair pprime : otherstem.basePairs) {
                if (p.get5primeIndex() <= pprime.get5primeIndex())
                    return false;
                if (p.get3primeIndex() >= pprime.get3primeIndex())
                    return false;
            }
        }
        return true;
    }

    /**
     * Uses the StemConflictTable Singleton class to look up conflicts quicker.
     * @param otherstem
     * @return
     */
    public boolean conflictsWith(RNAStem otherstem) {
        return StemConflictTable.getInstance().get(this.index, otherstem.index);
    }

    /**
     * returns true if the stems fail all of four tests.
     * @param stem
     * @return
     */
    public boolean slowConflictsWith(RNAStem otherstem) {
        if (shareBases(otherstem))
            return true;
        if (contains(otherstem))
            return false;
        if (containedInside(otherstem))
            return false;
        if (isPseudoknot(otherstem))
            return true;
        return false;
    }

    private boolean shareBases(RNAStem otherstem) {
        //check for shared bases
        for (NucleotidePair p : basePairs)
            for (NucleotidePair pprime : otherstem.basePairs)
                if (p.shareBase(pprime))
                    return true;
        return false;
    }

    private boolean contains(RNAStem otherstem) {
        for (NucleotidePair p : basePairs)
            for (NucleotidePair pprime : otherstem.basePairs)
                if (!p.contains(pprime))
                    return false;
        return true;
    }

    private boolean containedInside(RNAStem otherstem) {
        for (NucleotidePair p : basePairs)
            for (NucleotidePair pprime : otherstem.basePairs)
                if (!p.containedInside(pprime))
                    return false;
        return true;
    }

    private boolean isPseudoknot(RNAStem otherstem) {
        for (NucleotidePair p : basePairs)
            for (NucleotidePair pprime : otherstem.basePairs)
                if (p.isPseudoknot(pprime))
                    return true;
        return false;
    }

    public boolean add(NucleotidePair o) {
        return basePairs.add(o);
    }

    public int compareTo(RNAStem o) {
        try {
            if (basePairs.containsAll(o.basePairs))
                if (o.basePairs.containsAll(basePairs))
                    return 0;
        }
        catch (ClassCastException e) {
            return -1;
        }
        return -1;
    }

    public boolean addAll(Collection<? extends NucleotidePair> c) {
        return basePairs.addAll(c);
    }

    public boolean removeAll(Collection<?> c) {
        return basePairs.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return basePairs.retainAll(c);
    }

    public <T> T[] toArray(T[] a) {
        return basePairs.toArray(a);
    }

    public Iterator<NucleotidePair> iterator() {
        return basePairs.iterator();
    }

    public boolean containsAll(Collection<?> c) {
        return basePairs.containsAll(c);
    }

    public void setConflictingStems(ArrayList<RNAStem> conflicts) {
        this.conflictingStems = conflicts;
    }

    public ArrayList<RNAStem> getConflictingStems() {
        return this.conflictingStems;
    }

    public RNAStem getClone() {
        ArrayList<NucleotidePair> pairs = new ArrayList<NucleotidePair>();
        pairs.addAll(this.basePairs);
        return new RNAStem(pairs);
    }

    //public void setConflictingStems(ArrayList<RNAStem> cs) {
    //    this.conflictingStems = cs;
    //}

    @Override
    public String toString() {
        Object [] sorted = basePairs.toArray();
        Arrays.sort(sorted);
        StringBuilder sb = new StringBuilder();
        sb.append("\n   ");
        for (int i = 0; i < sorted.length; i++) {
            sb.append(((NucleotidePair) sorted[i]).get5primeIndex()+" ");
        }
        sb.append("\n5'-");
        for (int i = 0; i < sorted.length; i++) {
            sb.append(((NucleotidePair) sorted[i]).get5primeBase());
        }
        sb.append("\n   ");
        for (int i = 0; i < sorted.length; i++) {
            sb.append("|");
        }
        sb.append("\n3'-");
        for (int i = 0; i < sorted.length; i++) {
            sb.append(((NucleotidePair) sorted[i]).get3primeBase());
        }
        sb.append("\n   ");
        for (int i = 0; i < sorted.length; i++) {
            sb.append(((NucleotidePair) sorted[i]).get3primeIndex()+" ");
        }
        return sb.toString();
    }

    /**
     * Nucleotide string example:
     * 5'              3'
     * |               |
     * a a a g g c c u u
     * | | | | | | | | |
     * 0 1 2 3 4 5 6 7 8
     *
     * @param p5_index - The index of the nucleotide string that corresponds with the beginning of the stem
     * @param p3_index - The index of the nucleotide string that corresponds to the end of the stem
     * @param length - The length of the stem
     */



    /*
    public RNAStem(int p5_index, int p3_index, int length){
        this.id = RNAStem.count;
        RNAStem.count++;
        this.length = length;
        this.p5_i = p5_index;
        this.p3_i = p3_index;
    }

    public int getLength() {
        return this.length;
    }

    public int getId() {
        return this.id;
    }

    public int getP5_index() {
        return this.p5_i;
    }

    public int getP3_index() {
        return this.p3_i;
    }


    public int compareTo(Object arg0) {
        //order on 5prime index
        return this.p5_i - ((RNAStem)arg0).p5_i;
    }

    public boolean equals(Object rhs) {
        return (((RNAStem)rhs).id == this.id);
    }

    public int hashCode() {
        return id;
    }

    public String toString() {
        return ("Stemid:" + this.id + " | 5p:" + this.p5_i + " | 3p:" + this.p3_i + " | length:"+ length);
    }

    public void setConflictingStems(ArrayList<RNAStem> cs) {
        this.conflictingStems = cs;
    }


    public ArrayList<RNAStem> getConflictingStems() {
        return conflictingStems;
    }

    public boolean conflictsWith(RNAStem rna) {
        //two stems are not allowed to contain the same nucleotide
        int p5_i_end = this.p5_i + this.length - 1;
        int p3_i_begin = this.p3_i - this.length + 1;
        //check this stem's 5' strand against 5' and 3' strand of other stem
        //case 1: this stem is 'inside' the other stem
        if (p5_i > rna.p5_i+rna.length-1)
            if (p3_i < rna.p3_i-rna.length+1)
                return false;
        //case 2: the other stem is 'inside' this stem
        if (rna.p5_i > p5_i_end)
            if (rna.p3_i < p3_i_begin)
                return false;
        //case 3: neither is inside the other, whatch out for pseudo knots
            //this stem comes before the other stem
        if (p3_i < rna.p5_i && p3_i < rna.p3_i)
            return false;
            //other stem comes before this stem
        if (rna.p3_i < p5_i && rna.p3_i < p3_i)
            return false;
        //else
        return true;
    }

    public int getDimension() {
        return length;
    }

    private int id, length;
    private int p5_i, p3_i;
    private static int count = 0;
    private ArrayList<RNAStem> conflictingStems;

    @Override
    public void randomize() {


    }
    */

}
