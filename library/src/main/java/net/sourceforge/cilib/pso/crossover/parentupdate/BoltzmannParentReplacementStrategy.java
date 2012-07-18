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

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.util.selection.recipes.BoltzmannSelector;

/**
 * This ParentReplacementStrategy uses Boltzmann selection to determine if an 
 * offspring replaces a parent.
 */
public class BoltzmannParentReplacementStrategy extends ParentReplacementStrategy {
    private BoltzmannSelector<Particle> selector;
    
    public BoltzmannParentReplacementStrategy() {
        this.selector = new BoltzmannSelector<Particle>();
    }
    
    @Override
    public List<Particle> f(List<Particle> parents, List<Particle> offspring) {
        List<Particle> particles = Lists.newArrayList();
        
        for (int i = 0; i < Math.min(parents.size(), offspring.size()); i++) {
            particles.add(selector.on(Arrays.asList(parents.get(i), offspring.get(i))).select());
        }
        
        particles.addAll(parents.subList(particles.size(), parents.size()));
        
        return particles;
    }

    public void setBoltzmannSelector(BoltzmannSelector<Particle> selector) {
        this.selector = selector;
    }

    public BoltzmannSelector<Particle> getBoltzmannSelector() {
        return selector;
    }
}
