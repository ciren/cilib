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
package net.sourceforge.cilib.pso.iterationstrategies;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import fj.P1;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.comparator.SocialBestFitnessComparator;
import net.sourceforge.cilib.math.random.CauchyDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.crossover.operations.MultiParentCrossoverOperation;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;

public class GBestMutationIterationStrategy extends AbstractIterationStrategy<PSO> {
    
    private ControlParameter vMax;
    private IterationStrategy<PSO> delegate;
    private ProbabilityDistributionFuction distribution;
    
    public GBestMutationIterationStrategy() {
        PSOCrossoverIterationStrategy del = new PSOCrossoverIterationStrategy();
        del.setCrossoverOperation(new MultiParentCrossoverOperation());
        this.delegate = del;
        
        this.vMax = ConstantControlParameter.of(1.0);
        this.distribution = new CauchyDistribution();
    }
    
    public GBestMutationIterationStrategy(GBestMutationIterationStrategy copy) {
        this.vMax = copy.vMax.getClone(); 
        this.delegate = copy.delegate.getClone();
        this.distribution = copy.distribution;
    }

    @Override
    public AbstractIterationStrategy<PSO> getClone() {
        return new GBestMutationIterationStrategy(this);
    }

    @Override
    public void performIteration(PSO algorithm) {
        delegate.performIteration(algorithm);
        Topology<Particle> topology = algorithm.getTopology();
        
        // calculate vAvg
        Vector avgV = Vectors.mean(Lists.transform(topology, new Function<Particle, Vector>() {
            @Override
            public Vector apply(Particle f) {
                return (Vector) f.getVelocity();
            }            
        }));
        
        Vector.Builder builder = Vector.newBuilder();
        for (Numeric n : avgV) {
            if (Math.abs(n.doubleValue()) > vMax.getParameter()) {
                builder.add(vMax.getParameter());
            } else {
                builder.add(n);
            }
        }
        
        avgV = builder.build();
        
        // mutation
        Particle gBest = Topologies.getBestEntity(topology, new SocialBestFitnessComparator());
        Particle mutated = gBest.getClone();
        Vector pos = (Vector) gBest.getBestPosition();
        final Bounds bounds = pos.boundsOf(0);
        
        pos = pos.plus(avgV.multiply(new P1<Number>() {
            @Override
            public Number _1() {
                return distribution.getRandomNumber()*bounds.getRange() + bounds.getLowerBound();
            }
        }));
        
        mutated.setCandidateSolution(pos);
        mutated.calculateFitness();
        
        if (gBest.getBestFitness().compareTo(mutated.getFitness()) < 0) {
            gBest.getProperties().put(EntityType.Particle.BEST_FITNESS, mutated.getBestFitness());
            gBest.getProperties().put(EntityType.Particle.BEST_POSITION, mutated.getBestPosition());
        }
    }
    
    public void setVMax(ControlParameter vMax) {
        this.vMax = vMax;
    }

    public ControlParameter getVMax() {
        return vMax;
    }
    
    public void setDistribution(ProbabilityDistributionFuction distribution) {
        this.distribution = distribution;
    }

    public ProbabilityDistributionFuction getDistribution() {
        return distribution;
    }

    public IterationStrategy<PSO> getDelegate() {
        return delegate;
    }

    public void setDelegate(IterationStrategy<PSO> delegate) {
        this.delegate = delegate;
    }
}
