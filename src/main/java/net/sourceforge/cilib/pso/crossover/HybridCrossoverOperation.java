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
package net.sourceforge.cilib.pso.crossover;

import net.sourceforge.cilib.pso.crossover.util.HybridOffspringUpdateStrategy;
import net.sourceforge.cilib.pso.crossover.util.OffspringUpdateStrategy;
import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.comparator.SocialBestFitnessComparator;
import net.sourceforge.cilib.entity.operators.crossover.ArithmeticCrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.util.selection.Samples;

/**
 * A crossover operation for PSOs that selects particles for crossover and updates 
 * the offspring using a given OffspringUpdateStrategy.
 * 
 * <p>
 * @INPROCEEDINGS{Løvbjerg01hybridparticle,
 *   author = {Morten Løvbjerg and Thomas Kiel Rasmussen and Thiemo Krink},
 *   title = {Hybrid Particle Swarm Optimiser with Breeding and Subpopulations},
 *   booktitle = {Proceedings of the Genetic and Evolutionary Computation Conference (GECCO-2001},
 *   year = {2001},
 *   pages = {469--476},
 *   publisher = {Morgan Kaufmann}
 * }
 * </p>
 */
public class HybridCrossoverOperation implements PSOCrossoverOperation {
    
    private CrossoverStrategy crossoverStrategy;
    private OffspringUpdateStrategy offspringUpdate;
    
    public HybridCrossoverOperation() {
        this.offspringUpdate = new HybridOffspringUpdateStrategy();
        this.crossoverStrategy = new ArithmeticCrossoverStrategy();
        this.crossoverStrategy.setCrossoverProbability(ConstantControlParameter.of(0.2));
    }
    
    public HybridCrossoverOperation(HybridCrossoverOperation copy) {
        this.offspringUpdate = copy.offspringUpdate;
        this.crossoverStrategy = copy.crossoverStrategy.getClone();
        this.crossoverStrategy.setCrossoverProbability(ConstantControlParameter.of(0.2));
    }
    
    @Override
    public HybridCrossoverOperation getClone() {
        return new HybridCrossoverOperation(this);
    }

    @Override
    public Topology<Particle> performCrossoverOpertation(PSO pso) {
        Topology<Particle> topology = pso.getTopology();
        UniformDistribution uniform = new UniformDistribution();        
        List<Particle> parents = Lists.newArrayList();
        List<Particle> offspring;
        
        Topology<Particle> newTopology = topology.getClone();
        newTopology.clear();
        
        for (Particle p : topology) {
            if (uniform.getRandomNumber() < crossoverStrategy.getCrossoverProbability().getParameter()) {
                parents.add(p);
            } else {
                newTopology.add(p);
            }
        }

        while (!parents.isEmpty()) {
            // need two parents to perform crossover
            if (parents.size() == 1) {
                newTopology.add((Particle) parents.remove(0));
                break;
            }
            
            List<Particle> selectedParents = crossoverStrategy.getSelectionStrategy().on(parents).select(Samples.first(2));
            offspring = (List<Particle>) crossoverStrategy.crossover(selectedParents);
            
            parents.removeAll(selectedParents);
            
            newTopology.addAll(offspringUpdate.updateOffspring(selectedParents, offspring));
        }
        
        for (Particle p : newTopology) {
            Particle nBest = Topologies.getNeighbourhoodBest(newTopology, p, new SocialBestFitnessComparator());
            p.setNeighbourhoodBest(nBest);
        }

        return newTopology;
    }

    public CrossoverStrategy getCrossoverStrategy() {
        return crossoverStrategy;
    }

    public void setCrossoverStrategy(CrossoverStrategy crossoverStrategy) {
        this.crossoverStrategy = crossoverStrategy;
    }

    public void setOffspringUpdate(OffspringUpdateStrategy offspringUpdate) {
        this.offspringUpdate = offspringUpdate;
    }

    public OffspringUpdateStrategy getOffspringUpdate() {
        return offspringUpdate;
    }
}
