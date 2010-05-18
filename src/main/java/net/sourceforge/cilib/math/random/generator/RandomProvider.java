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
package net.sourceforge.cilib.math.random.generator;

/**
 * This interface provides a replacement API for the standard java.util.Random
 * classes and API. The reason for _not_ using the standard Java API is due to
 * the API maintaining a large amount of static state. As a result, the classes
 * implementing this interface are all self contained and should be as immutable
 * as possible.
 */
public interface RandomProvider extends net.sourceforge.cilib.util.Cloneable {

    @Override
    RandomProvider getClone();

    boolean nextBoolean();

    int nextInt();

    int nextInt(int n);

    long nextLong();

    float nextFloat();

    double nextDouble();

    void nextBytes(byte[] bytes);

}
