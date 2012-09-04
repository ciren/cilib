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
package net.sourceforge.cilib.pso.positionprovider;

import fj.P1;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.type.types.container.Vector;

public class VectorBasedPositionProvider implements PositionProvider {

    private ControlParameter granularity;
    private PositionProvider delegate;

    public VectorBasedPositionProvider() {
        this.granularity = ConstantControlParameter.of(0.5);
        this.delegate = new StandardPositionProvider();
    }

    public VectorBasedPositionProvider(VectorBasedPositionProvider copy) {
        this.granularity = copy.granularity.getClone();
        this.delegate = copy.delegate.getClone();
    }

    @Override
    public PositionProvider getClone() {
        return new VectorBasedPositionProvider(this);
    }

    @Override
    public Vector get(Particle particle) {
        Vector newPos = delegate.get(particle);

        Particle tmp = particle.getClone();
        tmp.setCandidateSolution(newPos);
        Fitness newFitness = particle.getFitnessCalculator().getFitness(tmp);

        final UniformDistribution uniform = new UniformDistribution();
        Vector newPBest = newPos.multiply(new P1<Number>() {
            @Override
            public Number _1() {
                return uniform.getRandomNumber(-granularity.getParameter(), granularity.getParameter());
            }
        }).plus(newPos);
        tmp.setCandidateSolution(newPos);
        Fitness newPBestFitness = particle.getFitnessCalculator().getFitness(tmp);

        if (newPBestFitness.compareTo(newFitness) < 0) {
            Vector tmpVector = Vector.copyOf(newPos);
            newPos = newPBest;
            newPBest = tmpVector;
        }

        double dot = ((Vector) particle.getNeighbourhoodBest().getBestPosition())
                .subtract(newPos).dot(newPBest.subtract(newPos));

        if (dot < 0) {
            return (Vector) particle.getPosition();
        }

        return newPos;
    }

    public void setDelegate(PositionProvider delegate) {
        this.delegate = delegate;
    }

    public PositionProvider getDelegate() {
        return delegate;
    }

    public void setGranularity(ControlParameter granularity) {
        this.granularity = granularity;
    }

    public ControlParameter getGranularity() {
        return granularity;
    }
}
