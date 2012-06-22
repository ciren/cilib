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
package net.sourceforge.cilib.clustering;

import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.algorithm.initialisation.DataDependantPopulationInitializationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.clustering.iterationstrategies.SinglePopulationDataClusteringIterationStrategy;
import net.sourceforge.cilib.clustering.iterationstrategies.StandardDataClusteringIterationStrategy;
import net.sourceforge.cilib.coevolution.cooperative.ParticipatingAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.contributionselection.ContributionSelectionStrategy;
import net.sourceforge.cilib.coevolution.cooperative.contributionselection.ZeroContributionSelectionStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.comparator.SocialBestFitnessComparator;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.problem.QuantizationErrorMinimizationProblem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author Kristina
 */
public class DataClusteringPSO extends SinglePopulationBasedAlgorithm implements ParticipatingAlgorithm {
    private Topology<ClusterParticle> topology;
    private SlidingWindow window;
    IterationStrategy<DataClusteringPSO> iterationStrategy;
    private ContributionSelectionStrategy contributionSelection;
    private boolean isExplorer;
    
    
    public DataClusteringPSO() {
        super();
        contributionSelection = new ZeroContributionSelectionStrategy();
        topology = new GBestTopology<ClusterParticle>();
        initialisationStrategy = new DataDependantPopulationInitializationStrategy<ClusterParticle>();
        window = new SlidingWindow();
        iterationStrategy = new StandardDataClusteringIterationStrategy();
        isExplorer = false;
    }

    public DataClusteringPSO(DataClusteringPSO copy) {
        super(copy);
        topology = copy.topology;
        initialisationStrategy = copy.initialisationStrategy;
        window = copy.window;
        iterationStrategy = copy.iterationStrategy;
        contributionSelection = copy.contributionSelection;
        isExplorer = copy.isExplorer;
    }
    
    @Override
    public DataClusteringPSO getClone() {
        return new DataClusteringPSO(this);
    }

    @Override
    protected void algorithmIteration() {
        iterationStrategy.performIteration(this);
        
    }
    
    @Override
    public Topology<ClusterParticle> getTopology() {
        return topology;
    }

    @Override
    public void setTopology(Topology<? extends Entity> receivedTopology) {
        topology = (Topology<ClusterParticle>) receivedTopology;
    }
    
    @Override
    public void performInitialisation() {
        DataTable dataset = window.initializeWindow();
        
        Vector pattern = ((StandardPattern) dataset.getRow(0)).getVector();
            
        ((QuantizationErrorMinimizationProblem) this.optimisationProblem).setDimension((int) pattern.size());
        
        ((DataDependantPopulationInitializationStrategy) initialisationStrategy).setDataset(window.getCompleteDataset());
        Iterable<ClusterParticle> particles = (Iterable<ClusterParticle>) this.initialisationStrategy.initialise(this.getOptimisationProblem());
        
        topology.clear();
        topology.addAll(Lists.<ClusterParticle>newLinkedList(particles));
        
        ((SinglePopulationDataClusteringIterationStrategy) iterationStrategy).setWindow(window);
//        System.out.println("Old: " + window.getFrequency());
//        System.out.println("New: " + ((SinglePopulationDataClusteringIterationStrategy) iterationStrategy).getWindow().getFrequency());
    }
    
    @Override
    public OptimisationSolution getBestSolution() {
        ClusterParticle bestEntity = Topologies.getBestEntity(topology, new SocialBestFitnessComparator<ClusterParticle>());
        return new OptimisationSolution(bestEntity.getBestPosition(), bestEntity.getBestFitness());
    }

    @Override
    public List<OptimisationSolution> getSolutions() {
        List<OptimisationSolution> solutions = Lists.newLinkedList();
        for (ClusterParticle e : Topologies.getNeighbourhoodBestEntities(topology, new SocialBestFitnessComparator<ClusterParticle>())) {
            solutions.add(new OptimisationSolution(e.getBestPosition(), e.getBestFitness()));
        }
        return solutions;
        
    }

    
    @Override
    public ContributionSelectionStrategy getContributionSelectionStrategy() {
        return contributionSelection;
    }

    @Override
    public void setContributionSelectionStrategy(ContributionSelectionStrategy strategy) {
        contributionSelection = strategy;
    }
    
    /**
     * Sets the source URL of the the data table builder.
     * @param sourceURL the new source URL of the the data table builder.
     */
    public void setSourceURL(String sourceURL) {
        window.setSourceURL(sourceURL);
    }
    
    public void setWindow(SlidingWindow slidingWindow) {
        String url = window.getSourceURL();
        window = slidingWindow;
        window.setSourceURL(url);
    }
    
    public void setIterationStrategy(IterationStrategy strategy) {
        iterationStrategy = strategy;
    }
    
    public IterationStrategy getIterationStrategy() {
        return iterationStrategy;
    }
    
    public void setIsExplorer(boolean value) {
        isExplorer = value;
    }
    
    public boolean isExplorer() {
        return isExplorer;
    }
    
}
