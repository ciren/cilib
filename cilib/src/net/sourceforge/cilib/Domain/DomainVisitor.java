/*
 * DomainVisitor.java 
 * 
 * Created on Apr 17, 2004
 *
 * Copyright (C)  2004 - CIRG@UP 
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
 *
 */
package net.sourceforge.cilib.Domain;

/**
 * Special visitor type for visiting instances of specific domains.  This
 * allows one to visit the entire tree structure of group instances of this
 * domain.  Any class implementing this interface will need to know which
 * indices contain what initial tree's, the acceptDomainVisitor functions will
 * ensure that the sub-tree's stays in the same order as the parent tree's.
 *
 * It is also guaranteed that the visit??? methods will be called <b>in
 * order</b>
 *
 * A quick note on usage, a PSO example would probably be appropriate where
 * we need position, personalBest and neighborhoodBest.  Lets say our domain
 * is R^30,B^30, then we will call acceptDomainVistior(someVisitor,
 * new Object[]{position, personalBest, neighborhoodBest}).  This will then
 * first call someVisitor.visitDoubleArray(double [][]instances) where
 * instances[0] will be the R^30 double[] of the position, instances[1] that
 * of the personalBest and instances[2] that of neighboorhoodBest.  The
 * visitor is itself responsible for keeping track of the dimensions (it will
 * be called in order so this is a simple task) if required.  After the first
 * call visitBitArray(byte [][]) will then be called in a similar fashion.
 *
 * @author jkroon
 */
public interface DomainVisitor {
	/**
	 * Gets called if the subtree is a single bit.  The Byte[] contains the
	 * diffent instances that was passed to acceptDomainVisitor method.  This
	 * will happen if you have a B in your domain without a ^ specifier.
	 *
	 * @param instances The "bit" associated with each of the instances
	 * 		passed to DomainComponent.acceptDomainVisitor().
	 *
	 * @author jkroon
	 */
	public void visitBit(Byte []instances);

	/**
	 * Gets called if the subtree is an array of bits.  This will happen
	 * if you have a B^?? in your domain.
	 *
	 * @param instances The arrays of bits for each instance of the instances
	 *		passed to DomainComponent.acceptDomainVisitor().
	 *
	 * @author jkroon
	 */
	public void visitBitSet(java.util.BitSet []instances);

	/**
	 * Gets called if the subtree is an single Real value.  This will happen
	 * if you have a R in your domain without the ^ modifier.
	 *
	 * @param instances An array containing all the Double's for the various
	 * 		instances passed to DomainComponent.acceptDomainVisitor().
	 *
	 * @author jkroon
	 */
	public void visitReal(Double []instances);

	/**
	 * Gets called if the subtree is an array of real values.  This will happen
	 * if you have a R^?? in your domain.
	 *
	 * @param instances An array containing all the double[] for each of the
	 * 		instances passed to DomainComponent.acceptDomainVisitor().
	 *
	 * @author jkroon
	 */
	public void visitRealArray(double [][]instances);

	/**
	 * Gets called if the subtree is a single Integer value.  This will happen
	 * if you have a Z in your domain without the ^ modifier.
	 *
	 * @param instances An array containing all the Integer's for the various
	 * 		instances passed to DomainComponent.acceptDomainVisitor().
	 *
	 * @author jkroon
	 */
	public void visitInteger(Integer []instances);

	/**
	 * Gets called if the subtree is an array of Integer values.  This will
	 * happen if you have Z^?? in your domain.
	 *
	 * @param instances An array containing all the int[]'s for the various
	 * 		instances passed to DomainComponent.acceptDomainVisitor().
	 *
	 * @author jkroon
	 */
	public void visitIntegerArray(int [][]instances);

	/**
	 * Gets called if the component is of the domain type Text.  Please note
	 * that there is no array special case since that would be a waste of
	 * coding.  The acceptDomainVisitor function will handle it an as efficient
	 * manner as possible, if you have a counter-example, please contact the
	 * author.
	 *
	 * @param instances An array containing the String parts of the appropraite
	 * 		instances.
	 *
	 * @author jkroon
	 */
	public void visitText(String []instances);
	
	/**
	 * Gets called if the component is of the domain type Set.  Please note
	 * that there is no array special case since that would be a waste of
	 * coding.  The acceptDomainVisitor function will handle it an as efficient
	 * manner as possible, if you have a counter-example, please contact the
	 * author.
	 *
	 * @param instances An array containing the  parts of the appropraite
	 * 		instances.
	 *
	 * @author jkroon
	 */
	public void visitSet(java.util.Set []instances);
	
	/**
	 * Gets called if the component is of the domain type Unknown.  Please note
	 * that there is no array special case since that would be a waste of
	 * coding.  The acceptDomainVisitor function will handle it an as efficient
	 * manner as possible, if you have a counter-example, please contact the
	 * author.
	 *
	 * @param instances An array containing the Object parts of the appropraite
	 * 		instances.
	 *
	 * @author jkroon
	 */
	public void visitUnknown(Object []instances);
}
