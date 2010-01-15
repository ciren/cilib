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
