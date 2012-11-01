/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.math.random.generator;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * TODO: This random number tester is extremely primitive. We need to implement something like the die-hard test suite to test for simulation quality.
 *
 */
public class SimpleRandomTester implements RandomTester {

    /** Creates a new instance of SimpleRandomTester */
    public SimpleRandomTester() {
        samples = new ArrayList<Double>();
    }

    public boolean hasRandomSamples() {
        int l = 1;
        int n1 = 0;
        int n2 = 0;

        Iterator<Double> i = samples.iterator();
        double previous = i.next().doubleValue();
        if (previous >= 0.5) {
            ++n1;
        }
        else {
            ++n2;
        }
        double current;
        while (i.hasNext()) {
            current = i.next().doubleValue();
            if (current >= 0.5) {
                ++n1;
                if (previous < 0.5) {
                    ++l;
                }
            }
            else {
                ++n2;
                if (previous >= 0.5) {
                    ++l;
                }
            }
            previous = current;
        }

        if (n1 == 0 || n2 == 0) {
            return false;
        }

        double u = ((double) 2 * n1 * n2) / ((double) n1 + n2) + 1;
        double s = ((double) 2 * n1 * n2 * (2 * n1 * n2 - n1 - n2))
                 / ((double) (n1 + n2) * (n1 + n2) * (n1 + n2 - 1));
        double z = ((double) l - u) / s;

        if (z <= -2.5796 || z >= 2.5796) {
            return false;
        }
        else {
            return true;
        }
    }

    public void addSample(double number) {
        samples.add(new Double(number));
    }

    private ArrayList<Double> samples;
}
