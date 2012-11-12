/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.decorators;

import net.sourceforge.cilib.functions.continuous.unconstrained.Rastrigin;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class AngleModulationTest {

    @Test
    public void testObjectDimensionality() {
        AngleModulation angle = new AngleModulation();
        Vector builtRepresentation = (Vector) angle.getDomain().getBuiltRepresentation();

        assertEquals(4, builtRepresentation.size());
    }

    @Test(expected = ArithmeticException.class)
    public void testSetPrecision() {
        AngleModulation angle = new AngleModulation();
        angle.setPrecision(4);
        assertEquals(4, angle.getPrecision());

        angle.setPrecision(0);
        assertEquals(0, angle.getPrecision());

        angle.setPrecision(-1);
    }

    @Test
    public void testCalcuateRequiredBits() {
        AngleModulation angle = new AngleModulation();

        DomainRegistry registry = new StringBasedDomainRegistry();
        registry.setDomainString("R(-5.12:5.12)^30");

        angle.setPrecision(3);
        assertEquals(14, angle.getRequiredNumberOfBits(registry));

        angle.setPrecision(2);
        assertEquals(10, angle.getRequiredNumberOfBits(registry));
    }

    @Test
    public void testSetDecoratedFunctionDomain() {
        AngleModulation angle = new AngleModulation();
        FunctionOptimisationProblem delegate = new FunctionOptimisationProblem();
        delegate.setDomain("R(-5.12:5.12)^30");
        delegate.setFunction(new Rastrigin());
        angle.setProblem(delegate);
        Assert.assertTrue(angle.getProblem().getFunction() instanceof Rastrigin);
    }

    /**
     * This test uses Rastrigin which has the default domain of R(-5.12, 5.12)^30
     *
     * Converting this to bits results in the expected number being:
     */
    @Test
    public void testConversionToBitRepresentationLength() {
        AngleModulation angle = new AngleModulation();
        FunctionOptimisationProblem delegate = new FunctionOptimisationProblem();
        delegate.setDomain("R(-5.12:5.12)^30");
        delegate.setFunction(new Rastrigin());
        angle.setProblem(delegate);

        Vector testVector = Vector.of(0,1,1,0);
        String converted = angle.generateBitString(testVector, 14);
        assertEquals(420, converted.length());
    }

    @Test
    public void testBinaryConversion() {
        AngleModulation angle = new AngleModulation();
        FunctionOptimisationProblem delegate = new FunctionOptimisationProblem();
        delegate.setDomain("R(-5.12:5.12)^30");
        delegate.setFunction(new Rastrigin());
        angle.setProblem(delegate);

        String test = "1111";
        String test2 = "1010";

        assertEquals(15.0, angle.valueOf(test), Double.MIN_NORMAL);
        assertEquals(15.0, angle.valueOf(test, 0, 4), Double.MIN_NORMAL);
        assertEquals(3.0, angle.valueOf(test, 2), Double.MIN_NORMAL);
        assertEquals(10.0, angle.valueOf(test2, 0, 4), Double.MIN_NORMAL);
    }
}
