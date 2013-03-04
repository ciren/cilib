/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import com.google.common.base.Preconditions;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p><b>The Egg Holder.</b></p>
 *
 * <p><b>Reference:</b> S.K. Mishra, <i>Some New Test Functions
 * for Global Optimization and Performance of Repulsive Particle
 * Swarm Methods</i>, Technical Report, North-Eastern Hill University,
 * India, 2006</p>
 *
 * <p>Note: n >= 2</p>
 *
 * <p>Minimum:
 * <ul>
 * <li> f(<b>x</b>*) approx -959.64 </li>
 * <li> <b>x</b>* = (512,404.2319) for n=2</li>
 * <li> for x_i in [-512,512]</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * <li>Only defined for 2+ dimensions</li>
 * <li>Multimodal</li>
 * <li>Non-separable</li>
 * <li>Not regular</li>
 * </ul>
 * </p>
 *
 * R(-512.0,512.0)^30
 *
 */
public class EggHolder implements ContinuousFunction {

    private static final long serialVersionUID = 358993985066821115L;

    @Override
    public Double apply(Vector input) {
        Preconditions.checkArgument(input.size() >= 2, "EggHolder function is only defined for 2 or more dimensions");

        double sum = 0.0;
        for (int i = 0; i < input.size() - 1; i++) {
            sum += (-1*(input.doubleValueOf(i+1) + 47)
                    *Math.sin(Math.sqrt(Math.abs(input.doubleValueOf(i+1) + input.doubleValueOf(i)/2 + 47)))
                    + Math.sin(Math.sqrt(Math.abs(input.doubleValueOf(i) - (input.doubleValueOf(i+1)+47))))
                    *(-1*input.doubleValueOf(i)));
        }
        return sum;
    }
}
