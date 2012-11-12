/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.discrete;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;


/**
 * Implementation of Hollands Royal Road function.
 * Intended for bit strings of length 240
 * <p>
 * References:
 * </p>
 * <ul><li>
 * Terry Jones;, "A Description of Holland's Royal Road Function" (1994).
 * Sante Fe Institute
 * </li></ul>
 */
public class RoyalRoad implements ContinuousFunction {

    private int k;
    private int b;
    private int g;
    private int mstar;
    private double v;
    private double ustar;
    private double u;

    public RoyalRoad() {
        // set up default parameters as described by Holland
        k = 4;
        b = 8;
        g = 7;
        mstar = 4;
        v = 0.02;
        ustar = 1.0;
        u = 0.3;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        Preconditions.checkArgument(input.size() == Math.pow(2,k) * (b + g),
            "Royal Road function must be used with vectors of length " + Math.pow(2,k) * (b + g) + " for the given configuration.");

        return part(input) + bonus(input);
    }

    private Double part(Vector input) {
        double partFitness = 0.0;

        List<Vector> blocks = getBlocks(input);
        for (Vector block : blocks) {
            int numOnes = 0;
            for(int i = 0; i < block.size(); i++) {
                if (block.booleanValueOf(i)) {
                    numOnes++;
                }
            }

            if (numOnes != b) {
                if (numOnes <= mstar) {
                    partFitness += numOnes * v;
                } else {
                    partFitness += (numOnes - mstar) * (-v);
                }
            }
        }

        return partFitness;
    }

    private Double bonus(Vector input) {
        int levels = k+1;
        double bonusFitness = 0;

        List<Vector> blocks = getBlocks(input);

        for(int level = 0; level < levels; level++) {
            boolean first = true;

            for(int j = 0; j < blocks.size(); j+= Math.pow(2,level)) {
                boolean contiguous = true;

                for(int k = 0; k < Math.pow(2,level); k++) {
                    if (!isComplete(blocks.get(j+k))) {
                        contiguous = false;
                    }
                }

                if (contiguous) {
                    if (first) {
                        bonusFitness += ustar;
                        first = false;
                    } else {
                        bonusFitness += u;
                    }
                }
            }
        }

        return bonusFitness;
    }

    private List<Vector> getBlocks(Vector input) {
        Integer region = b + g;
        List<Vector> blocks = new ArrayList<Vector>();

        for(int i = 0; i < input.size(); i += region) {
            blocks.add(input.copyOfRange(i, i+b));
        }
        return blocks;
    }

    private boolean isComplete(Vector input) {
        for(int i = 0; i < input.size(); i++) {
            if (!input.booleanValueOf(i)) {
                return false;
            }
        }
        return true;
    }

    public void setV(double v) {
        this.v = v;
    }

    public void setUstar(double ustar) {
        this.ustar = ustar;
    }

    public void setU(double u) {
        this.u = u;
    }

    public void setMstar(int mstar) {
        this.mstar = mstar;
    }

    public void setK(int k) {
        this.k = k;
    }

    public void setG(int g) {
        this.g = g;
    }

    public void setB(int b) {
        this.b = b;
    }

    public double getV() {
        return v;
    }

    public double getUstar() {
        return ustar;
    }

    public double getU() {
        return u;
    }

    public int getMstar() {
        return mstar;
    }

    public int getK() {
        return k;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }
}
