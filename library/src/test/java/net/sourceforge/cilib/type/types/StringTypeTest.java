/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type.types;

import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;

/**
 *
 */
public class StringTypeTest {

    @Test
    public void testClone() {
        StringType t = new StringType("test string");
        StringType clone = t.getClone();
        Assert.assertTrue(t.getString().equals(clone.getString()));
        Assert.assertEquals(t.getString(), clone.getString());
    }

    @Test
    public void testDimensionality() {
        StringType s = new StringType("This is a StringType");
        Assert.assertEquals(1, Types.dimensionOf(s));
    }

    @Test
    public void whitespaceReplacement() {
        StringType type = new StringType("This is a string with whitespace");
        Assert.assertThat(type.toString(), is(equalTo("This_is_a_string_with_whitespace")));
    }

}
