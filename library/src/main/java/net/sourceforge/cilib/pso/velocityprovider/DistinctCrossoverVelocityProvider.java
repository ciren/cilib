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
package net.sourceforge.cilib.pso.velocityprovider;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.real.ParentCentricCrossoverStrategy;
import net.sourceforge.cilib.pso.positionprovider.LinearPositionProvider;
import net.sourceforge.cilib.pso.positionprovider.StandardPositionProvider;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * A VelocityProvider that applies crossover to the nBest, pBest and position of a particle if they are distinct.
 * Otherwise, it tries getting distinct vectors from the nBest, pBest, position and previous position.
 * If there still aren't 3 distinct vectors it applies an alternative crossover to two vectors.
 * If there is only one distinct vector a delegate velocity provider is applied to the particle.
 *
 * <p>
 * "Enhancing Performance of Particle Swarm Optimization Through an Algorithmic Linking with Genetic Algorithms",
 * K Deb, N. Padhye, Swarm and Evolutionary Computation Journal, 2012
 * </p>
 */
public class DistinctCrossoverVelocityProvider implements VelocityProvider {
    private CrossoverStrategy mainCrossover;
    private CrossoverStrategy alternateCrossover;
    private VelocityProvider delegate;

    /**
     * Default constructor.
     */
    public DistinctCrossoverVelocityProvider() {
        this.mainCrossover = new ParentCentricCrossoverStrategy();
        ParentCentricCrossoverStrategy crossover = new ParentCentricCrossoverStrategy();
        crossover.setSigma2(ConstantControlParameter.of(0.0));
        this.alternateCrossover = crossover;
        this.delegate = new StandardVelocityProvider();
    }

    /**
     * Copy constructor.
     * @param copy
     */
    public DistinctCrossoverVelocityProvider(DistinctCrossoverVelocityProvider copy) {
        this.mainCrossover = copy.mainCrossover.getClone();
        this.alternateCrossover = copy.alternateCrossover.getClone();
        this.delegate = copy.delegate.getClone();
    }

    /**
     * Clones this instance
     *
     * @return the clone
     */
    @Override
    public DistinctCrossoverVelocityProvider getClone() {
        return new DistinctCrossoverVelocityProvider(this);
    }

    private Vector applyCrossover(Particle particle, List<Vector> parents, CrossoverStrategy crossover) {
        List<Entity> entityParents = Lists.newLinkedList();

        for (Vector v : parents) {
            Entity parent = particle.getClone();
            parent.setCandidateSolution(v);
            entityParents.add(parent);
        }

        return (Vector) crossover.crossover(entityParents).get(0).getCandidateSolution();
    }

    /**
     * Returns the new position
     *
     * @param particle The particle to update
     * @return  the particle's new position
     */
    @Override
    public Vector get(Particle particle) {
        particle.setPositionProvider(new LinearPositionProvider());
        
        Vector solution = (Vector) particle.getCandidateSolution();
        Vector pBest = (Vector) particle.getBestPosition();
        Vector nBest = (Vector) particle.getNeighbourhoodBest().getBestPosition();

        Set<Vector> solutions = new HashSet<Vector>(Arrays.asList(solution, pBest, nBest));

        if (solutions.size() == 3) {
            return applyCrossover(particle, Lists.newLinkedList(solutions), mainCrossover);
        }

        Vector prevPos = (Vector) particle.getProperties().get(EntityType.PREVIOUS_SOLUTION);
        solutions.add(prevPos);

        if (solutions.size() == 3) {
            return applyCrossover(particle, Lists.newLinkedList(solutions), mainCrossover);
        }

        if (solutions.size() == 2) {
            return applyCrossover(particle, Lists.newLinkedList(solutions), alternateCrossover);
        }
        
        particle.setPositionProvider(new StandardPositionProvider());
        return delegate.get(particle);
    }

    public void setMainCrossover(CrossoverStrategy crossoverStrategy) {
        this.mainCrossover = crossoverStrategy;
    }

    public VelocityProvider getDelegate() {
        return delegate;
    }

    public void setDelegate(VelocityProvider delegate) {
        this.delegate = delegate;
    }

    public CrossoverStrategy getMainCrossover() {
        return mainCrossover;
    }

    public CrossoverStrategy getAlternateCrossover() {
        return alternateCrossover;
    }

    public void setAlternateCrossover(CrossoverStrategy alternateCrossover) {
        this.alternateCrossover = alternateCrossover;
    }
}
