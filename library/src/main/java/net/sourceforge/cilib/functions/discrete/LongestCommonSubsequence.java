/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.discrete;

import com.google.common.base.Preconditions;
import net.sourceforge.cilib.functions.DiscreteFunction;
import net.sourceforge.cilib.problem.dataset.TextDataSetBuilder;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Implementation of the Longest Common Subsequence problem.
 */
public class LongestCommonSubsequence implements DiscreteFunction {

    private static final long serialVersionUID = -3586259608521073084L;

    private TextDataSetBuilder dataSetBuilder;

    @Override
    public Integer apply(Vector input) {
        Preconditions.checkNotNull(dataSetBuilder, "Dataset builder has not been set yet.");
        int l = length(input);
        int m = matches(input);
        int k = dataSetBuilder.size();
        int v = l + (30 * m);

        if (l == dataSetBuilder.getShortestString().length()) {
            v += 50;
        }

        if (m == k) {
            v *= 3000;
        } else {
            v *= -1000 * (k - m);
        }

        return v;
    }

    /**
     * Calculates the number of positive bits in the bit string.
     *
     * @param x the input bit string
     * @return the number of positive bits
     */
    private int length(Vector x) {
        int count = 0;

        for (Numeric n : x) {
            if (n.booleanValue()) {
                count++;
            }
        }

        return count;
    }

    private int matches(Vector x) {
        int count = 0;

        String targetSubSequence = this.getSubSequence(x, dataSetBuilder.getShortestString());

        for (int i = 0; i < dataSetBuilder.size(); i++) {
            String tmp = this.getSubSequence(x, dataSetBuilder.get(i));

            if (tmp.equals(targetSubSequence)) {
                count++;
            }
        }

        return count;
    }

    private String getSubSequence(Vector x, String target) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < x.size(); i++) {
            Numeric n = x.get(i);
            if (n.booleanValue()) {
                builder.append(target.charAt(i));
            }
        }

        return builder.toString();
    }

    public void setDataSetBuilder(TextDataSetBuilder dataSetBuilder) {
        this.dataSetBuilder = dataSetBuilder;
    }

    public TextDataSetBuilder getDataSetBuilder() {
        return dataSetBuilder;
    }
}
