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
package net.sourceforge.cilib.bioinf.sequencealignment;

import java.util.Collection;

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.MaximisationFitness;
import net.sourceforge.cilib.problem.OptimisationProblemAdapter;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;
import net.sourceforge.cilib.type.parser.DomainParser;
import net.sourceforge.cilib.type.parser.parser.ParserException;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This class represents the Optimization Problem to be solved for the MSA (Binary representation).
 *
 */
public class BinaryMSAProblem extends OptimisationProblemAdapter {

    private static final long serialVersionUID = 7939251270170439461L;

    private DomainRegistry domainRegistry;  //will hold the domain, i.e. the solution space is defined
    private Collection<String> strings; //holds the array of input sequences
    private BinaryAlignmentCreator alignmentCreator;  //discretize position into alignment
    private GapPenaltiesMethod gapPenaltyMethod; //interface for the gap penalty methods

    private int maxSequenceGapsAllowed;  //FOR GAPPED, AS A STANDARD, SHOULD BE SET TO 20% EXTRA OF THE LENGTH OF SEQUENCE, MIN 2
    private int smallestLength = Integer.MAX_VALUE;  //for init purpose
    private int biggestLength = Integer.MIN_VALUE; //for init purpose
    private int averageLength;  //computed average length of all the input sequences
    private int [] gapsArray; //holds total num of gaps to be inserted
    private int totalGaps;
    private double weight1 = 1.0, weight2 = 1.0;  // defaults, can be set in the XML configuration

    /**
     * Create an instance of the class.
     */
    public BinaryMSAProblem() {
        this.domainRegistry = new StringBasedDomainRegistry();
    }

    /**
     * {@inheritDoc}
     */
    public OptimisationProblemAdapter getClone() {
        return this;
    }

    /**
     * Set the value of weight1.
     * @param weight1 The value to set.
     */
    public void setWeight1(double weight1) {
        this.weight1 = weight1;
    }

    /**
     * Get the value of weight1.
     * @return The value of weight1.
     */
    public double getWeight1() {
        return weight1;
    }

    /**
     * Get the alignment creator.
     * @return The {@linkplain AlignmentCreator}.
     */
    public BinaryAlignmentCreator getAlignmentCreator() {
        return alignmentCreator;
    }

    /**
     * Set the {@linkplain AlignmentCreator}.
     * @param alignmentCreator The {@linkplain AlignmentCreator} to set.
     */
    public void setAlignmentCreator(BinaryAlignmentCreator alignmentCreator) {
        this.alignmentCreator = alignmentCreator;
    }

    /**
     * Set the value of weight2.
     * @param weight2 The value to set.
     */
    public void setWeight2(double weight2) {
        this.weight2 = weight2;
    }

    /**
     * Set the strategy to penalize gaps.
     * @param gapPenaltyMethod The strategy to set.
     */
    public void setGapPenaltyMethod(GapPenaltiesMethod gapPenaltyMethod) {
        this.gapPenaltyMethod = gapPenaltyMethod;
    }

    /**
     * Get the current gap penelization method.
     * @return The current {@linkplain GapPenaltiesMethod}.
     */
    public GapPenaltiesMethod getGapPenaltyMethod() {
        return gapPenaltyMethod;
    }

    /**
     * {@inheritDoc}
     */
    protected Fitness calculateFitness(Type solution) {  //    solution = particule position vector
        Vector realValuedPosition = (Vector) solution;
        //System.out.println("Fitness for matches: "+alignmentCreator.getFitness(strings, realValuedPosition, gapsArray));   // debug purpose
        //System.out.println("Fitness for gap penalties: "+gapPenaltyMethod.getPenalty(alignmentCreator.getAlignment());  // debug purpose

        //speed boost: don't calculate gaps penalty at all if weight2=0
        //final fitness with weights applied
        if (weight2 == 0.0) return new MaximisationFitness(new Double(weight1*alignmentCreator.getFitness(strings, realValuedPosition, gapsArray)));
        else return new MaximisationFitness(new Double(weight1*alignmentCreator.getFitness(strings, realValuedPosition, gapsArray)- weight2*gapPenaltyMethod.getPenalty(alignmentCreator.getAlignment())));
    }

    /**
     * Set the maximm number of allowable gaps.
     * @param number The maximum number allowable.
     */
    //     If gaps are allowed, make it a 20% of sequence length (in XML file). Otherwise set it to 0.
    public void setMaxSequenceGapsAllowed(int number) {
        if (number < 0) {
            this.maxSequenceGapsAllowed = 0;
            System.out.println("  **  Warning  **  Negative values for specified amount of gaps allowed cannot be negative, set to 0.");
        }
        else
            this.maxSequenceGapsAllowed = number;
    }

    /**
     * {@inheritDoc}
     */
    public DomainRegistry getDomain() { //computes the domain according to the input sequences and amount of gaps to insert
        if (this.domainRegistry.getDomainString() == null) {
//            DomainParser parser = new DomainParser();

            //reads in the input data sets.
            FASTADataSetBuilder stringBuilder = (FASTADataSetBuilder) this.getDataSetBuilder();
            strings = stringBuilder.getStrings();
            int totalLength = 0;

            for (String string : strings) {
                totalLength += string.length();

                if (string.length() < smallestLength) smallestLength = string.length();
                if (string.length() > biggestLength) biggestLength = string.length();
            }

            averageLength = (int) Math.round(totalLength/strings.size());
            System.out.println("Got "+strings.size()+" sequences of average length: "+averageLength+".");

            /*
             * ATTENTION:  An alignment is only valid if all the aligned sequences are of the same length!!!!
             * PRE-PROCESSING follows. Calculates the number of gaps to be inserted in each sequences and fills up an array
             * used later to allocate the correct number of gaps to its respective sequence.
             */
            gapsArray = new int [strings.size()];

            int delta = 0;
            int c = 0;
            for (String aSeq : strings) {
                delta = biggestLength - aSeq.length();
                gapsArray[c] = delta+maxSequenceGapsAllowed;
                c++;
            }

            for (int t = 0; t < strings.size(); t++) totalGaps+=gapsArray[t];

            System.out.println("Total gaps to be added: " + totalGaps+".");

            String rep = "";
            //Ouputs a recommended amount of gaps to be inserted per sequence which is usually set to 20% of the longest sequence
            System.out.println("Recommended number of gaps per sequence: " + Math.ceil(0.2 * averageLength) +".");
            // The domain representation string
            rep = "B^"+totalGaps*4;  // ENABLE THIS TO USE A BINARY REPRESENTATION (4 bits is for alignment of maximum 15 in length).

            System.out.println("Domain: "+rep);  //extra for debug, can be commented out

                //extra for debug, can be commented out
                //extra for debug, can be commented out
//            parser.parse(rep);
            DomainParser.parse(rep);

            domainRegistry.setDomainString(rep);
        }

        return domainRegistry;
    }

}
