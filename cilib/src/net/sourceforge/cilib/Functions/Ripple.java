package net.sourceforge.cilib.Functions;

import net.sourceforge.cilib.Domain.Validator.DimensionValidator;

/**
 * <p>Title: CILib</p>
 * <p>Description: CILib (Computational Intelligence Library)</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Clive Naicker
 * @version 1.0
 */

public class Ripple extends ContinuousFunction {

    public Ripple() {
        constraint.add(new DimensionValidator(2));
        setDomain("R(0, 1)^2");
    }
    
    public Object getMinimum() {
        return new Double(2.2);
    }

    public double evaluate(double[] X) {
        double x = X[0];
        double y = X[1];

        double term1 = Math.exp(-1.0*Math.log(2)*Math.pow(((x - 0.1)/0.8), 2));
        double term2 = Math.pow(Math.sin(5*Math.PI*x), 6) + 0.1*Math.pow(Math.cos(500*Math.PI*x), 2);
        double term3 = Math.exp(-2.0*Math.log(2)*Math.pow(((y - 0.1)/0.8), 2));
        double term4 = Math.sin(5*Math.PI*y) + 0.1*Math.pow(Math.cos(500*Math.PI*y), 2);

        double result = term1*term2 + term3*term4;
        return result;
    }

}