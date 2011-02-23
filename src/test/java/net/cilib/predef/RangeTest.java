/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.cilib.predef;

import org.hamcrest.Matcher;
import org.hamcrest.Factory;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import com.google.common.base.Supplier;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author gpampara
 */
public class RangeTest {

    @Test
    public void creation() {
        Range r = Range.of(-10, 20);

        Assert.assertThat(r.begin(), equalTo(-10));
        Assert.assertThat(r.end(), equalTo(20));
    }

    @Test(expected = IllegalStateException.class)
    public void invalidArguments() {
        Range.of(5, -2);
    }

    @Test
    public void supplierGivenRandomProvider() {
        Range r = Range.of(-10, 20);
        Supplier<Double> supplier = r.toRandomSupplier(new MersenneTwister());

        Assert.assertThat(supplier.get().doubleValue(), IsBetween.isBetween(r.begin(), r.end()));
    }

    @Test
    public void iterator() {
        for (int i : Range.ofWithIncrement(1, 2, 1)) {
            System.out.println(i);
        }
    }

    private static class IsBetween extends TypeSafeMatcher<Double> {

        private final double first;
        private final double second;

        private IsBetween(double first, double second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public boolean matchesSafely(Double item) {
            double value = item.doubleValue();
            return value >= first && value <= second;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("value must be between [" + first + "] and [" + second + "]");
        }

        @Factory
        public static <T> Matcher<Double> isBetween(double first, double second) {
            return new IsBetween(first, second);
        }
    }
}
