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

import fj.F;
import fj.F2;

/**
 *
 * @author filipe
 */
public class BasicFunctions {
    private BasicFunctions() {
        throw new UnsupportedOperationException();
    }

    /**
     * Base helper functions
     */
    public static final F<Double, Double> pi = new F<Double, Double>() {
        @Override
        public Double f(Double a) {
            return a * Math.PI;
        }
    };
    
    public static final F<Integer, Integer> succ = new F<Integer, Integer>() {
        @Override
        public Integer f(Integer a) {
            return a + 1;
        }
    };
    
    public static final F<Integer, Integer> pred = new F<Integer, Integer>() {
        @Override
        public Integer f(Integer a) {
            return a - 1;
        }
    };
    
    /**
     * Lifted identity function.
     */
    public static final F<Double, Double> identity = new F<Double, Double>() {
        @Override
        public Double f(Double a) {
            return a;
        }
    };
    
    public static final F2<Double, Double, Double> pow = new F2<Double, Double, Double>() {
        @Override
        public Double f(Double a, Double b) {
            return Math.pow(a, b);
        }
    };
    
    public static final F<Double, Double> abs = new F<Double, Double>() {
        @Override
        public Double f(Double a) {
            return Math.abs(a);
        }
    };
    
    public static final F<Double, Double> square = new F<Double, Double>() {
        @Override
        public Double f(Double a) {
            return a * a;
        }
    };
    
    public static final F<Double, Double> sqrt = new F<Double, Double>() {
        @Override
        public Double f(Double a) {
            return Math.sqrt(a);
        }
    };
    
    public static final F<Double, Double> cos = new F<Double, Double>() {
        @Override
        public Double f(Double a) {
            return Math.cos(a);
        }
    };
    
    public static final F<Double, Double> sin = new F<Double, Double>() {
        @Override
        public Double f(Double a) {
            return Math.sin(a);
        }
    };
    
    public static final F<Double, Double> ceil = new F<Double, Double>() {
        @Override
        public Double f(Double a) {
            return Math.ceil(a);
        }
    };
    
    public static final F<Double, Double> floor = new F<Double, Double>() {
        @Override
        public Double f(Double a) {
            return Math.floor(a);
        }
    };
}
