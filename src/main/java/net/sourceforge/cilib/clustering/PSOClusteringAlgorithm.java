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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.coevolution.cooperative.ParticipatingAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.contributionselection.ContributionSelectionStrategy;
import net.sourceforge.cilib.coevolution.cooperative.contributionselection.ZeroContributionSelectionStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.comparator.SocialBestFitnessComparator;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.io.transform.DataOperator;
import net.sourceforge.cilib.io.transform.PatternConversionOperator;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.problem.QuantizationErrorMinimizationProblem;
import net.sourceforge.cilib.problem.boundaryconstraint.BoundaryConstraint;
import net.sourceforge.cilib.problem.boundaryconstraint.CentroidBoundaryConstraint;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 *
 * @author Kristina
 */
public class PSOClusteringAlgorithm extends SinglePopulationBasedAlgorithm implements ParticipatingAlgorithm {
    private Topology<ClusterParticle> topology;
    private ContributionSelectionStrategy contributionSelection;
    private DataTable dataset;
    private DataOperator patternConverstionOperator;
    private EuclideanDistanceMeasure distanceMeasure;
    private CentroidBoundaryConstraint boundaryConstraint;
    private SlidingWindow window;
    
    public PSOClusteringAlgorithm() {
        super();
        topology = new GBestTopology<ClusterParticle>();
        contributionSelection = new ZeroContributionSelectionStrategy();
        dataset = new StandardPatternDataTable();
        patternConverstionOperator = new PatternConversionOperator();
        distanceMeasure = new EuclideanDistanceMeasure();
        initialisationStrategy = new ClonedPopulationInitialisationStrategy<ClusterParticle>();
        boundaryConstraint = new CentroidBoundaryConstraint();
        window = new SlidingWindow();
    }

    public PSOClusteringAlgorithm(PSOClusteringAlgorithm copy) {
        super(copy);
        topology = copy.topology;
        contributionSelection = copy.contributionSelection;
        dataset = copy.dataset;
        patternConverstionOperator = copy.patternConverstionOperator;
        distanceMeasure = copy.distanceMeasure;
        initialisationStrategy = copy.initialisationStrategy;
        boundaryConstraint = copy.boundaryConstraint;
        window = copy.window;
    }
    
    @Override
    public SinglePopulationBasedAlgorithm getClone() {
        return new PSOClusteringAlgorithm(this);
    }

    @Override
    protected void algorithmIteration() {
        double euclideanDistance;
        Vector addedPattern;
        clearCentroidDistanceValues();
        
        for(ClusterParticle particle : topology) {
            CentroidHolder candidateSolution = (CentroidHolder) particle.getCandidateSolution();
            for(int i = 0; i < dataset.size(); i++) {
                euclideanDistance = Double.POSITIVE_INFINITY;
                addedPattern = Vector.of();
                Vector pattern = ((StandardPattern) dataset.getRow(i)).getVector();
                int centroidIndex = 0;
                int patternIndex = 0;
                //System.out.println("Pattern: " + pattern.toString());
                for(ClusterCentroid centroid : candidateSolution) {
                    if(distanceMeasure.distance(centroid.toVector(), pattern) < euclideanDistance) {
                        euclideanDistance = distanceMeasure.distance(centroid.toVector(), pattern);
                        addedPattern = Vector.copyOf(pattern);
                        patternIndex = centroidIndex;
                    }
                    centroidIndex++;
                }
                
                //System.out.println("--------------\n");
                //System.out.println("Selected Centroid: " + candidateSolution.get(patternIndex).toString() + "\n");
                
                candidateSolution.get(patternIndex).addDataItem(euclideanDistance, addedPattern);
            }
            
            particle.setCandidateSolution(candidateSolution);
            
            particle.calculateFitness();
            particle.updateVelocity();
            particle.updatePosition();
            
            boundaryConstraint.enforce(particle);
        }
        
        for (Iterator<? extends Particle> i = topology.iterator(); i.hasNext();) {
            Particle current = i.next();
            //current.calculateFitness();

            for (Iterator<? extends Particle> j = topology.neighbourhood(i); j.hasNext();) {
                Particle other = j.next();
                if (current.getSocialFitness().compareTo(other.getNeighbourhoodBest().getSocialFitness()) > 0) {
                    other.setNeighbourhoodBest(current); // TODO: neighbourhood visitor?
                }
            }
            
            //System.out.println("Clusters: " + current.getCandidateSolution().toString());
        }
        
        //System.out.println("\n\n");
        
        dataset = window.slideWindow(getIterations(), getPercentageComplete());
        
    }
    
    private void clearCentroidDistanceValues() {
        for(ClusterParticle particle : topology) {
            CentroidHolder candidateSolution = (CentroidHolder) particle.getCandidateSolution();
            
            for(ClusterCentroid centroid : candidateSolution) {
                centroid.clearDataItems();
            }
        }
    }

    @Override
    public Topology<? extends Entity> getTopology() {
        return topology;
    }

    @Override
    public void setTopology(Topology<? extends Entity> topology) {
        this.topology = (Topology<ClusterParticle>) topology;
    }
    
    @Override
    public void performInitialisation() {
        dataset = window.initializeWindow();
        Vector pattern = ((StandardPattern) dataset.getRow(0)).getVector();
        ((QuantizationErrorMinimizationProblem) this.optimisationProblem).setDimension(pattern.size());
        Iterable<ClusterParticle> particles = (Iterable<ClusterParticle>) this.initialisationStrategy.initialise(this.getOptimisationProblem());
        topology.clear();
        topology.addAll(Lists.<ClusterParticle>newLinkedList(particles));
        
    }

    @Override
    public OptimisationSolution getBestSolution() {
        ClusterParticle bestEntity = Topologies.getBestEntity(topology, new SocialBestFitnessComparator<ClusterParticle>());
        return new OptimisationSolution(bestEntity.getBestPosition(), bestEntity.getBestFitness());
    }

    @Override
    public Iterable<OptimisationSolution> getSolutions() {
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
     * Get the { @link PatternConversionOperator}
     * @return the pattern conversion operator
     */
    public DataOperator getPatternConversionOperator() {
        return patternConverstionOperator;
    }

    /**
     * Set the { @link PatternConversionOperator}
     * @param patternConverstionOperator the new pattern conversion operator
     */
    public void setPatternConversionOperator(DataOperator patternConverstionOperator) {
        this.patternConverstionOperator = patternConverstionOperator;
    }
    
    /**
     * Get the Distance Measure
     * @return the Distance Measure
     */
    public DistanceMeasure getDistanceMeasure() {
        return distanceMeasure;
    }
    
    /**
     * Get the Dataset
     * @return the Dataset
     */
    public DataTable getDataset() {
        return dataset;
    }
    
    public void setBoundaryConstraint(BoundaryConstraint constraint) {
        boundaryConstraint.setDelegate(constraint);
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

}
