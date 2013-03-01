/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.bbob;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.continuous.decorators.IllConditionedFunctionDecorator;
import net.sourceforge.cilib.functions.continuous.decorators.IrregularFunctionDecorator;
import net.sourceforge.cilib.functions.continuous.decorators.AsymmetricFunctionDecorator;
import net.sourceforge.cilib.functions.continuous.decorators.RotatedFunctionDecorator;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.math.random.CauchyDistribution;
import net.sourceforge.cilib.math.random.UniformDistribution;

/**
 * Helper class for the bbob 2009 test suite.
 */
public final class Helper {

    private Helper() {
    }

    public static Vector randomXOpt(int size) {
    	Vector.Builder builder = Vector.newBuilder();
    	for (int i = 0; i < size; i++) {
    		builder.add(new UniformDistribution().getRandomNumber(-4.0, 4.0));
    	}
    	return builder.build();
    }

    public static double randomFOpt() {
    	double fOpt = new CauchyDistribution().getRandomNumber(0, 100);
    	if (Math.abs(fOpt) > 1000) {
    		fOpt = Math.signum(fOpt) * 1000;
    	}
    	return (double)Math.round(fOpt * 100) / 100;
    }

    public static IllConditionedFunctionDecorator newIllConditioned(double alpha, ContinuousFunction f) {
        IllConditionedFunctionDecorator ill = new IllConditionedFunctionDecorator();
        ill.setAlpha(ConstantControlParameter.of(alpha));
        ill.setFunction(f);
        return ill;
    }

    public static IrregularFunctionDecorator newIrregular(ContinuousFunction f) {
        IrregularFunctionDecorator irr = new IrregularFunctionDecorator();
        irr.setFunction(f);
        return irr;
    }

    public static AsymmetricFunctionDecorator newAsymmetric(double beta, ContinuousFunction f) {
        AsymmetricFunctionDecorator asymm = new AsymmetricFunctionDecorator();
        asymm.setBeta(ConstantControlParameter.of(beta));
        asymm.setFunction(f);
        return asymm;
    }

    public static RotatedFunctionDecorator newRotated(ContinuousFunction f) {
        RotatedFunctionDecorator r = new RotatedFunctionDecorator();
        r.setFunction(f);
        return r;
    }

    public static Penalty newPenalty(double boundary) {
        Penalty pen = new Penalty();
        pen.setBoundary(ConstantControlParameter.of(boundary));
        return pen;
    }
}
