/*
 * ProblemFactory.java
 *
 * Created on February 5, 2003, 10:24 AM
 */

package net.sourceforge.cilib.Problem;

/**
 *
 * <p>
 * A factory used to produce a {@link Problem} must satisfy this interface. 
 * {@link net.sourceforge.cilib.XMLObjectFactory} implements this
 * interface so that problems can be created for the 
 * {@link net.sourceforge.cilib.Simulator.Simulator}.
 * </p>
 * <p>
 * To avoid the use of XML you can create your own implementation of <code>ProblemFactory</code> to create
 * appropriately configured problems.
 * </p>
 *
 * @author  espeer
 */
public interface ProblemFactory {
    /**
     * Returns a newly contructed problem.
     *
     * @return A new {@link Problem}.
     */
    public Problem newProblem();
}
