/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type.parser;

import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.StringType;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests related to the parsing of domain strings.
 */
public class DomainParserTest {

    /**
     * Creation of {@code StringType}.
     */
    @Test
    public void stringType() {
        StructuredType vector = DomainParser.parse("T");

        Assert.assertEquals(1, vector.size());
    }

    /**
     * The default kind of domain string that would be quite common.
     */
    @Test
    public void dimensionRange() {
        Vector vector = (Vector) DomainParser.parse("R(-9.0:9.0)^6");

        Assert.assertEquals(6, vector.size());
    }

    @Test
    public void infiniteBounds() {
        Vector vector = (Vector) DomainParser.parse("R^6");

        Assert.assertEquals(6, vector.size());
    }

    @Test
    public void singleInfiniteBounds() {
        Vector vector = (Vector) DomainParser.parse("R");

        Assert.assertEquals(1, vector.size());
    }

    @Test(expected=RuntimeException.class)
    public void invalidDomain() {
        DomainParser.parse("Y");
    }

    @Test(expected=RuntimeException.class)
    public void parseNotValid() {
        DomainParser.parse("R(-5, -4, -5)^-7");
    }

    @Test(expected=RuntimeException.class)
    public void negativeExponent() {
        DomainParser.parse("R^-9");
    }

    @Test(expected=RuntimeException.class)
    public void zeroExponent() {
        DomainParser.parse("R^0");
    }

    @Test
    public void integerBounds() {
        DomainParser.parse("R(1:3)");
        DomainParser.parse("R(-1:3)");
        DomainParser.parse("R(-3:-1)");
        DomainParser.parse("R(-3:-1)^9");
    }

    @Test(expected=RuntimeException.class)
    public void incorrectBoundsOrder() {
        DomainParser.parse("R(3:2)"); // Lower bound > Upper bound = WRONG!
    }

     @Test
    public void testParseReal() {
        DomainParser.parse("R(0.0:9.0)");
        DomainParser.parse("R");
        DomainParser.parse("R^6");
        DomainParser.parse("R(-9.0:9.0)");
        DomainParser.parse("R(-30.0:30.0)^6");
    }


    @Test
    public void testParseBit() {
        DomainParser.parse("B");
        DomainParser.parse("B^6");
    }


    @Test
    public void testParseInteger() {
        DomainParser.parse("Z");
        DomainParser.parse("Z(-1:0)");
//        Parser.parse("Z(1)");
        DomainParser.parse("Z(0:1)");
        DomainParser.parse("Z(-999:999)");
        DomainParser.parse("Z^8");
        DomainParser.parse("Z(0:1)^10");
    }


    @Test
    public void testParseString() {
        DomainParser.parse("T^5");
    }

    @Test
    public void vectorReturn() {
        StructuredType type = DomainParser.parse("R^5");
        Assert.assertThat(type, is(Vector.class));
    }

    @Test
    public void boundsNotSwappedVector() {
        Vector vector = (Vector) DomainParser.parse("R(0:100),R(100:200)");

        Assert.assertThat(vector.boundsOf(0).toString(), is("(0.0:100.0)"));
        Assert.assertThat(vector.boundsOf(1).toString(), is("(100.0:200.0)"));
    }

    @Test
    public void boundsNotSwappedTypeList() {
        TypeList vector = (TypeList) DomainParser.parse("T, T, T");

        Assert.assertThat(vector.size(), is(3));

        Assert.assertTrue(vector.get(0) instanceof StringType);
        Assert.assertTrue(vector.get(1) instanceof StringType);
        Assert.assertTrue(vector.get(2) instanceof StringType);
    }
}
