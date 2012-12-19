/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.discrete;

import com.google.common.base.Strings;
import java.math.BigInteger;
import net.sourceforge.cilib.functions.DiscreteFunction;
import net.sourceforge.cilib.math.random.generator.RandomAdaptor;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Discrete function to match the given bit string or a randomly generated bit
 * string.
 *
 */
public class BitStringMatcher implements DiscreteFunction {

    private static final long serialVersionUID = 7535776840908399415L;
    private String targetRandomString;
    private int numberOfBits;

    /**
     * Constructor.
     */
    public BitStringMatcher() {
    }

    /**
     * Get the target random bit string to match.
     *
     * @return The target random bit string
     */
    public String getTargetRandomString() {
        return this.targetRandomString;
    }

    /**
     * Set the target random bit string to match.
     *
     * @param target The target random bit string to set
     */
    public void setTargetRandomString(String target) {
        this.targetRandomString = target;
    }

    /**
     * Get the number of bits in the bit string that must be matched.
     *
     * @return The number of bits within the bit string
     */
    public int getNumberOfBits() {
        return this.numberOfBits;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer apply(Vector input) {
        if (this.targetRandomString == null) {
            this.numberOfBits = input.size();

            BigInteger bi = new BigInteger(this.numberOfBits, new RandomAdaptor());
            this.targetRandomString = Strings.padStart(bi.toString(2), numberOfBits, '0');
        }

        int result = 0;

        for (int i = 0; i < input.size(); i++) {
            boolean bitValue = (this.targetRandomString.charAt(i) == '1') ? true : false;

            if (input.booleanValueOf(i) == bitValue) {
                result++;
            }
        }

        return result;
    }
}
