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
package net.sourceforge.cilib.functions.discrete;

import java.math.BigInteger;

import net.sourceforge.cilib.functions.DiscreteFunction;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Discrete function to match the given bit string or a randomly generated bit string.
 *
 * @author Gary Pampara
 */
public class BitStringMatcher extends DiscreteFunction {

    private static final long serialVersionUID = 7535776840908399415L;

    private String targetRandomString;
    private int numberOfBits;

    /**
     * Constructor.
     */
    public BitStringMatcher() {
    }

    public BitStringMatcher getClone() {
        return new BitStringMatcher();
    }


    /**
     * Set the domain of the function and generate a random bit string. The generated
     * random bit string is generated to ensure that there is a target bit string to
     * solve if one is not provided by {@see net.sourceforge.cilib.functions.discrete.BitStringMatcher#setTargetRandomString(String)}.
     * The super classes setDomain() is called before the random bit string is generated.
     *
     *  @param newDomain The string representation of the doamin to set.
     */
    public void setDomain(String newDomain) {
        super.setDomain(newDomain);
        this.numberOfBits = this.getDimension();

        BigInteger bi = new BigInteger(this.numberOfBits, new MersenneTwister());
        this.targetRandomString = bi.toString(2);

        // We need to prepend leading '0's cause the BigInteger removes leading 0's
        // cause it does not change the value of the number that is represented
        if (this.targetRandomString.length() != this.numberOfBits) {
            StringBuilder buf = new StringBuilder(this.targetRandomString);
            int difference = this.numberOfBits - this.targetRandomString.length();

            for (int i = 0; i < difference; i++) {
                buf.insert(0, '0');
            }

            this.targetRandomString = buf.toString();
        }
    }


    /**
     * Get the target random bit string to match.
     * @return The target random bit string
     */
    public String getTargetRandomString() {
        return this.targetRandomString;
    }


    /**
     * Set the target random bit string to match.
     * @param target The target random bit string to set
     */
    public void setTargetRandomString(String target) {
        this.targetRandomString = target;
    }



    /**
     * Get the number of bits in the bit string that must be matched.
     * @return The number of bits within the bit string
     */
    public int getNumberOfBits() {
        return this.numberOfBits;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Integer evaluate(Vector input) {
        int result = 0;

        for (int i = 0; i < input.getDimension(); i++) {
            boolean bitValue = (this.targetRandomString.charAt(i) == '1') ? true : false;

            if (input.getBit(i) == bitValue)
                result++;
        }

        return result;
    }

}
