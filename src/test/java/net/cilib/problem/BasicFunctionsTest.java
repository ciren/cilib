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
package net.cilib.problem;

import fj.F2;
import fj.F;
import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.number.IsCloseTo.*;

/**
 *
 * @author filipe
 */
public class BasicFunctionsTest {
    @Test
    public void abs() {
        F<Double, Double> f = BasicFunctions.abs;

        Assert.assertEquals(f.f(0.5), new Double(0.5));
        Assert.assertEquals(f.f(-0.5), new Double(0.5));
        Assert.assertEquals(f.f(100.0), new Double(100));
        Assert.assertEquals(f.f(-100.0), new Double(100));
        Assert.assertEquals(f.f(1.0/3.0), new Double(1.0/3.0));
        Assert.assertEquals(f.f(-1.0/3.0), new Double(1.0/3.0));
        Assert.assertEquals(f.f(0.0), new Double(0.0));
    }
    
    @Test
    public void ceil() {
        F<Double, Double> f = BasicFunctions.ceil;

        Assert.assertEquals(f.f(0.5), new Double(1.0));
        Assert.assertEquals(f.f(5.49), new Double(6.0));
        Assert.assertEquals(f.f(8.01), new Double(9.0));
        Assert.assertEquals(f.f(4.0), new Double(4.0));
    }
    
    @Test
    public void floor() {
        F<Double, Double> f = BasicFunctions.floor;

        Assert.assertEquals(f.f(0.5), new Double(0.0));
        Assert.assertEquals(f.f(5.49), new Double(5.0));
        Assert.assertEquals(f.f(8.01), new Double(8.0));
        Assert.assertEquals(f.f(4.0), new Double(4.0));
        Assert.assertEquals(f.f(-4.9), new Double(-5.0));
    }
    
    @Test
    public void cos() {
        F<Double, Double> f = BasicFunctions.cos;

        Assert.assertThat(f.f(0.0), closeTo(1.0, 0.00000001));
        Assert.assertThat(f.f(Math.PI), closeTo(-1.0, 0.00000001));
        Assert.assertThat(f.f(Math.PI*4/3), closeTo(-0.5, 0.00000001));
        Assert.assertThat(f.f(3.0), closeTo(-0.989992496, 0.00000001));
        Assert.assertThat(f.f(Math.PI/3), closeTo(0.5, 0.00000001));
        Assert.assertThat(f.f(1.0), closeTo(0.540302305, 0.00000001));
    }
    
    @Test
    public void sin() {
        F<Double, Double> f = BasicFunctions.sin;

        Assert.assertThat(f.f(0.0), closeTo(0.0, 0.00000001));
        Assert.assertThat(f.f(Math.PI), closeTo(0.0, 0.00000001));
        Assert.assertThat(f.f(Math.PI*4/3), closeTo(-0.866025403, 0.00000001));
        Assert.assertThat(f.f(3.0), closeTo(0.141120008, 0.00000001));
        Assert.assertThat(f.f(Math.PI/3), closeTo(0.866025403, 0.00000001));
        Assert.assertThat(f.f(1.0), closeTo(0.841470984, 0.00000001));
    }
    
    @Test
    public void square() {
        F<Double, Double> f = BasicFunctions.square;

        Assert.assertThat(f.f(0.0), closeTo(0.0, 0.00000001));
        Assert.assertThat(f.f(2.0), closeTo(4.0, 0.00000001));
        Assert.assertThat(f.f(-2.0), closeTo(4.0, 0.00000001));
        Assert.assertThat(f.f(27.0), closeTo(729.0, 0.00000001));
        Assert.assertThat(f.f(Math.PI/3), closeTo(1.096622711, 0.00000001));
        Assert.assertThat(f.f(1.0), closeTo(1.0, 0.00000001));
    }
    
    @Test
    public void sqrt() {
        F<Double, Double> f = BasicFunctions.sqrt;

        Assert.assertThat(f.f(0.0), closeTo(0.0, 0.00000001));
        Assert.assertThat(f.f(Math.PI), closeTo(1.772453851, 0.00000001));
        Assert.assertThat(f.f(2.25), closeTo(1.5, 0.00000001));
        Assert.assertThat(f.f(100.0), closeTo(10.0, 0.00000001));
        Assert.assertThat(f.f(729.0), closeTo(27.0, 0.00000001));
        Assert.assertThat(f.f(1.0), closeTo(1.0, 0.00000001));
    }
    
    @Test
    public void succ() {
        F<Integer, Integer> f = BasicFunctions.succ;

        Assert.assertEquals(f.f(1), new Integer(2));
        Assert.assertEquals(f.f(-200), new Integer(-199));
        Assert.assertEquals(f.f(0), new Integer(1));
        Assert.assertEquals(f.f(39), new Integer(40));
        Assert.assertEquals(f.f(67), new Integer(68));
        Assert.assertEquals(f.f(22), new Integer(23));
    }
    
    @Test
    public void pred() {
        F<Integer, Integer> f = BasicFunctions.pred;

        Assert.assertEquals(f.f(1), new Integer(0));
        Assert.assertEquals(f.f(-200), new Integer(-201));
        Assert.assertEquals(f.f(0), new Integer(-1));
        Assert.assertEquals(f.f(39), new Integer(38));
        Assert.assertEquals(f.f(67), new Integer(66));
        Assert.assertEquals(f.f(22), new Integer(21));
    }
    
    @Test
    public void identity() {
        F<Double, Double> f = BasicFunctions.identity;

        Assert.assertEquals(f.f(0.5), new Double(0.5));
        Assert.assertEquals(f.f(5.49), new Double(5.49));
        Assert.assertEquals(f.f(-8.01), new Double(-8.01));
        Assert.assertEquals(f.f(4.0), new Double(4.0));
    }
    
    @Test
    public void pow() {
        F2<Double, Double, Double> f = BasicFunctions.pow;

        Assert.assertThat(f.f(0.0, 1.0), closeTo(0.0, 0.00000001));
        Assert.assertThat(f.f(Math.PI, 0.0), closeTo(1.0, 0.00000001));
        Assert.assertThat(f.f(50.0, 6.0), closeTo(1.5625e10, 0.00000001));
        Assert.assertThat(f.f(3.0, -2.0), closeTo(0.1111111111, 0.00000001));
        Assert.assertThat(f.f(-6.0, 2.0), closeTo(36.0, 0.00000001));
        Assert.assertThat(f.f(-4.0, -3.0), closeTo(-0.015625, 0.00000001));
    }
    
    @Test
    public void pi() {
        F<Double, Double> f = BasicFunctions.pi;

        Assert.assertThat(f.f(0.5), closeTo(1.570796327, 0.00000001));
        Assert.assertThat(f.f(2.3), closeTo(7.225663103, 0.00000001));
        Assert.assertThat(f.f(0.0), closeTo(0.0, 0.00000001));
        Assert.assertThat(f.f(1.0), closeTo(3.141592654, 0.00000001));
        Assert.assertThat(f.f(2.0), closeTo(6.283185307, 0.00000001));
    }
}
