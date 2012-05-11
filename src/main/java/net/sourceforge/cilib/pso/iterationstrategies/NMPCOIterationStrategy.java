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

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.comparator.SocialBestFitnessComparator;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.MultiParentCrossoverStrategy;
import net.sourceforge.cilib.math.random.CauchyDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.selection.Samples;

public class NMPCOIterationStrategy extends AbstractIterationStrategy<PSO> {
    
    private CrossoverStrategy crossover;
    private ControlParameter vMax;
    private ProbabilityDistributionFuction distribution;
    
    public NMPCOIterationStrategy() {
        this.crossover = new MultiParentCrossoverStrategy();
        this.crossover.setCrossoverProbability(ConstantControlParameter.of(0.8));
        
        this.vMax = ConstantControlParameter.of(1.0);        
        this.distribution = new CauchyDistribution();
    }
    
    public NMPCOIterationStrategy(NMPCOIterationStrategy copy) {
        this.crossover = copy.crossover.getClone();
        this.distribution = copy.distribution;
        this.vMax = copy.vMax.getClone();
    }

    @Override
    public AbstractIterationStrategy<PSO> getClone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void performIteration(PSO algorithm) {
        Topology<Particle> topology = algorithm.getTopology();
        
        // pos/vel update
        for (Particle current : topology) {
            current.updateVelocity();
            current.updatePosition();

            boundaryConstraint.enforce(current);
            
            current.calculateFitness();
        }

        // crossover
        for (int i = 0; i < topology.size(); i++) {
            Particle p = topology.get(i);
            if (crossover.getRandomDistribution().getRandomNumber() < crossover.getCrossoverProbability().getParameter()) {
                List<Particle> parents = crossover.getSelectionStrategy().on(topology).select(Samples.first(3));
                parents.add(p);
                parents = Lists.reverse(parents);
                Particle offspring = (Particle) crossover.crossover(parents).get(0);
                offspring.calculateFitness();
                offspring.setNeighbourhoodBest(p.getNeighbourhoodBest());
                
                if (offspring.compareTo(p) > 0) {
                    topology.set(i, offspring);
                }
            }
        }
        
        // update neighbourhood
        for (Particle p : topology) {
            p.calculateFitness();
        
            Particle nBest = Topologies.getNeighbourhoodBest(topology, p, new SocialBestFitnessComparator());
            p.setNeighbourhoodBest(nBest);
        }
        
        // calculate vAvg
        Vector avgV = (Vector) topology.get(0).getVelocity();
        for (int i = 1; i < topology.size(); i++) {
            Vector v = (Vector) topology.get(1).getVelocity();
            avgV.plus(v);
        }
        
        avgV.divide(topology.size());
        
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
        
        pos = pos.plus(avgV.multiply(new Supplier<Number>() {
            @Override
            public Number get() {
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
    
}
