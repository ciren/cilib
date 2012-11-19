/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.functions;

import fj.Equal;
import fj.F;
import fj.F2;
import net.sourceforge.cilib.type.types.Bit;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;

public final class Numerics {
    
    private Numerics() {}
    
    public static <N extends Numeric> F<N, Double> doubleValue() {
        return new F<N, Double>() {
            @Override
            public Double f(N a) {
                return a.doubleValue();
            }
        };
    }
    
    public static <N extends Numeric> F<N, Boolean> booleanValue() {
        return new F<N, Boolean>() {
            @Override
            public Boolean f(N a) {
                return a.booleanValue();
            }
        };
    }
    
    public static <N extends Numeric> F<N, Integer> intValue() {
        return new F<N, Integer>() {
            @Override
            public Integer f(N a) {
                return a.intValue();
            }
        };
    }
    
    public static <N extends Numeric> F<N, String> getRepresentation() {
        return new F<N, String>() {
            @Override
            public String f(N a) {
                return a.getRepresentation();
            }
        };
    }

    public static <N extends Number> F<N, Real> real() {
        return new F<N, Real>() {
            @Override
            public Real f(N a) {
                return Real.valueOf(a.doubleValue());
            }
        };
    }
    
    public static <N extends Number> F<N, Int> integer() {
        return new F<N, Int>() {
            @Override
            public Int f(N a) {
                return Int.valueOf(a.intValue());
            }
        };
    }
    
    public static <N extends Number> F<N, Bit> bit() {
        return new F<N, Bit>() {
            @Override
            public Bit f(N a) {
                return Bit.valueOf(a.doubleValue() == 0.0);
            }
        };
    }
    
    public static <N extends Number> F<N, Numeric> numeric() {
        return new F<N, Numeric>() {
            @Override
            public Numeric f(N a) {
                return Real.valueOf(a.doubleValue());
            }
        };
    }
    
    public static F2<Numeric, Numeric, Boolean> equalF() {
        return new F2<Numeric, Numeric, Boolean>() {
            @Override
            public Boolean f(Numeric a, Numeric b) {
                return a.equals(b);
            }
        };
    }
    
    public static Equal<Numeric> equal() {
        return Equal.<Numeric>equal(equalF().curry());
    }
}
