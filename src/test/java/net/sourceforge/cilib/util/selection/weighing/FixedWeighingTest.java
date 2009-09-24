/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.util.selection.weighing;

import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.util.selection.Selection;
import net.sourceforge.cilib.util.selection.Selection.Entry;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author gpampara
 */
<<<<<<< HEAD:src/test/java/net/sourceforge/cilib/util/selection/weighing/FixedWeighingTest.java
public class FixedWeighingTest {

    @Test
    public void fixedWeighing() {
        List<Integer> elements = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<Entry<Integer>> weighedElements = Selection.from(elements).weigh(new FixedWeighing(1.0)).entries();
        for (int i = 0; i < weighedElements.size(); ++i) {
            Assert.assertEquals(1.0, weighedElements.get(i).getWeight(), 0.0001);
        }
=======
public class Step extends ContinuousFunction {

    private static final long serialVersionUID = -3888436745417400797L;

    /**
     * Create an instance of the {@code Step} function.
     */
    public Step() {
        setDomain("R(-100.0, 100.0)^6");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Step getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double evaluate(Vector input) {
        double sum = 0.0;

        for (int i = 0; i < input.getDimension(); i++) {
            double value = Math.floor(input.getReal(i));
            sum += value*value;
        }

        return sum;
>>>>>>> Corrected implementation of Step function.:src/main/java/net/sourceforge/cilib/functions/continuous/Step.java
    }

}
