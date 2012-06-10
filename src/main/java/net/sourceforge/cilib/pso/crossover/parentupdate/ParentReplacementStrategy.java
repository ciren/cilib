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
package net.sourceforge.cilib.pso.crossover.parentupdate;

import fj.F2;
import java.util.List;
import net.sourceforge.cilib.entity.Particle;

/**
 * These classes determine which particles replace the parents after particle crossover.
 */
public abstract class ParentReplacementStrategy extends F2<List<Particle>, List<Particle>, List<Particle>> {    
    @Override
    public abstract List<Particle> f(List<Particle> parents, List<Particle> offspring);    
}
