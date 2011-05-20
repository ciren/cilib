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
package net.cilib.algorithm;

import fj.F;

/**
 *
 */
public abstract class Predicate<T> extends F<T, Boolean> {
    
    private static final Predicate<Boolean> ALWAYS_TRUE = new Predicate<Boolean>() {
        @Override
        public Boolean f(Boolean a) {
            return true;
        }
    };
    
    private static final Predicate<Boolean> ALWAYS_FALSE = new Predicate<Boolean>() {
        @Override
        public Boolean f(Boolean a) {
            return false;
        }
    };
    
    public static Predicate<Boolean> alwaysTrue() {
        return ALWAYS_TRUE;
    }
    
    public static Predicate<Boolean> alwaysFalse() {
        return ALWAYS_FALSE;
    }
}
