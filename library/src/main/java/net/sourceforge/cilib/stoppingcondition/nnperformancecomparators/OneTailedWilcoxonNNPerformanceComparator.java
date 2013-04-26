/**           __  __
*    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
*   / ___/ / / / __ \   (c) CIRG @ UP
*  / /__/ / / / /_/ /   http://cilib.net
*  \___/_/_/_/_.___/
*/
package net.sourceforge.cilib.stoppingcondition.nnperformancecomparators;

import java.util.ArrayList;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.types.Real;

/**
 * Performs a Wilcoxon signed-rank test to determine if to NNs have the same performance.
 * An error set is generated for each NN, and the distributions of these error sets are
 * then compared by the Wilcoxon signed-rank test. The errors in the errors sets are
 * based on the means squared error.
 * 
 * The test follows the implementation in R with the following parameters
 * - 2-sample case but paired, resulting in 1 sample case after subtraction
 * - not exact
 * - allows zeros
 * - allows ties
 * - without continuity correction
 */
public class OneTailedWilcoxonNNPerformanceComparator implements NNPerformanceComparator {

    double confLevel;
    double lastPValue;

    public OneTailedWilcoxonNNPerformanceComparator() {
        confLevel = 0.95;
        lastPValue = 0.0;
    }

    public OneTailedWilcoxonNNPerformanceComparator(OneTailedWilcoxonNNPerformanceComparator rhs) {
        confLevel = rhs.confLevel;
        lastPValue = rhs.lastPValue;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public OneTailedWilcoxonNNPerformanceComparator getClone() {
        return new OneTailedWilcoxonNNPerformanceComparator(this);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean isSame(StandardPatternDataTable validationSet, ArrayList<Vector> leftNN, ArrayList<Vector> rightNN) {
        lastPValue = pvalue(validationSet, leftNN, rightNN);
        return (1-confLevel) < lastPValue;
    }

    /**
     * Determines the p-value from a one-tailed, paired, Wilcoxon signed-rank test.
     * A small p-value implies that rightNN is producing better results.
     * This code has been adapted from R-2.10.1 (http://www.r-project.org/).
     * @return The p-value.
     * @param validationSet a data table containing the target outputs.
     * @param leftNN the actual outputs of the first neural network.
     * @param rightNN the actual outputs of the second neural network.
     */
    public static double pvalue(StandardPatternDataTable validationSet, ArrayList<Vector> leftNN, ArrayList<Vector> rightNN) {
        //calculate error sets consisting of per-pattern mean-squared errors.
        ArrayList<Real> leftErrors = new ArrayList<Real>();
        ArrayList<Real> rightErrors = new ArrayList<Real>();
        for (int curPattern = 0; curPattern < validationSet.size(); ++curPattern) {
            double lError = 0;
            double rError = 0;
            if (validationSet.getRow(0).getTarget() instanceof Vector) {
                for (int curOutput = 0; curOutput < leftNN.get(0).size(); ++curOutput) {
                    double t_k = ((Vector) validationSet.getRow(curPattern).getTarget()).doubleValueOf(curOutput);
                    lError += Math.pow(t_k - leftNN.get(curPattern).doubleValueOf(curOutput), 2);
                    rError += Math.pow(t_k - rightNN.get(curPattern).doubleValueOf(curOutput), 2);
                }
                leftErrors.add(Real.valueOf(lError/leftNN.get(0).size()));
                rightErrors.add(Real.valueOf(rError/leftNN.get(0).size()));
            }
            else {
                double t_k = ((Real) validationSet.getRow(curPattern).getTarget()).doubleValue();
                leftErrors.add(Real.valueOf(Math.pow(t_k - leftNN.get(curPattern).doubleValueOf(0), 2)));
                rightErrors.add(Real.valueOf(Math.pow(t_k - rightNN.get(curPattern).doubleValueOf(0), 2)));
            }
        }

        //calculate differences between pairs in the error sets
        ArrayList<Real> differences = new ArrayList<Real>();
        for (int curPair = 0; curPair < leftErrors.size(); ++curPair) {
            double difference = rightErrors.get(curPair).doubleValue() - leftErrors.get(curPair).doubleValue();
            if (difference != 0)
                differences.add(Real.valueOf(difference));
        }

        int n = differences.size();

        if (n == 0)
            return 0.5;

        //calculate ranks
        ArrayList<Real> ranks = new ArrayList<Real>();
        for (Real curDiff : differences) {
            double rank = 1.0;
            double ties = 0.0;
            for (Real curCompare : differences) {
                if (Math.abs(curDiff.doubleValue()) > Math.abs(curCompare.doubleValue()))
                    rank += 1;
                else if (curCompare != curDiff)
                    if (Math.abs(curDiff.doubleValue()) == Math.abs(curCompare.doubleValue()))
                        ties += 1;
            }
            rank += ties/2.0;   //average rank of multiple ties
            ranks.add(Real.valueOf(rank));
        }

        double z = 0;
        for (int curRank = 0; curRank < ranks.size(); curRank++) {
            if (differences.get(curRank).doubleValue() > 0) {
                z += ranks.get(curRank).doubleValue();
            }
        }
        z = z - n*(n+1.0)/4.0;

        //NTIES is only used to calculate this, so excluding the creation of a Map for simplicity.
        double sumNTies = 0;
        while (ranks.size() > 0) {
            int count = 0;
            double curRank = ranks.get(0).doubleValue();
            for (int curCompare = 0; curCompare < ranks.size(); curCompare++) {
                if (ranks.get(curCompare).doubleValue() == curRank) {
                    count++;
                    ranks.remove(curCompare);
                    curCompare--;
                }
            }
            sumNTies += count*count*count - count;
        }
        double sigma = Math.sqrt(n*(n+1.0)*(2.0*n+1.0)/24.0 - sumNTies/48.0);

        double pval = pnorm(z/sigma);

        return pval;
    }

    /**
     * Determines the p-value associated with particular W statistic.
     * This code has been adapted from R-2.10.1 (http://www.r-project.org/).
     * @param x The W statistic.
     * @return The p-value.
     */
    /*
        The coding style is deliberately ugly; the code was written to be as
        similar to the R code, in order to make comparison easier.
        Some code was left out due to default parameters making it unnecessary.
        In such cases, a comment was added to indicate this.
    */
    public static double pnorm(double x) {
        //default arguments:
        //mu = 0
        //sigma = 1
        //lower_tail = TRUE
        //log_p = FALSE

        double cum; //a.k.a. p
        double ccum; //a.k.a. cp

        cum = x; //mu = 0, sigma = 1

        double xden;
        double xnum;
        double temp;
        double del;
        double eps;
        double xsq;
        double y;

        final double a[] = {
            2.2352520354606839287,
            161.02823106855587881,
            1067.6894854603709582,
            18154.981253343561249,
            0.065682337918207449113
        };
        final double b[] = {
            47.20258190468824187,
            976.09855173777669322,
            10260.932208618978205,
            45507.789335026729956
        };
        final double c[] = {
            0.39894151208813466764,
            8.8831497943883759412,
            93.506656132177855979,
            597.27027639480026226,
            2494.5375852903726711,
            6848.1904505362823326,
            11602.651437647350124,
            9842.7148383839780218,
            1.0765576773720192317e-8
        };
        final double d[] = {
            22.266688044328115691,
            235.38790178262499861,
            1519.377599407554805,
            6485.558298266760755,
            18615.571640885098091,
            34900.952721145977266,
            38912.003286093271411,
            19685.429676859990727
        };
        final double p[] = {
            0.21589853405795699,
            0.1274011611602473639,
            0.022235277870649807,
            0.001421619193227893466,
            2.9112874951168792e-5,
            0.02307344176494017303
        };
        final double q[] = {
            1.28426009614491121,
            0.468238212480865118,
            0.0659881378689285515,
            0.00378239633202758244,
            7.29751555083966205e-5
        };

        int i;
        //boolean lower = true;
        //boolean upper = false;

        eps = Maths.EPSILON;

        y = Math.abs(x);
        if (y <= 0.67448975) {
            if (y > eps) {
                xsq = x*x;
                xnum = a[4] * xsq;
                xden = xsq;
                for (i = 0; i < 3; ++i) {
                    xnum = (xnum + a[i])*xsq;
                    xden = (xden + b[i])*xsq;
                }
            }
            else
            xnum = xden = 0.0;

            temp = x * (xnum + a[3]) / (xden + b[3]);

            //lower = true
            cum = 0.5 + temp;
            //upper = false
        }
        else if (y <= Math.sqrt(32)) {
            xnum = c[8] * y;
            xden = y;
            for (i = 0; i < 7; ++i) {
                xnum = (xnum + c[i]) * y;
                xden = (xden + d[i]) * y;
            }
            temp = (xnum + c[7]) / (xden + d[7]);

            //call do_del(y)
            {
                double trunc = y >= 0 ? Math.floor(y * 16) : Math.ceil(y * 16);
                xsq = trunc/16;
                del = (y - xsq) * (y + xsq);
                //log_p = false
                cum = Math.exp(-xsq*xsq*0.5) * Math.exp(-del*0.5) * temp;
                ccum = 1.0 - cum;
            }

            //call swap_tail
            {
                if (x > 0) {
                    temp = cum;
                    //lower = true
                    cum = ccum;
                    ccum = temp;
                }
            }
        }
        //log_p = false, lower = true, upper = false
        else if (-37.5193 < x && x < 8.2924) {
            xsq = 1.0 / (x*x);
            xnum = p[5] * xsq;
            xden = xsq;
            for (i = 0; i < 4; ++i) {
                xnum = (xnum + p[i]) * xsq;
                xden = (xden + q[i]) * xsq;
            }
            temp = xsq * (xnum + p[4]) / (xden + q[4]);
            temp = (1/Math.sqrt(2*Math.PI) - temp) / y;

            //call do_del(x)
            {
                double trunc = x >= 0 ? Math.floor(x * 16) : Math.ceil(x * 16);
                xsq = trunc/16;
                del = (x - xsq) * (x + xsq);
                //log_p = false
                cum = Math.exp(-xsq*xsq*0.5) * Math.exp(-del*0.5) * temp;
                ccum = 1.0 - cum;
            }

            //call swap_tail
            {
                if (x > 0) {
                    temp = cum;
                    //lower = true
                    cum = ccum;
                    ccum = temp;
                }
            }
        }
        else {
            if (x > 0) {
                cum = 1;
                ccum = 0;
            }
            else {
                cum = 0;
                ccum = 1;
            }
        }

        //lower_tail = true
        return cum;
    }

    /**
     * Set the target confidence level.
     * @param a confidence level in [0,1] (0.95 recommended).
     */
    public void setConfLevel(double newConfLevel) {
        confLevel = newConfLevel;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public double getLastPValue() {
        return lastPValue;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public double getTargetPValue() {
        return 1.0 - confLevel;
    }
}
