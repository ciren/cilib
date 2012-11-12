/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * SchwefelProblem1_2.
 *
 * Characteristics:
 *
 * <li>Unimodal</li>
 * <li>Non Separable</li>
 *
 * f(x) = 0; x = (0,0,...,0)
 *
 * x e [-100,100]
 *
 * R(-100,100)^30
 * <p>
 * Implementation: 
 * http://web.mysites.ntu.edu.sg/epnsugan/PublicSite/Shared%20Documents/Forms/AllItems.aspx?RootFolder=%2fepnsugan%2fPublicSite%2fShared%20Documents%2fCEC2005&FolderCTID=&View=%7bDAF31868%2d97D8%2d4779%2dAE49%2d9CEC4DC3F310%7
 * </p>
 *
 */
public class SchwefelProblem1_2 implements ContinuousFunction {

    private static final long serialVersionUID = -65519037071861168L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double prevSum;
        double currSum = 0.0;
        double outerSum = 0.0;

        for (int i = 0; i < input.size(); i ++) {
            prevSum = currSum;
            currSum = prevSum + input.doubleValueOf(i);
            outerSum += currSum * currSum;
        }

        return outerSum;
    }
}
