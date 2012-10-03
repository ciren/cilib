package net.sourceforge.cilib.functions.continuous.unconstrained;

import static org.junit.Assert.assertEquals;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.Before;
import org.junit.Test;

public class ZettleTest {

    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new Zettle();
    }

    /** Test of evaluate method, of class Zettle. */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(-0.0299, 0);
        assertEquals(-0.003791, function.apply(x), 0.000009);
        
    }
}
