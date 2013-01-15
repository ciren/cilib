/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.io.pattern;

import net.sourceforge.cilib.type.types.StringType;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class StandardPatternTest {

    private Vector feature;
    private StringType klas;
    private StandardPattern pattern1;
    private StandardPattern pattern2;
    private StandardPattern pattern3;

    @Before
    public void setup() {
        feature = Vector.of(0.1, 0.2, 0.3, 0.4, 0.5);
        klas = new StringType("class1");

        pattern1 = new StandardPattern();
        pattern1.setVector(feature);
        pattern1.setTarget(klas);

        pattern2 = new StandardPattern(feature, klas);
        pattern3 = new StandardPattern(pattern1);
    }


    @Test
    public void testConstructors() {
        Assert.assertEquals(feature, pattern1.getVector());
        Assert.assertEquals(feature, pattern2.getVector());
        Assert.assertEquals(feature, pattern3.getVector());

        Assert.assertEquals(klas, pattern1.getTarget());
        Assert.assertEquals(klas, pattern2.getTarget());
        Assert.assertEquals(klas, pattern3.getTarget());
    }
}
