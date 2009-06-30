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
package net.sourceforge.cilib.math.random.generator.quasi;

import net.sourceforge.cilib.math.random.generator.Random;

/**
 * TODO: Need to complete javadoc.
 *
 */
public abstract class QuasiRandom extends Random {
    private static final long serialVersionUID = -1631441422804523649L;

    protected int dimensions;
    protected int skipValue;

    public QuasiRandom(long seed) {
        super(seed);
        this.dimensions = 3;
        this.skipValue = 0;
    }

    public void setDimensions(int dimensions) {
        this.dimensions = dimensions;
    }

    public int getDimensions() {
        return this.dimensions;
    }

    public void setSkipValue(int skipValue) {
        this.skipValue = skipValue;
    }

    public int getSkipValue() {
        return this.skipValue;
    }

    public abstract double[] nextPoint();
}
