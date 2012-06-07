/**
 * Computational Intelligence Library (CIlib) Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP) Department of Computer
 * Science University of Pretoria South Africa
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.functions.discrete;

import com.google.common.base.Strings;
import java.math.BigInteger;
import net.sourceforge.cilib.functions.DiscreteFunction;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
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

            BigInteger bi = new BigInteger(this.numberOfBits, new RandomAdaptor(new MersenneTwister()));
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
