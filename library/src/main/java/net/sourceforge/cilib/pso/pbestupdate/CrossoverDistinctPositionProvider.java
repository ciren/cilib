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
package net.sourceforge.cilib.pso.pbestupdate;

import java.util.Arrays;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.real.ParentCentricCrossoverStrategy;
import net.sourceforge.cilib.type.types.container.Vector;

public class CrossoverDistinctPositionProvider extends DistinctPositionProvider {

    private CrossoverStrategy crossoverStrategy;
    
    public CrossoverDistinctPositionProvider() {
        this.crossoverStrategy = new ParentCentricCrossoverStrategy();
    }
    
    public CrossoverDistinctPositionProvider(CrossoverDistinctPositionProvider copy) {
        this.crossoverStrategy = copy.crossoverStrategy.getClone();
    }

    @Override
    public CrossoverDistinctPositionProvider getClone() {
        return new CrossoverDistinctPositionProvider(this);
    }
    
    @Override
    public Vector f(Particle particle) {
        Particle p1 = particle.getClone();
        Particle p2 = particle.getClone();
        Particle p3 = particle.getClone();
        
        p1.setCandidateSolution(particle.getCandidateSolution());
        p2.setCandidateSolution(particle.getBestPosition());
        p3.setCandidateSolution(particle.getNeighbourhoodBest().getBestPosition());

        return (Vector) crossoverStrategy.crossover(Arrays.asList(p1, p2, p3))
                .get(0).getCandidateSolution();
    }
    
    public void setCrossoverStrategy(CrossoverStrategy crossoverStrategy) {
        this.crossoverStrategy = crossoverStrategy;
    }

    public CrossoverStrategy getCrossoverStrategy() {
        return crossoverStrategy;
    }
}
